package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

object AttentionNotificationHelper {
    private const val CHANNEL_ID = "focus_pulse_eye_protection"
    private const val NOTIFICATION_ID = 2020

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "FocusPulse 20-20-20 Göz Koruması"
            val descriptionText = "20 dakikada bir gelen göz dinlendirme ve odaklanma bildirimleri"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 350, 150, 350, 150, 500)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Sends the required 20-20-20 notification and plays an attention-grabbing alert chime.
     */
    fun send202020Alert(context: Context, scope: CoroutineScope) {
        // 1. Play High-Attention Synthesized Chime (Crystal clear ascending bell chime)
        playAttentionChime(scope)

        // 2. Vibrate phone
        triggerVibration(context)

        // 3. Post Android System Notification
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("👁️ 20 Dakika Doldu!")
            .setContentText("20 Dakika Doldu 5 Dakika Uzaklara Bak! Sonra İşine Devam Et")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("20 Dakika Doldu 5 Dakika Uzaklara Bak! Sonra İşine Devam Et.\n\n👁️ Göz kaslarını gevşetmek için 20 fit (en az 6 metre) uzaktaki bir noktaya odaklan ve derin nefes al.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        try {
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(NOTIFICATION_ID, builder.build())
        } catch (e: SecurityException) {
            // Android 13+ permission not yet granted; audio chime still played!
        }
    }

    /**
     * Ascending bell chime (880Hz -> 1175Hz -> 1568Hz -> 2093Hz) designed to grab attention
     * without being harsh.
     */
    fun playAttentionChime(scope: CoroutineScope) {
        scope.launch(Dispatchers.Default) {
            val sampleRate = 24000
            val frequencies = doubleArrayOf(880.0, 1175.0, 1568.0, 2093.0)
            val noteDurationMs = 180
            val totalSamples = (sampleRate * (noteDurationMs * frequencies.size + 400) / 1000)
            val buffer = ShortArray(totalSamples)

            var sampleIndex = 0
            for (freq in frequencies) {
                val samplesForNote = (sampleRate * noteDurationMs / 1000)
                val dt = 1.0 / sampleRate
                val twoPiFreq = 2.0 * PI * freq

                for (n in 0 until samplesForNote) {
                    if (sampleIndex >= totalSamples) break
                    val t = n * dt
                    // Bell decay envelope
                    val envelope = exp(-3.2 * t)
                    // Harmonic overtone for rich attention-grabbing bell tone
                    val wave = sin(twoPiFreq * t) + 0.4 * sin(twoPiFreq * 2.0 * t) + 0.2 * sin(twoPiFreq * 3.0 * t)
                    val sampleVal = (wave * envelope * 0.7 * 32767.0).coerceIn(-32767.0, 32767.0).toInt().toShort()
                    buffer[sampleIndex++] = sampleVal
                }
            }

            // Tail sustain ring
            val tailFreq = frequencies.last()
            val remaining = totalSamples - sampleIndex
            val dt = 1.0 / sampleRate
            for (n in 0 until remaining) {
                val t = n * dt
                val envelope = exp(-2.0 * t)
                val wave = sin(2.0 * PI * tailFreq * t) + 0.3 * sin(2.0 * PI * tailFreq * 2.0 * t)
                val sampleVal = (wave * envelope * 0.6 * 32767.0).coerceIn(-32767.0, 32767.0).toInt().toShort()
                buffer[sampleIndex++] = sampleVal
            }

            try {
                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                track.write(buffer, 0, buffer.size)
                track.play()
                // Let it finish playing
                kotlinx.coroutines.delay(1200)
                track.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun triggerVibration(context: Context) {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(
                    VibrationEffect.createWaveform(
                        longArrayOf(0, 300, 150, 400),
                        -1
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(longArrayOf(0, 300, 150, 400), -1)
            }
        } catch (e: Exception) {
            // Ignore if vibration permission or hardware unavailable
        }
    }
}
