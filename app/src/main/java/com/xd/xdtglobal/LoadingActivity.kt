package com.xd.xdtglobal

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.xd.xdtglobal.data.GameRepo
import com.xd.xdtglobal.navigation.LoadingGraph
import com.xd.xdtglobal.ui.screens.privacy.TV3
import org.koin.android.ext.android.inject
import kotlin.getValue

class LoadingActivity : ComponentActivity() {
    lateinit var TV3: TV3
    private val gameRepo: GameRepo by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        hideSystemBars()
        TV3 = TV3(this, gameRepo)
        TV3.updateIntent(intent)
        setContent {
            LoadingGraph(TV3)
        }
    }

    private fun hideSystemBars() {
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller.hide(WindowInsetsCompat.Type.systemBars())
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemBars()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)

        if (::TV3.isInitialized) {
            TV3.updateIntent(intent)
        }
    }

    override fun onDestroy() {
        if (::TV3.isInitialized) {
            TV3.destroy()
        }
        super.onDestroy()
    }
}
