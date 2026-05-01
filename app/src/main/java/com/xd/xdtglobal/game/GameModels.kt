package com.xd.xdtglobal.game

import androidx.compose.ui.graphics.Color
import com.xd.xdtglobal.ui.theme.GemAmethyst
import com.xd.xdtglobal.ui.theme.GemCitrine
import com.xd.xdtglobal.ui.theme.GemDiamond
import com.xd.xdtglobal.ui.theme.GemEmerald
import com.xd.xdtglobal.ui.theme.GemRuby
import com.xd.xdtglobal.ui.theme.GemSapphire
import com.xd.xdtglobal.ui.theme.GemTopaz

enum class PieceType(
    val gemName: String,
    val core: Color,
    val highlight: Color,
    val rim: Color
) {
    I("Sapphire", GemSapphire, Color(0xFFB7D4FF), Color(0xFF1E3A8A)),
    O("Ruby", GemRuby, Color(0xFFFFB1BD), Color(0xFF7A1027)),
    T("Amethyst", GemAmethyst, Color(0xFFE7C0FF), Color(0xFF4F1F86)),
    L("Emerald", GemEmerald, Color(0xFFB7F5D7), Color(0xFF105B3F)),
    J("Citrine", GemCitrine, Color(0xFFFFF1A1), Color(0xFF8A6A0B)),
    S("Topaz", GemTopaz, Color(0xFFFFD8A8), Color(0xFF8A4A0F)),
    Z("Diamond", GemDiamond, Color(0xFFFFFFFF), Color(0xFF6F8FA8))
}

data class ActivePiece(
    val type: PieceType,
    val rotation: Int,
    val row: Int,
    val col: Int
)

data class ClearEvent(
    val rows: List<Int>,
    val combo: Int,
    val token: Long
)

enum class GameStatus { PLAYING, PAUSED, WON, LOST }

data class GameState(
    val board: List<List<PieceType?>>,
    val activePiece: ActivePiece?,
    val nextPiece: PieceType,
    val score: Int,
    val targetScore: Int,
    val level: Int,
    val status: GameStatus,
    val clearEvent: ClearEvent?,
    val lastScoreGain: Int,
    val lastScoreToken: Long
) {
    companion object {
        fun emptyBoard(): List<List<PieceType?>> =
            List(GameConfig.ROWS) { List(GameConfig.COLS) { null } }
    }
}
