package com.xd.xdtglobal

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import com.xd.xdtglobal.audio.AudioManager
import com.xd.xdtglobal.data.GameProgressStore
import com.xd.xdtglobal.data.SettingsStore

class AppContainer(context: Context) {
    val settings = SettingsStore(context)
    val progress = GameProgressStore(context)
    val audio = AudioManager(context, settings)
}

val LocalAppContainer = compositionLocalOf<AppContainer> {
    error("AppContainer not provided")
}
