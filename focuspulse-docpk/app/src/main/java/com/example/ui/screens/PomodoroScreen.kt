package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PomodoroPreset
import com.example.model.PomodoroUiState
import com.example.model.TimerMode
import com.example.ui.components.CustomTimerDialog
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanGlow
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonEmeraldGlow
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950

@Composable
fun PomodoroScreen(
    state: PomodoroUiState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onSkip: () -> Unit,
    onSelectPreset: (PomodoroPreset) -> Unit,
    onSetCustomDurations: (workMin: Int, breakMin: Int) -> Unit,
    onNavigateToCourses: () -> Unit,
    onTest202020Alert: () -> Unit
) {
    var showCustomDialog by remember { mutableStateOf(false) }

    if (showCustomDialog) {
        CustomTimerDialog(
            initialWorkMin = state.workMinutes,
            initialBreakMin = state.breakMinutes,
            onDismiss = { showCustomDialog = false },
            onConfirm = { w, b ->
                onSetCustomDurations(w, b)
                showCustomDialog = false
            }
        )
    }

    val activeColor = when (state.mode) {
        TimerMode.WORK -> NeonEmerald
        TimerMode.SHORT_BREAK -> NeonAmber
        TimerMode.LONG_BREAK -> NeonCyan
    }

    val activeGlow = when (state.mode) {
        TimerMode.WORK -> NeonEmeraldGlow
        TimerMode.SHORT_BREAK -> NeonAmber
        TimerMode.LONG_BREAK -> NeonCyanGlow
    }

    val animatedColor by animateColorAsState(targetValue = activeColor, label = "timerColor")

    val progress = if (state.totalSecondsForCurrentMode > 0) {
        state.remainingSeconds.toFloat() / state.totalSecondsForCurrentMode.toFloat()
    } else 1f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 350),
        label = "progress"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate950)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Active Course Selection Badge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Slate900)
                .border(1.dp, Slate800, RoundedCornerShape(16.dp))
                .clickable { onNavigateToCourses() }
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .testTag("active_course_badge")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(NeonViolet.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "Ders",
                            tint = NeonViolet,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Aktif Çalışma Dersi",
                            color = Slate400,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = state.activeCourse?.title ?: "Ders Seçilmedi",
                            color = Slate100,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Slate800)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = state.activeCourse?.branch?.displayName ?: "Değiştir",
                        color = NeonViolet,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 20-20-20 Eye Protection Status Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Slate900),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(Slate800, NeonCyan.copy(alpha = 0.35f)))
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(NeonCyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "20-20-20",
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "20-20-20 Göz Koruması",
                                color = Slate100,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(NeonCyan)
                            )
                        }
                        val minsWork = state.workSecondsSinceLastEyeBreak / 60
                        Text(
                            text = "Döngü: $minsWork / 20 dk (20 dk'da sesli uyarı)",
                            color = Slate400,
                            fontSize = 11.sp
                        )
                    }
                }

                // Quick test trigger button
                OutlinedButton(
                    onClick = onTest202020Alert,
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("test_202020_btn"),
                    shape = RoundedCornerShape(10.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.horizontalGradient(listOf(NeonCyan.copy(alpha = 0.5f), NeonCyan))
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = NeonCyan.copy(alpha = 0.1f),
                        contentColor = NeonCyan
                    ),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Uyarısı Test Et", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Main Circular Glowing Timer
        Box(
            modifier = Modifier
                .size(270.dp)
                .testTag("pomodoro_circular_timer"),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(255.dp)) {
                val strokeWidth = 14.dp.toPx()
                val centerOffset = Offset(size.width / 2, size.height / 2)
                val arcRadius = (size.width - strokeWidth) / 2
                val arcTopLeft = Offset(centerOffset.x - arcRadius, centerOffset.y - arcRadius)
                val arcSize = Size(arcRadius * 2, arcRadius * 2)

                // Background track
                drawArc(
                    color = Slate800,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Active Progress Arc
                drawArc(
                    brush = Brush.sweepGradient(
                        listOf(
                            animatedColor,
                            activeGlow,
                            animatedColor
                        )
                    ),
                    startAngle = -90f,
                    sweepAngle = animatedProgress * 360f,
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            // Inner Timer Details
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Mode Tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(animatedColor.copy(alpha = 0.15f))
                        .border(1.dp, animatedColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = state.mode.title.uppercase(),
                        color = animatedColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                val minutes = state.remainingSeconds / 60
                val seconds = state.remainingSeconds % 60
                val formattedTime = String.format("%02d:%02d", minutes, seconds)

                Text(
                    text = formattedTime,
                    color = Slate100,
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-1).sp
                )

                Text(
                    text = if (state.completedSessions > 0) "${state.completedSessions}. Oturum Tamamlandı" else "Günün İlk Oturumu",
                    color = Slate400,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Timer Controls: Reset, Start/Pause, Skip
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Reset Button
            IconButton(
                onClick = onReset,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Slate900)
                    .border(1.dp, Slate800, CircleShape)
                    .testTag("reset_timer_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Sıfırla",
                    tint = Slate300,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            // Primary Play/Pause Button
            Button(
                onClick = { if (state.isRunning) onPause() else onStart() },
                modifier = Modifier
                    .height(56.dp)
                    .width(160.dp)
                    .testTag("start_pause_timer_button"),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = animatedColor,
                    contentColor = Slate950
                )
            ) {
                Icon(
                    imageVector = if (state.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (state.isRunning) "Duraklat" else "Başlat",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (state.isRunning) "DURAKLAT" else "BAŞLAT",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            // Skip Mode Button
            IconButton(
                onClick = onSkip,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Slate900)
                    .border(1.dp, Slate800, CircleShape)
                    .testTag("skip_timer_button")
            ) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Sonraki Mod",
                    tint = Slate300,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Preset Duration Selector Pills
        Text(
            text = "POMODORO SÜRE MODLARI",
            color = Slate400,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PresetButton(
                title = "25 / 5 Dk",
                subtitle = "Klasik",
                isSelected = state.preset == PomodoroPreset.CLASSIC_25_5,
                modifier = Modifier.weight(1f),
                onClick = { onSelectPreset(PomodoroPreset.CLASSIC_25_5) }
            )

            PresetButton(
                title = "50 / 10 Dk",
                subtitle = "Derin Odak",
                isSelected = state.preset == PomodoroPreset.DEEP_50_10,
                modifier = Modifier.weight(1f),
                onClick = { onSelectPreset(PomodoroPreset.DEEP_50_10) }
            )

            PresetButton(
                title = "Özel Süre",
                subtitle = "${state.workMinutes}/${state.breakMinutes} Dk",
                isSelected = state.preset == PomodoroPreset.CUSTOM,
                icon = Icons.Default.Tune,
                modifier = Modifier.weight(1f),
                onClick = { showCustomDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Today's Stats Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Slate900),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Slate800))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatColumn(label = "Bugün Odak", value = "${state.totalFocusMinutesToday} dk")
                Box(modifier = Modifier.width(1.dp).height(36.dp).background(Slate800))
                StatColumn(label = "Tamamlanan", value = "${state.completedSessions} seans")
                Box(modifier = Modifier.width(1.dp).height(36.dp).background(Slate800))
                StatColumn(label = "20-20-20 Mola", value = "${(state.totalFocusMinutesToday / 20)} kez")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun PresetButton(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) Slate800 else Slate900)
            .border(
                1.5.dp,
                if (isSelected) NeonEmerald else Slate800,
                RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) NeonEmerald else Slate400,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
            Text(
                text = title,
                color = if (isSelected) NeonEmerald else Slate100,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = Slate400,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun StatColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = Slate400, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, color = Slate100, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}
