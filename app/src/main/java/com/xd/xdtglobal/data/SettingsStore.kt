package com.xd.xdtglobal.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SettingsStore(context: Context) {
    private val prefs = context.applicationContext
        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var musicEnabled by mutableStateOf(prefs.getBoolean(KEY_MUSIC, true))
        private set

    var winLoseSoundEnabled by mutableStateOf(prefs.getBoolean(KEY_WIN_LOSE_SOUND, true))
        private set

    var lightningSoundEnabled by mutableStateOf(prefs.getBoolean(KEY_LIGHTNING_SOUND, true))
        private set

    fun setMusic(enabled: Boolean) {
        musicEnabled = enabled
        prefs.edit().putBoolean(KEY_MUSIC, enabled).apply()
    }

    fun setWinLoseSound(enabled: Boolean) {
        winLoseSoundEnabled = enabled
        prefs.edit().putBoolean(KEY_WIN_LOSE_SOUND, enabled).apply()
    }

    fun setLightningSound(enabled: Boolean) {
        lightningSoundEnabled = enabled
        prefs.edit().putBoolean(KEY_LIGHTNING_SOUND, enabled).apply()
    }

    companion object {
        private const val PREFS_NAME = "zlg_settings"
        private const val KEY_MUSIC = "music"
        private const val KEY_WIN_LOSE_SOUND = "win_lose_sound"
        private const val KEY_LIGHTNING_SOUND = "lightning_sound"
    }
}
