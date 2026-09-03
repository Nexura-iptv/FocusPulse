package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Random
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

enum class AmbientSoundType(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String
) {
    RAIN("rain", "Yağmur", "Hafif ve yatıştırıcı yağmur damlaları", "CloudRain"),
    GAMMA_40HZ("gamma_40hz", "40 Hz Odak Dalgaları", "Zihinsel netlik ve derin konsantrasyon dalgası", "Zap"),
    WHITE_NOISE("white_noise", "Beyaz Gürültü", "Dış sesleri izole eden pürüzsüz frekans", "Radio"),
    LIBRARY("library", "Kütüphane Uğultusu", "Sessiz çalışma salonu ve ahşap yankısı", "BookOpen"),
    FIREPLACE("fireplace", "Şömine Çıtırtısı", "Odun çıtırtıları ve sıcak alev uğultusu", "Flame")
}

class AmbientAudioEngine(
    private val scope: CoroutineScope
) {
    private val sampleRate = 22050
    private val bufferSize = AudioTrack.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    ).coerceAtLeast(4096)

    private var audioTrack: AudioTrack? = null
    private var playbackJob: Job? = null

    // Volume levels for each ambient sound: 0.0f .. 1.0f
    private val volumes = mutableMapOf<AmbientSoundType, Float>().apply {
        AmbientSoundType.values().forEach { put(it, 0f) }
    }

    private var masterVolume = 0.8f
    val isPlaying: Boolean
        get() = volumes.values.any { it > 0.01f } && playbackJob?.isActive == true

    fun setVolume(type: AmbientSoundType, volume: Float) {
        volumes[type] = volume.coerceIn(0f, 1f)
        checkPlaybackState()
    }

    fun getVolume(type: AmbientSoundType): Float = volumes[type] ?: 0f

    fun toggleSound(type: AmbientSoundType) {
        val current = getVolume(type)
        if (current > 0.01f) {
            setVolume(type, 0f)
        } else {
            setVolume(type, 0.65f)
        }
    }

    fun stopAll() {
        AmbientSoundType.values().forEach { volumes[it] = 0f }
        stopAudioTrack()
    }

    private fun checkPlaybackState() {
        val hasActiveSound = volumes.values.any { it > 0.01f }
        if (hasActiveSound && (playbackJob == null || !playbackJob!!.isActive)) {
            startAudioTrack()
        } else if (!hasActiveSound) {
            stopAudioTrack()
        }
    }

    private fun startAudioTrack() {
        stopAudioTrack()

        try {
            audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            audioTrack?.play()
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

        playbackJob = scope.launch(Dispatchers.Default) {
            generateAudioLoop()
        }
    }

    private fun stopAudioTrack() {
        playbackJob?.cancel()
        playbackJob = null
        try {
            audioTrack?.pause()
            audioTrack?.flush()
            audioTrack?.stop()
            audioTrack?.release()
        } catch (e: Exception) {
            // Ignore track release issues
        }
        audioTrack = null
    }

    private fun generateAudioLoop() {
        val shortBuffer = ShortArray(1024)
        val random = Random()

        var phaseCarrier = 0.0
        var phaseMod = 0.0
        var filterLast = 0.0
        var libraryPhase1 = 0.0
        var libraryPhase2 = 0.0
        var crackleDecay = 0.0

        val twoPi = 2.0 * PI
        val dt = 1.0 / sampleRate

        while (scope.isActive && audioTrack != null) {
            val vRain = volumes[AmbientSoundType.RAIN] ?: 0f
            val vGamma = volumes[AmbientSoundType.GAMMA_40HZ] ?: 0f
            val vWhite = volumes[AmbientSoundType.WHITE_NOISE] ?: 0f
            val vLib = volumes[AmbientSoundType.LIBRARY] ?: 0f
            val vFire = volumes[AmbientSoundType.FIREPLACE] ?: 0f

            if (vRain <= 0f && vGamma <= 0f && vWhite <= 0f && vLib <= 0f && vFire <= 0f) {
                break
            }

            for (i in shortBuffer.indices) {
                var mixedSample = 0.0

                // 1. Yağmur Sesi (Filtered Noise + Random Droplet clicks)
                if (vRain > 0.01f) {
                    val rawNoise = (random.nextDouble() * 2.0 - 1.0)
                    filterLast = filterLast * 0.85 + rawNoise * 0.15
                    var dropClick = 0.0
                    if (random.nextInt(400) == 0) {
                        dropClick = (random.nextDouble() * 0.7)
                    }
                    mixedSample += (filterLast * 0.7 + dropClick) * vRain
                }

                // 2. 40 Hz Odak Dalgaları (200Hz carrier modulated with 40Hz isochronic pulse)
                if (vGamma > 0.01f) {
                    phaseCarrier += twoPi * 196.0 * dt
                    if (phaseCarrier > twoPi) phaseCarrier -= twoPi

                    phaseMod += twoPi * 40.0 * dt
                    if (phaseMod > twoPi) phaseMod -= twoPi

                    val carrier = sin(phaseCarrier)
                    val mod = 0.55 + 0.45 * sin(phaseMod)
                    mixedSample += (carrier * mod * 0.65) * vGamma
                }

                // 3. Beyaz Gürültü (Smooth Pink/White noise)
                if (vWhite > 0.01f) {
                    val white = (random.nextDouble() * 2.0 - 1.0) * 0.35
                    mixedSample += white * vWhite
                }

                // 4. Kütüphane Uğultusu (Warm low resonance murmur)
                if (vLib > 0.01f) {
                    libraryPhase1 += twoPi * 85.0 * dt
                    if (libraryPhase1 > twoPi) libraryPhase1 -= twoPi

                    libraryPhase2 += twoPi * 140.0 * dt
                    if (libraryPhase2 > twoPi) libraryPhase2 -= twoPi

                    val softHum = (sin(libraryPhase1) * 0.6 + sin(libraryPhase2) * 0.4) * 0.3
                    val roomAir = (random.nextDouble() * 2.0 - 1.0) * 0.1
                    mixedSample += (softHum + roomAir) * vLib
                }

                // 5. Şömine Çıtırtısı (Warm low background + sudden snap crackles)
                if (vFire > 0.01f) {
                    val lowRumble = (random.nextDouble() * 2.0 - 1.0) * 0.15
                    // Crackle impulse
                    if (random.nextInt(600) == 0) {
                        crackleDecay = 0.9 + random.nextDouble() * 0.1
                    }
                    crackleDecay *= 0.96
                    val crackleSound = crackleDecay * (random.nextDouble() * 2.0 - 1.0)
                    mixedSample += (lowRumble + crackleSound * 0.8) * vFire
                }

                // Master gain & soft-clipping protection
                val finalOutput = (mixedSample * masterVolume).coerceIn(-1.0, 1.0)
                shortBuffer[i] = (finalOutput * 32767.0).toInt().toShort()
            }

            try {
                audioTrack?.write(shortBuffer, 0, shortBuffer.size)
            } catch (e: Exception) {
                break
            }
        }
    }
}
