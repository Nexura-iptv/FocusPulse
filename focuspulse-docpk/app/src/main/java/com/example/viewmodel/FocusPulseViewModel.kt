package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.AmbientAudioEngine
import com.example.audio.AmbientSoundType
import com.example.model.CourseItem
import com.example.model.CourseRepository
import com.example.model.PomodoroPreset
import com.example.model.PomodoroUiState
import com.example.model.SubjectBranch
import com.example.model.TimerMode
import com.example.model.YouTubeDirectory
import com.example.notification.AttentionNotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class FocusPulseViewModel(application: Application) : AndroidViewModel(application) {

    val audioEngine = AmbientAudioEngine(viewModelScope)

    private val _uiState = MutableStateFlow(
        PomodoroUiState(
            activeCourse = CourseRepository.allCourses.firstOrNull { it.id == "yks_tyt_mat" }
        )
    )
    val uiState: StateFlow<PomodoroUiState> = _uiState.asStateFlow()

    // Ambient Sound states for UI reactivity
    private val _soundVolumes = MutableStateFlow<Map<AmbientSoundType, Float>>(
        AmbientSoundType.values().associateWith { 0f }
    )
    val soundVolumes: StateFlow<Map<AmbientSoundType, Float>> = _soundVolumes.asStateFlow()

    // Course selection filters
    private val _selectedGradeId = MutableStateFlow("YKS_TYT")
    val selectedGradeId: StateFlow<String> = _selectedGradeId.asStateFlow()

    private val _selectedBranch = MutableStateFlow(SubjectBranch.ALL)
    val selectedBranch: StateFlow<SubjectBranch> = _selectedBranch.asStateFlow()

    // YouTube state
    private val _currentVideoId = MutableStateFlow<String?>("jfKfPfyJRdk") // Default lofi/focus study live ID
    val currentVideoId: StateFlow<String?> = _currentVideoId.asStateFlow()

    private val _currentChannelUrl = MutableStateFlow<String?>(null)
    val currentChannelUrl: StateFlow<String?> = _currentChannelUrl.asStateFlow()

    private var timerJob: Job? = null
    private var eyeBreakJob: Job? = null

    init {
        AttentionNotificationHelper.createNotificationChannel(application.applicationContext)
    }

    fun startTimer() {
        if (_uiState.value.isRunning) return
        _uiState.update { it.copy(isRunning = true) }

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive && _uiState.value.isRunning) {
                delay(1000L)
                val current = _uiState.value
                val newRemaining = current.remainingSeconds - 1

                // Track work seconds for 20-20-20 rule
                var newWorkSinceBreak = current.workSecondsSinceLastEyeBreak
                if (current.mode == TimerMode.WORK) {
                    newWorkSinceBreak += 1
                }

                // Check 20-20-20 trigger: exactly every 20 minutes (1200 seconds)
                val triggerEyeBreak = (current.mode == TimerMode.WORK && newWorkSinceBreak >= 1200)

                if (triggerEyeBreak) {
                    // Trigger alert and mask
                    trigger202020Break()
                    newWorkSinceBreak = 0
                }

                if (newRemaining <= 0) {
                    // Timer finished for current mode
                    handleTimerComplete()
                    break
                } else {
                    _uiState.update {
                        it.copy(
                            remainingSeconds = newRemaining,
                            workSecondsSinceLastEyeBreak = newWorkSinceBreak,
                            totalFocusMinutesToday = if (it.mode == TimerMode.WORK && (it.remainingSeconds % 60 == 0))
                                it.totalFocusMinutesToday + 1 else it.totalFocusMinutesToday
                        )
                    }
                }
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(isRunning = false) }
    }

    fun resetTimer() {
        pauseTimer()
        val current = _uiState.value
        val totalSec = if (current.mode == TimerMode.WORK) current.workMinutes * 60 else current.breakMinutes * 60
        _uiState.update {
            it.copy(
                remainingSeconds = totalSec,
                totalSecondsForCurrentMode = totalSec,
                workSecondsSinceLastEyeBreak = 0
            )
        }
    }

    fun skipMode() {
        pauseTimer()
        val current = _uiState.value
        val nextMode = if (current.mode == TimerMode.WORK) TimerMode.SHORT_BREAK else TimerMode.WORK
        val totalSec = if (nextMode == TimerMode.WORK) current.workMinutes * 60 else current.breakMinutes * 60
        _uiState.update {
            it.copy(
                mode = nextMode,
                remainingSeconds = totalSec,
                totalSecondsForCurrentMode = totalSec,
                workSecondsSinceLastEyeBreak = 0
            )
        }
    }

    private fun handleTimerComplete() {
        val current = _uiState.value
        val nextMode = if (current.mode == TimerMode.WORK) {
            val sessions = current.completedSessions + 1
            if (sessions % 4 == 0) TimerMode.LONG_BREAK else TimerMode.SHORT_BREAK
        } else {
            TimerMode.WORK
        }

        val totalSec = when (nextMode) {
            TimerMode.WORK -> current.workMinutes * 60
            TimerMode.SHORT_BREAK -> current.breakMinutes * 60
            TimerMode.LONG_BREAK -> (current.breakMinutes * 3).coerceAtLeast(15) * 60
        }

        AttentionNotificationHelper.playAttentionChime(viewModelScope)

        _uiState.update {
            it.copy(
                mode = nextMode,
                remainingSeconds = totalSec,
                totalSecondsForCurrentMode = totalSec,
                isRunning = false,
                completedSessions = if (current.mode == TimerMode.WORK) it.completedSessions + 1 else it.completedSessions,
                workSecondsSinceLastEyeBreak = 0
            )
        }
    }

    fun selectPreset(preset: PomodoroPreset) {
        pauseTimer()
        _uiState.update {
            val totalSec = if (it.mode == TimerMode.WORK) preset.workMin * 60 else preset.breakMin * 60
            it.copy(
                preset = preset,
                workMinutes = preset.workMin,
                breakMinutes = preset.breakMin,
                remainingSeconds = totalSec,
                totalSecondsForCurrentMode = totalSec,
                workSecondsSinceLastEyeBreak = 0
            )
        }
    }

    fun setCustomDurations(workMin: Int, breakMin: Int) {
        val safeWork = workMin.coerceIn(1, 120)
        val safeBreak = breakMin.coerceIn(1, 60)
        pauseTimer()
        _uiState.update {
            val totalSec = if (it.mode == TimerMode.WORK) safeWork * 60 else safeBreak * 60
            it.copy(
                preset = PomodoroPreset.CUSTOM,
                workMinutes = safeWork,
                breakMinutes = safeBreak,
                remainingSeconds = totalSec,
                totalSecondsForCurrentMode = totalSec,
                workSecondsSinceLastEyeBreak = 0
            )
        }
    }

    // 20-20-20 Eye Protection
    fun trigger202020Break() {
        // Play distinct attention chime & fire notification
        AttentionNotificationHelper.send202020Alert(getApplication(), viewModelScope)

        _uiState.update {
            it.copy(
                isEyeProtectionActive = true,
                eyeBreakRemainingSeconds = 20,
                isEyeBreakExtended5Min = false
            )
        }

        startEyeBreakCountdown(20)
    }

    fun test202020Break() {
        trigger202020Break()
    }

    private fun startEyeBreakCountdown(seconds: Int) {
        eyeBreakJob?.cancel()
        eyeBreakJob = viewModelScope.launch {
            var rem = seconds
            while (isActive && rem > 0 && _uiState.value.isEyeProtectionActive) {
                delay(1000L)
                rem -= 1
                _uiState.update { it.copy(eyeBreakRemainingSeconds = rem) }
            }
            if (rem <= 0 && _uiState.value.isEyeProtectionActive) {
                // Eye break ended smoothly with a pleasant chime
                AttentionNotificationHelper.playAttentionChime(viewModelScope)
            }
        }
    }

    fun extendEyeBreakTo5Min() {
        _uiState.update {
            it.copy(
                isEyeBreakExtended5Min = true,
                eyeBreakRemainingSeconds = 5 * 60
            )
        }
        startEyeBreakCountdown(5 * 60)
    }

    fun dismissEyeBreak() {
        eyeBreakJob?.cancel()
        _uiState.update {
            it.copy(
                isEyeProtectionActive = false,
                workSecondsSinceLastEyeBreak = 0
            )
        }
    }

    // Active Course & Filters
    fun selectCourse(course: CourseItem) {
        _uiState.update { it.copy(activeCourse = course) }
    }

    fun setGradeFilter(gradeId: String) {
        _selectedGradeId.value = gradeId
    }

    fun setBranchFilter(branch: SubjectBranch) {
        _selectedBranch.value = branch
    }

    // Ambient Audio Controls
    fun toggleSound(type: AmbientSoundType) {
        audioEngine.toggleSound(type)
        syncVolumes()
    }

    fun setSoundVolume(type: AmbientSoundType, volume: Float) {
        audioEngine.setVolume(type, volume)
        syncVolumes()
    }

    fun stopAllSounds() {
        audioEngine.stopAll()
        syncVolumes()
    }

    private fun syncVolumes() {
        val map = AmbientSoundType.values().associateWith { audioEngine.getVolume(it) }
        _soundVolumes.value = map
    }

    // YouTube Controls
    fun playYouTubeVideoOrUrl(input: String) {
        val extractedId = YouTubeDirectory.extractVideoId(input)
        if (extractedId != null) {
            _currentVideoId.value = extractedId
            _currentChannelUrl.value = null
        } else if (input.startsWith("http://") || input.startsWith("https://")) {
            // Full web url (e.g. channel URL)
            _currentChannelUrl.value = input
            _currentVideoId.value = null
        }
    }

    fun openChannel(url: String) {
        _currentChannelUrl.value = url
        _currentVideoId.value = null
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        eyeBreakJob?.cancel()
        audioEngine.stopAll()
    }
}
