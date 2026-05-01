package com.xd.xdtglobal.game

object GameConfig {
    const val LEVEL_COUNT = 36
    const val ROWS = 20
    const val COLS = 10

    fun targetScoreFor(level: Int): Int = 300 + (level - 1) * 150

    fun dropIntervalMillis(level: Int): Long {
        val base = 820L - (level - 1) * 18L
        return base.coerceAtLeast(190L)
    }
}
