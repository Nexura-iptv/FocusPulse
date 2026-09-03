package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.components.EyeProtectionOverlay
import com.example.ui.screens.AmbientSoundsScreen
import com.example.ui.screens.CoursesScreen
import com.example.ui.screens.PomodoroScreen
import com.example.ui.screens.YouTubeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.viewmodel.FocusPulseViewModel

enum class MainTab(val title: String, val icon: ImageVector) {
    POMODORO("Odak", Icons.Default.Timer),
    COURSES("Dersler", Icons.Default.MenuBook),
    AMBIENT("Ambiyans", Icons.Default.GraphicEq),
    YOUTUBE("YouTube", Icons.Default.VideoLibrary)
}

class MainActivity : ComponentActivity() {

    private val viewModel: FocusPulseViewModel by viewModels()

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { _ ->
            // Notification permission handled
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Request notification permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            MyApplicationTheme {
                FocusPulseApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun FocusPulseApp(viewModel: FocusPulseViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val soundVolumes by viewModel.soundVolumes.collectAsState()
    val selectedGradeId by viewModel.selectedGradeId.collectAsState()
    val selectedBranch by viewModel.selectedBranch.collectAsState()
    val currentVideoId by viewModel.currentVideoId.collectAsState()
    val currentChannelUrl by viewModel.currentChannelUrl.collectAsState()

    var selectedTab by remember { mutableStateOf(MainTab.POMODORO) }

    // 20-20-20 Eye Protection Animated Overlay
    if (uiState.isEyeProtectionActive) {
        EyeProtectionOverlay(
            remainingSeconds = uiState.eyeBreakRemainingSeconds,
            isExtended5Min = uiState.isEyeBreakExtended5Min,
            onDismiss = { viewModel.dismissEyeBreak() },
            onExtend5Min = { viewModel.extendEyeBreakTo5Min() }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate950),
        topBar = {
            TopPulseHeader(
                activeCourseTitle = uiState.activeCourse?.title,
                isTimerRunning = uiState.isRunning,
                onHeaderClick = { selectedTab = MainTab.POMODORO }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Slate950)
            ) {
                // Mini-bar if timer or ambient sounds are running while on another tab
                if (selectedTab != MainTab.POMODORO && (uiState.isRunning || viewModel.audioEngine.isPlaying)) {
                    MiniPlaybackBar(
                        isTimerRunning = uiState.isRunning,
                        remainingSeconds = uiState.remainingSeconds,
                        isAmbientPlaying = viewModel.audioEngine.isPlaying,
                        onToggleTimer = {
                            if (uiState.isRunning) viewModel.pauseTimer() else viewModel.startTimer()
                        },
                        onNavigateToTimer = { selectedTab = MainTab.POMODORO }
                    )
                }

                // Lucide-styled Bottom Navigation Bar
                BottomNavBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Slate950)
        ) {
            Crossfade(targetState = selectedTab, label = "tabCrossfade") { tab ->
                when (tab) {
                    MainTab.POMODORO -> PomodoroScreen(
                        state = uiState,
                        onStart = { viewModel.startTimer() },
                        onPause = { viewModel.pauseTimer() },
                        onReset = { viewModel.resetTimer() },
                        onSkip = { viewModel.skipMode() },
                        onSelectPreset = { viewModel.selectPreset(it) },
                        onSetCustomDurations = { w, b -> viewModel.setCustomDurations(w, b) },
                        onNavigateToCourses = { selectedTab = MainTab.COURSES },
                        onTest202020Alert = { viewModel.test202020Break() }
                    )

                    MainTab.COURSES -> CoursesScreen(
                        selectedGradeId = selectedGradeId,
                        selectedBranch = selectedBranch,
                        activeCourse = uiState.activeCourse,
                        onSelectGrade = { viewModel.setGradeFilter(it) },
                        onSelectBranch = { viewModel.setBranchFilter(it) },
                        onSelectActiveCourse = { course ->
                            viewModel.selectCourse(course)
                            selectedTab = MainTab.POMODORO
                        },
                        onOpenYouTubeForCourse = { course ->
                            val channel = com.example.model.YouTubeDirectory.quickAccessChannels
                                .firstOrNull { it.name.contains(course.recommendedChannel, ignoreCase = true) }
                            if (channel != null) {
                                viewModel.openChannel(channel.url)
                            } else {
                                viewModel.playYouTubeVideoOrUrl("https://www.youtube.com/results?search_query=${course.title}")
                            }
                            selectedTab = MainTab.YOUTUBE
                        }
                    )

                    MainTab.AMBIENT -> AmbientSoundsScreen(
                        soundVolumes = soundVolumes,
                        onToggleSound = { viewModel.toggleSound(it) },
                        onSetVolume = { type, vol -> viewModel.setSoundVolume(type, vol) },
                        onStopAll = { viewModel.stopAllSounds() }
                    )

                    MainTab.YOUTUBE -> YouTubeScreen(
                        currentVideoId = currentVideoId,
                        currentChannelUrl = currentChannelUrl,
                        onPlayCustomInput = { viewModel.playYouTubeVideoOrUrl(it) },
                        onOpenChannel = { viewModel.openChannel(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TopPulseHeader(
    activeCourseTitle: String?,
    isTimerRunning: Boolean,
    onHeaderClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_dot")
    val dotPulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Slate950)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Pulse Dot
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onHeaderClick() }
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .scale(if (isTimerRunning) dotPulse else 1f)
                        .clip(CircleShape)
                        .background(if (isTimerRunning) NeonEmerald else NeonCyan)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Focus",
                    color = Slate100,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Pulse",
                    color = NeonEmerald,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            // Active Subject Chip
            if (activeCourseTitle != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Slate900)
                        .border(1.dp, Slate800, RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = NeonViolet,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = activeCourseTitle,
                            color = Slate100,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniPlaybackBar(
    isTimerRunning: Boolean,
    remainingSeconds: Int,
    isAmbientPlaying: Boolean,
    onToggleTimer: () -> Unit,
    onNavigateToTimer: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Slate900)
            .border(1.dp, Slate800, RoundedCornerShape(12.dp))
            .clickable { onNavigateToTimer() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (isTimerRunning) NeonEmerald else NeonCyan)
                )
                Spacer(modifier = Modifier.width(8.dp))
                val mins = remainingSeconds / 60
                val secs = remainingSeconds % 60
                Text(
                    text = String.format("%02d:%02d", mins, secs),
                    color = Slate100,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                if (isAmbientPlaying) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• Ambiyans Aktif",
                        color = NeonCyan,
                        fontSize = 11.sp
                    )
                }
            }

            IconButton(
                onClick = onToggleTimer,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = NeonEmerald,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun BottomNavBar(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Slate950)
            .border(
                width = 1.dp,
                color = Slate800,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(Slate900)
            .navigationBarsPadding()
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .testTag("bottom_nav_bar")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainTab.values().forEach { tab ->
                val isSelected = tab == selectedTab
                val accentColor = when (tab) {
                    MainTab.POMODORO -> NeonEmerald
                    MainTab.COURSES -> NeonViolet
                    MainTab.AMBIENT -> NeonCyan
                    MainTab.YOUTUBE -> NeonEmerald
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTabSelected(tab) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag("tab_${tab.name.lowercase()}")
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.title,
                        tint = if (isSelected) accentColor else Slate400,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = tab.title,
                        color = if (isSelected) Slate100 else Slate400,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
