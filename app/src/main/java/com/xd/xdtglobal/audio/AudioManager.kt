package com.xd.xdtglobal.audio

import all.documentreader.filereader.office.vie.R
import android.content.Context
import android.media.MediaPlayer
import com.xd.xdtglobal.data.SettingsStore

class AudioManager(
    context: Context,
    private val settings: SettingsStore
) {
    private val appContext = context.applicationContext
    private var music: MediaPlayer? = null
    private var manuallyPaused = false

    fun startMusicIfEnabled() {
        if (!settings.musicEnabled) return
        if (music != null) {
            resumeMusic()
            return
        }
        music = runCatching {
            MediaPlayer.create(appContext, R.raw.game_music)?.apply {
                isLooping = true
                setVolume(MUSIC_VOLUME, MUSIC_VOLUME)
                start()
            }
        }.getOrNull()
        manuallyPaused = false
    }

    fun pauseMusic() {
        music?.takeIf { it.isPlaying }?.pause()
        manuallyPaused = true
    }

    fun resumeMusic() {
        if (!settings.musicEnabled) return
        val player = music ?: run { startMusicIfEnabled(); return }
        if (!player.isPlaying) {
            runCatching { player.start() }
        }
        manuallyPaused = false
    }

    fun stopMusic() {
        music?.runCatching {
            if (isPlaying) stop()
            release()
        }
        music = null
        manuallyPaused = false
    }

    fun applyMusicSetting() {
        if (settings.musicEnabled) {
            startMusicIfEnabled()
        } else {
            stopMusic()
        }
    }

    fun playLineClear() {
        if (settings.lightningSoundEnabled) playOneShot(R.raw.lightning, 0.9f)
    }

    fun playWin() {
        if (settings.winLoseSoundEnabled) playOneShot(R.raw.level_win, 1.0f)
    }

    fun playLose() {
        if (settings.winLoseSoundEnabled) playOneShot(R.raw.level_lose, 1.0f)
    }

    private fun playOneShot(resId: Int, volume: Float) {
        runCatching {
            MediaPlayer.create(appContext, resId)?.apply {
                setVolume(volume, volume)
                setOnCompletionListener { player ->
                    runCatching { player.release() }
                }
                start()
            }
        }
    }

    fun release() {
        stopMusic()
    }

    companion object {
        private const val MUSIC_VOLUME = 0.45f
    }
}
