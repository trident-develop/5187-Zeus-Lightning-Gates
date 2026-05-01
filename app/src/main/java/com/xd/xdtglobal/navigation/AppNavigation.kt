package com.xd.xdtglobal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.xd.xdtglobal.ui.screens.GameScreen
import com.xd.xdtglobal.ui.screens.HowToPlayScreen
import com.xd.xdtglobal.ui.screens.LeaderboardScreen
import com.xd.xdtglobal.ui.screens.LevelsScreen
import com.xd.xdtglobal.ui.screens.MenuScreen
import com.xd.xdtglobal.ui.screens.PrivacyPolicyScreen
import com.xd.xdtglobal.ui.screens.RoundResultScreen
import com.xd.xdtglobal.ui.screens.SettingsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.MENU
    ) {
        composable(Routes.MENU) {
            MenuScreen(
                onPlay = { navController.navigate(Routes.LEVELS) },
                onLevels = { navController.navigate(Routes.LEVELS) },
                onLeaderboard = { navController.navigate(Routes.LEADERBOARD) },
                onSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }
        composable(Routes.LEVELS) {
            LevelsScreen(
                onLevelSelected = { level ->
                    navController.navigate(Routes.game(level))
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.GAME,
            arguments = listOf(navArgument("level") { type = NavType.IntType })
        ) { entry ->
            val level = entry.arguments?.getInt("level") ?: 1
            GameScreen(
                level = level,
                onExitToMenu = {
                    navController.popBackStack(Routes.MENU, inclusive = false)
                },
                onRoundEnd = { l, score, won ->
                    navController.navigate(Routes.roundResult(l, score, won)) {
                        popUpTo(Routes.LEVELS) { inclusive = false }
                    }
                }
            )
        }
        composable(
            route = Routes.ROUND_RESULT,
            arguments = listOf(
                navArgument("level") { type = NavType.IntType },
                navArgument("score") { type = NavType.IntType },
                navArgument("won") { type = NavType.BoolType }
            )
        ) { entry ->
            val level = entry.arguments?.getInt("level") ?: 1
            val score = entry.arguments?.getInt("score") ?: 0
            val won = entry.arguments?.getBoolean("won") ?: false
            RoundResultScreen(
                level = level,
                score = score,
                won = won,
                onRetry = {
                    navController.navigate(Routes.game(level)) {
                        popUpTo(Routes.LEVELS) { inclusive = false }
                    }
                },
                onLevels = {
                    navController.popBackStack(Routes.LEVELS, inclusive = false)
                },
                onMenu = {
                    navController.popBackStack(Routes.MENU, inclusive = false)
                }
            )
        }
        composable(Routes.LEADERBOARD) {
            LeaderboardScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onHowToPlay = { navController.navigate(Routes.HOW_TO_PLAY) },
                onPrivacy = { navController.navigate(Routes.PRIVACY) }
            )
        }
        composable(Routes.HOW_TO_PLAY) {
            HowToPlayScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.PRIVACY) {
            PrivacyPolicyScreen(onBack = { navController.popBackStack() })
        }
    }
}
