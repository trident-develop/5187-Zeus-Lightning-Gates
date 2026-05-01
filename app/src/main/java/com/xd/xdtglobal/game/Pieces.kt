package com.xd.xdtglobal.game

private typealias Shape = List<Pair<Int, Int>>

object Pieces {

    private val I_ROTATIONS: List<Shape> = listOf(
        listOf(1 to 0, 1 to 1, 1 to 2, 1 to 3),
        listOf(0 to 2, 1 to 2, 2 to 2, 3 to 2),
        listOf(2 to 0, 2 to 1, 2 to 2, 2 to 3),
        listOf(0 to 1, 1 to 1, 2 to 1, 3 to 1)
    )

    private val O_ROTATIONS: List<Shape> = List(4) {
        listOf(0 to 1, 0 to 2, 1 to 1, 1 to 2)
    }

    private val T_ROTATIONS: List<Shape> = listOf(
        listOf(0 to 1, 1 to 0, 1 to 1, 1 to 2),
        listOf(0 to 1, 1 to 1, 1 to 2, 2 to 1),
        listOf(1 to 0, 1 to 1, 1 to 2, 2 to 1),
        listOf(0 to 1, 1 to 0, 1 to 1, 2 to 1)
    )

    private val L_ROTATIONS: List<Shape> = listOf(
        listOf(0 to 2, 1 to 0, 1 to 1, 1 to 2),
        listOf(0 to 1, 1 to 1, 2 to 1, 2 to 2),
        listOf(1 to 0, 1 to 1, 1 to 2, 2 to 0),
        listOf(0 to 0, 0 to 1, 1 to 1, 2 to 1)
    )

    private val J_ROTATIONS: List<Shape> = listOf(
        listOf(0 to 0, 1 to 0, 1 to 1, 1 to 2),
        listOf(0 to 1, 0 to 2, 1 to 1, 2 to 1),
        listOf(1 to 0, 1 to 1, 1 to 2, 2 to 2),
        listOf(0 to 1, 1 to 1, 2 to 0, 2 to 1)
    )

    private val S_ROTATIONS: List<Shape> = listOf(
        listOf(0 to 1, 0 to 2, 1 to 0, 1 to 1),
        listOf(0 to 1, 1 to 1, 1 to 2, 2 to 2),
        listOf(1 to 1, 1 to 2, 2 to 0, 2 to 1),
        listOf(0 to 0, 1 to 0, 1 to 1, 2 to 1)
    )

    private val Z_ROTATIONS: List<Shape> = listOf(
        listOf(0 to 0, 0 to 1, 1 to 1, 1 to 2),
        listOf(0 to 2, 1 to 1, 1 to 2, 2 to 1),
        listOf(1 to 0, 1 to 1, 2 to 1, 2 to 2),
        listOf(0 to 1, 1 to 0, 1 to 1, 2 to 0)
    )

    fun shape(type: PieceType, rotation: Int): Shape {
        val rotations = when (type) {
            PieceType.I -> I_ROTATIONS
            PieceType.O -> O_ROTATIONS
            PieceType.T -> T_ROTATIONS
            PieceType.L -> L_ROTATIONS
            PieceType.J -> J_ROTATIONS
            PieceType.S -> S_ROTATIONS
            PieceType.Z -> Z_ROTATIONS
        }
        val r = ((rotation % 4) + 4) % 4
        return rotations[r]
    }

    fun cellsOf(piece: ActivePiece): List<Pair<Int, Int>> =
        shape(piece.type, piece.rotation).map { (r, c) ->
            piece.row + r to piece.col + c
        }

    fun spawnColumn(): Int = (GameConfig.COLS - 4) / 2
}
