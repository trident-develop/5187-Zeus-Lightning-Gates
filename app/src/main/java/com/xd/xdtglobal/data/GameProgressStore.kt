package com.xd.xdtglobal.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import com.xd.xdtglobal.game.GameConfig

class GameProgressStore(context: Context) {
    private val prefs = context.applicationContext
        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var highestUnlockedLevel by mutableIntStateOf(
        prefs.getInt(KEY_UNLOCKED, 1).coerceIn(1, GameConfig.LEVEL_COUNT)
    )
        private set

    private val bestScores = mutableStateMapOf<Int, Int>().apply {
        for (level in 1..GameConfig.LEVEL_COUNT) {
            val saved = prefs.getInt(bestScoreKey(level), 0)
            if (saved > 0) put(level, saved)
        }
    }

    fun bestScoreFor(level: Int): Int = bestScores[level] ?: 0

    fun isLevelUnlocked(level: Int): Boolean = level in 1..highestUnlockedLevel

    fun isLevelCompleted(level: Int): Boolean = bestScores[level]?.let { it > 0 } == true

    fun recordRoundResult(level: Int, score: Int, won: Boolean) {
        val current = bestScores[level] ?: 0
        if (score > current) {
            bestScores[level] = score
            prefs.edit().putInt(bestScoreKey(level), score).apply()
        }
        if (won && level == highestUnlockedLevel && level < GameConfig.LEVEL_COUNT) {
            highestUnlockedLevel = level + 1
            prefs.edit().putInt(KEY_UNLOCKED, highestUnlockedLevel).apply()
        }
    }

    private fun bestScoreKey(level: Int) = "best_$level"

    companion object {
        private const val PREFS_NAME = "zlg_progress"
        private const val KEY_UNLOCKED = "highest_unlocked"
    }
}
