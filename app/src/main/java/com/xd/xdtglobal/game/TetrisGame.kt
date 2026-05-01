package com.xd.xdtglobal.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class TetrisGame(level: Int) {

    private val rng = java.util.Random()
    private var bag: MutableList<PieceType> = refillBag()

    var state by mutableStateOf(initialState(level))
        private set

    private fun initialState(level: Int): GameState {
        val first = nextFromBag()
        val active = ActivePiece(first, 0, row = 0, col = Pieces.spawnColumn())
        val board = GameState.emptyBoard()
        return GameState(
            board = board,
            activePiece = active,
            nextPiece = nextFromBag(),
            score = 0,
            targetScore = GameConfig.targetScoreFor(level),
            level = level,
            status = GameStatus.PLAYING,
            clearEvent = null,
            lastScoreGain = 0,
            lastScoreToken = 0L
        )
    }

    fun moveLeft() = tryMove(rowDelta = 0, colDelta = -1)

    fun moveRight() = tryMove(rowDelta = 0, colDelta = 1)

    fun rotate() {
        val s = state
        if (s.status != GameStatus.PLAYING) return
        val piece = s.activePiece ?: return
        val rotated = piece.copy(rotation = (piece.rotation + 1) % 4)
        val kicks = listOf(0 to 0, 0 to -1, 0 to 1, 0 to -2, 0 to 2, -1 to 0)
        for ((dr, dc) in kicks) {
            val candidate = rotated.copy(row = rotated.row + dr, col = rotated.col + dc)
            if (canPlace(s.board, candidate)) {
                state = s.copy(activePiece = candidate)
                return
            }
        }
    }

    fun tick() {
        val s = state
        if (s.status != GameStatus.PLAYING) return
        val piece = s.activePiece ?: return
        val moved = piece.copy(row = piece.row + 1)
        if (canPlace(s.board, moved)) {
            state = s.copy(activePiece = moved)
        } else {
            lockAndAdvance()
        }
    }

    fun pause() {
        if (state.status == GameStatus.PLAYING) {
            state = state.copy(status = GameStatus.PAUSED)
        }
    }

    fun resume() {
        if (state.status == GameStatus.PAUSED) {
            state = state.copy(status = GameStatus.PLAYING)
        }
    }

    private fun tryMove(rowDelta: Int, colDelta: Int): Boolean {
        val s = state
        if (s.status != GameStatus.PLAYING) return false
        val piece = s.activePiece ?: return false
        val candidate = piece.copy(row = piece.row + rowDelta, col = piece.col + colDelta)
        return if (canPlace(s.board, candidate)) {
            state = s.copy(activePiece = candidate)
            true
        } else {
            false
        }
    }

    private fun lockAndAdvance() {
        val s = state
        val piece = s.activePiece ?: return
        val mutable = s.board.map { it.toMutableList() }.toMutableList()
        for ((r, c) in Pieces.cellsOf(piece)) {
            if (r in 0 until GameConfig.ROWS && c in 0 until GameConfig.COLS) {
                mutable[r][c] = piece.type
            }
        }

        val fullRows = mutable.indices.filter { r ->
            mutable[r].all { it != null }
        }

        var newScore = s.score
        var clearEvent: ClearEvent? = null
        var scoreGain = 0
        if (fullRows.isNotEmpty()) {
            scoreGain = when (fullRows.size) {
                1 -> 100
                2 -> 250
                3 -> 500
                4 -> 900
                else -> 0
            }
            newScore += scoreGain
            clearEvent = ClearEvent(
                rows = fullRows,
                combo = fullRows.size,
                token = System.currentTimeMillis()
            )

            val keptRows = mutable.filterIndexed { index, _ -> index !in fullRows }
            val emptyRow = List(GameConfig.COLS) { null as PieceType? }
            val newBoardSrc = MutableList(GameConfig.ROWS) { emptyRow.toMutableList() }
            val offset = GameConfig.ROWS - keptRows.size
            for (i in keptRows.indices) {
                newBoardSrc[offset + i] = keptRows[i]
            }
            for (i in newBoardSrc.indices) {
                mutable[i] = newBoardSrc[i]
            }
        }

        val boardSnapshot = mutable.map { it.toList() }

        if (newScore >= s.targetScore) {
            state = s.copy(
                board = boardSnapshot,
                activePiece = null,
                score = newScore,
                status = GameStatus.WON,
                clearEvent = clearEvent,
                lastScoreGain = scoreGain,
                lastScoreToken = if (scoreGain > 0) System.currentTimeMillis() else s.lastScoreToken
            )
            return
        }

        val nextActive = ActivePiece(
            type = s.nextPiece,
            rotation = 0,
            row = 0,
            col = Pieces.spawnColumn()
        )
        if (!canPlace(boardSnapshot, nextActive)) {
            state = s.copy(
                board = boardSnapshot,
                activePiece = null,
                score = newScore,
                status = GameStatus.LOST,
                clearEvent = clearEvent,
                lastScoreGain = scoreGain,
                lastScoreToken = if (scoreGain > 0) System.currentTimeMillis() else s.lastScoreToken
            )
            return
        }

        state = s.copy(
            board = boardSnapshot,
            activePiece = nextActive,
            nextPiece = nextFromBag(),
            score = newScore,
            clearEvent = clearEvent,
            lastScoreGain = scoreGain,
            lastScoreToken = if (scoreGain > 0) System.currentTimeMillis() else s.lastScoreToken
        )
    }

    private fun canPlace(board: List<List<PieceType?>>, piece: ActivePiece): Boolean {
        for ((r, c) in Pieces.cellsOf(piece)) {
            if (c < 0 || c >= GameConfig.COLS) return false
            if (r >= GameConfig.ROWS) return false
            if (r >= 0 && board[r][c] != null) return false
        }
        return true
    }

    private fun nextFromBag(): PieceType {
        if (bag.isEmpty()) bag = refillBag()
        return bag.removeAt(bag.size - 1)
    }

    private fun refillBag(): MutableList<PieceType> =
        PieceType.values().toMutableList().also { it.shuffle(rng) }
}
