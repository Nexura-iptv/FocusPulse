package com.example.model

enum class TimerMode(val title: String) {
    WORK("Çalışma Modu"),
    SHORT_BREAK("Kısa Mola"),
    LONG_BREAK("Uzun Mola")
}

enum class PomodoroPreset(val workMin: Int, val breakMin: Int, val label: String) {
    CLASSIC_25_5(25, 5, "25 / 5 Dk"),
    DEEP_50_10(50, 10, "50 / 10 Dk"),
    CUSTOM(30, 7, "Özel Süre")
}

data class PomodoroUiState(
    val mode: TimerMode = TimerMode.WORK,
    val preset: PomodoroPreset = PomodoroPreset.CLASSIC_25_5,
    val workMinutes: Int = 25,
    val breakMinutes: Int = 5,
    val remainingSeconds: Int = 25 * 60,
    val totalSecondsForCurrentMode: Int = 25 * 60,
    val isRunning: Boolean = false,
    val completedSessions: Int = 0,
    val totalFocusMinutesToday: Int = 0,
    val workSecondsSinceLastEyeBreak: Int = 0,
    val isEyeProtectionActive: Boolean = false,
    val eyeBreakRemainingSeconds: Int = 20,
    val isEyeBreakExtended5Min: Boolean = false,
    val activeCourse: CourseItem? = null
)
