package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = NeonEmerald,
    onPrimary = Slate950,
    primaryContainer = Slate850,
    onPrimaryContainer = NeonEmeraldGlow,

    secondary = NeonCyan,
    onSecondary = Slate950,
    secondaryContainer = Slate800,
    onSecondaryContainer = NeonCyanGlow,

    tertiary = NeonViolet,
    onTertiary = Slate950,
    tertiaryContainer = Slate850,
    onTertiaryContainer = NeonVioletGlow,

    background = Slate950,
    onBackground = Slate100,

    surface = Slate900,
    onSurface = Slate100,
    surfaceVariant = Slate850,
    onSurfaceVariant = Slate300,

    outline = Slate700,
    outlineVariant = Slate800,

    error = NeonRose,
    onError = Slate950
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    // FocusPulse is designed strictly with Dark Mode, Slate/Zinc foundation
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
