package com.xd.xdtglobal.navigation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xd.xdtglobal.LoadingActivity
import com.xd.xdtglobal.MainActivity
import com.xd.xdtglobal.event.StartSideEffect
import com.xd.xdtglobal.ui.screens.ConnectScreen
import com.xd.xdtglobal.ui.screens.LoadingScreen
import com.xd.xdtglobal.ui.screens.isEgyptConnected
import com.xd.xdtglobal.ui.screens.privacy.TV3
import com.xd.xdtglobal.viewmodel.StartViewModel
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@SuppressLint("ContextCastToActivity")
@Composable
fun LoadingGraph(TV3: TV3) {

    val navController = rememberNavController()
    val context = LocalContext.current as LoadingActivity

    NavHost(
        navController = navController,
        startDestination = if (context.isEgyptConnected()) Routes.LOADING else Routes.CONNECT
    ) {
        composable(Routes.LOADING) {

            val viewModel: StartViewModel = koinViewModel()
            val state by viewModel.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.start()
            }

            LaunchedEffect(TV3) {
                viewModel.observeTVEvents(TV3)
            }

            viewModel.collectSideEffect { sideEffect ->
                when (sideEffect) {
                    is StartSideEffect.OpenBuiltScore -> {
                        TV3.loadUrl(sideEffect.score)
                    }

                    is StartSideEffect.OpenTypeA -> {
                        TV3.loadUrl(sideEffect.score)
                    }

                    is StartSideEffect.OpenTypeB -> {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        context.finish()
                    }

                    StartSideEffect.OpenGame -> {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        context.finish()
                    }
                }
            }
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize())
            }

            LoadingScreen()
        }

        composable(Routes.CONNECT) {
            ConnectScreen(navController)
        }
    }
}