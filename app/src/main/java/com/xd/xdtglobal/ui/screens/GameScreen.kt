package com.xd.xdtglobal.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.xd.xdtglobal.LocalAppContainer
import com.xd.xdtglobal.game.GameConfig
import com.xd.xdtglobal.game.GameStatus
import com.xd.xdtglobal.game.PieceType
import com.xd.xdtglobal.game.Pieces
import com.xd.xdtglobal.game.TetrisGame
import com.xd.xdtglobal.ui.components.GemstoneCell
import com.xd.xdtglobal.ui.components.OlympusBackdrop
import com.xd.xdtglobal.ui.components.ScreenTitle
import com.xd.xdtglobal.ui.components.ZeusButton
import com.xd.xdtglobal.ui.components.ZeusButtonStyle
import com.xd.xdtglobal.ui.components.drawGemstone
import com.xd.xdtglobal.ui.components.pressableWithCooldown
import com.xd.xdtglobal.ui.theme.ElectricBlue
import com.xd.xdtglobal.ui.theme.ElectricCyan
import com.xd.xdtglobal.ui.theme.LightningWhite
import com.xd.xdtglobal.ui.theme.OlympusDeep
import com.xd.xdtglobal.ui.theme.OlympusMid
import com.xd.xdtglobal.ui.theme.OlympusNight
import com.xd.xdtglobal.ui.theme.OlympusViolet
import com.xd.xdtglobal.ui.theme.ZeusGold
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

private val ZEUS_PHRASES = listOf(
    "Mighty strike!",
    "Olympus approves!",
    "Thunder combo!",
    "You command the storm!",
    "Divine move!"
)

@Composable
fun GameScreen(
    level: Int,
    onExitToMenu: () -> Unit,
    onRoundEnd: (level: Int, score: Int, won: Boolean) -> Unit
) {
    val container = LocalAppContainer.current

    val game = remember(level) { TetrisGame(level) }
    val state = game.state

    var lastClearToken by remember { mutableStateOf(0L) }
    var clearFlash by remember { mutableFloatStateOf(0f) }
    var zeusPhrase by remember { mutableStateOf<String?>(null) }
    var zeusPhraseUntil by remember { mutableStateOf(0L) }
    var lastScoreToken by remember { mutableStateOf(0L) }
    var scorePopupValue by remember { mutableIntStateOf(0) }
    var scorePopupUntil by remember { mutableStateOf(0L) }
    var nowMs by remember { mutableStateOf(System.currentTimeMillis()) }
    var roundEndedHandled by remember(level) { mutableStateOf(false) }
    var isExitingScreen by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE && state.status != GameStatus.WON && state.status != GameStatus.LOST && !isExitingScreen)
                game.pause()
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    BackHandler(enabled = true) {
        isExitingScreen = true
        onExitToMenu()
    }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { now ->
                nowMs = now
                clearFlash = (clearFlash - 0.06f).coerceAtLeast(0f)
            }
        }
    }

    val event = state.clearEvent
    LaunchedEffect(event?.token) {
        if (event != null && event.token != lastClearToken) {
            lastClearToken = event.token
            clearFlash = 1f
            container.audio.playLineClear()
            if (event.combo >= 2) {
                zeusPhrase = ZEUS_PHRASES.random()
                zeusPhraseUntil = System.currentTimeMillis() + 1800
            }
        }
    }

    LaunchedEffect(state.lastScoreToken) {
        if (state.lastScoreToken != 0L && state.lastScoreToken != lastScoreToken) {
            lastScoreToken = state.lastScoreToken
            scorePopupValue = state.lastScoreGain
            scorePopupUntil = System.currentTimeMillis() + 1100
        }
    }

    LaunchedEffect(level, state.status) {
        if (state.status == GameStatus.PLAYING) {
            while (state.status == GameStatus.PLAYING) {
                kotlinx.coroutines.delay(GameConfig.dropIntervalMillis(level))
                if (game.state.status == GameStatus.PLAYING) {
                    game.tick()
                }
            }
        }
    }

    LaunchedEffect(state.status) {
        if (!roundEndedHandled && (state.status == GameStatus.WON || state.status == GameStatus.LOST)) {
            roundEndedHandled = true
            val won = state.status == GameStatus.WON
            container.progress.recordRoundResult(level, state.score, won)
            if (won) container.audio.playWin() else container.audio.playLose()
            kotlinx.coroutines.delay(900)
            onRoundEnd(level, state.score, won)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            // ensure we don't keep ticking in background
        }
    }

    val flashIntensity by remember {
        derivedStateOf { clearFlash }
    }

    OlympusBackdrop(showColumns = true, flashIntensity = flashIntensity) {
        Column(modifier = Modifier.fillMaxSize().padding(12.dp).padding(top = 32.dp)) {
            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ScoreCard(score = state.score, target = state.targetScore, level = level)
                PauseButton(onClick = { game.pause() })
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Board area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                BoxWithConstraints(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val maxW = maxWidth
                    val maxH = maxHeight
                    val cellWidth = maxW / GameConfig.COLS
                    val maxCellByHeight = maxH / GameConfig.ROWS
                    val cell: Dp = if (cellWidth < maxCellByHeight) cellWidth else maxCellByHeight
                    val boardWidth = cell * GameConfig.COLS
                    val boardHeight = cell * GameConfig.ROWS

                    Box(
                        modifier = Modifier
                            .width(boardWidth)
                            .height(boardHeight)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        OlympusNight.copy(alpha = 0.85f),
                                        OlympusDeep.copy(alpha = 0.80f),
                                        OlympusViolet.copy(alpha = 0.75f)
                                    )
                                )
                            )
                            .border(2.dp, ZeusGold.copy(alpha = 0.55f), RoundedCornerShape(14.dp))
                    ) {
                        BoardCanvas(
                            game = game,
                            flashRows = state.clearEvent?.takeIf {
                                System.currentTimeMillis() - it.token < 220
                            }?.rows ?: emptyList(),
                            modifier = Modifier.fillMaxSize()
                        )

                        // Zeus character bottom-right
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 6.dp, bottom = 6.dp)
                                .size(78.dp)
                        ) {
                            ZeusCharacter(
                                phrase = zeusPhrase,
                                visible = nowMs < zeusPhraseUntil,
                                onPhraseEnded = { zeusPhrase = null }
                            )
                        }

                        // Score popup top-center of board
                        ScorePopup(
                            value = scorePopupValue,
                            visible = nowMs < scorePopupUntil,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom controls
            BottomControls(
                onLeft = { game.moveLeft() },
                onRotate = { game.rotate() },
                onRight = { game.moveRight() },
                enabled = state.status == GameStatus.PLAYING
            )
        }

        // Pause overlay
        if (state.status == GameStatus.PAUSED) {
            PauseOverlay(
                onResume = { game.resume() },
                onMenu = {
                    isExitingScreen = true
                    onExitToMenu()
                }
            )
        }
    }
}

@Composable
private fun ScoreCard(score: Int, target: Int, level: Int) {
    Row(
        modifier = Modifier
            .background(
                Brush.horizontalGradient(listOf(OlympusViolet, OlympusMid, OlympusDeep)),
                RoundedCornerShape(percent = 50)
            )
            .border(2.dp, ZeusGold.copy(alpha = 0.6f), RoundedCornerShape(percent = 50))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "LV $level",
                style = MaterialTheme.typography.labelMedium,
                color = ElectricCyan
            )
            Text(
                text = "$score / $target",
                style = MaterialTheme.typography.titleMedium,
                color = ZeusGold
            )
        }
    }
}

@Composable
private fun PauseButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .pressableWithCooldown(cooldownMillis = 600L, onClick = onClick)
            .size(56.dp)
            .background(
                Brush.radialGradient(listOf(OlympusViolet, OlympusDeep)),
                RoundedCornerShape(percent = 50)
            )
            .border(2.dp, ZeusGold, RoundedCornerShape(percent = 50)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 6.dp, height = 22.dp)
                    .background(ZeusGold, RoundedCornerShape(2.dp))
            )
            Box(
                modifier = Modifier
                    .size(width = 6.dp, height = 22.dp)
                    .background(ZeusGold, RoundedCornerShape(2.dp))
            )
        }
    }
}

@Composable
private fun BoardCanvas(
    game: TetrisGame,
    flashRows: List<Int>,
    modifier: Modifier = Modifier
) {
    val state = game.state

    Canvas(modifier = modifier) {
        val cellW = size.width / GameConfig.COLS
        val cellH = size.height / GameConfig.ROWS
        val cellSize = if (cellW < cellH) cellW else cellH
        val xPad = (size.width - cellSize * GameConfig.COLS) / 2f
        val yPad = (size.height - cellSize * GameConfig.ROWS) / 2f

        // Subtle column hints (temple feel)
        for (col in 0 until GameConfig.COLS) {
            if (col % 3 == 0) {
                drawRect(
                    color = LightningWhite.copy(alpha = 0.025f),
                    topLeft = Offset(xPad + col * cellSize, yPad),
                    size = Size(cellSize, cellSize * GameConfig.ROWS)
                )
            }
        }

        // Glow grid
        val gridColor = ElectricBlue.copy(alpha = 0.18f)
        for (col in 0..GameConfig.COLS) {
            drawLine(
                color = gridColor,
                start = Offset(xPad + col * cellSize, yPad),
                end = Offset(xPad + col * cellSize, yPad + cellSize * GameConfig.ROWS),
                strokeWidth = 1f
            )
        }
        for (row in 0..GameConfig.ROWS) {
            drawLine(
                color = gridColor,
                start = Offset(xPad, yPad + row * cellSize),
                end = Offset(xPad + cellSize * GameConfig.COLS, yPad + row * cellSize),
                strokeWidth = 1f
            )
        }

        // Flash row highlight
        for (row in flashRows) {
            drawRect(
                color = LightningWhite.copy(alpha = 0.45f),
                topLeft = Offset(xPad, yPad + row * cellSize),
                size = Size(cellSize * GameConfig.COLS, cellSize)
            )
        }

        // Locked cells
        for (r in state.board.indices) {
            for (c in state.board[r].indices) {
                val type = state.board[r][c] ?: continue
                drawCellGem(
                    type = type,
                    x = xPad + c * cellSize,
                    y = yPad + r * cellSize,
                    size = cellSize
                )
            }
        }

        // Active piece
        val piece = state.activePiece
        if (piece != null) {
            // Ghost preview
            val ghost = computeGhost(state.board, piece)
            for ((r, c) in Pieces.shape(piece.type, piece.rotation).map { (rr, cc) ->
                ghost.row + rr to ghost.col + cc
            }) {
                if (r < 0) continue
                drawRect(
                    color = piece.type.core.copy(alpha = 0.18f),
                    topLeft = Offset(xPad + c * cellSize + cellSize * 0.12f, yPad + r * cellSize + cellSize * 0.12f),
                    size = Size(cellSize * 0.76f, cellSize * 0.76f)
                )
            }

            for ((r, c) in Pieces.cellsOf(piece)) {
                if (r < 0) continue
                drawCellGem(
                    type = piece.type,
                    x = xPad + c * cellSize,
                    y = yPad + r * cellSize,
                    size = cellSize
                )
            }
        }

        // Sparks along flash rows
        if (flashRows.isNotEmpty()) {
            val rng = Random(System.currentTimeMillis() / 50)
            for (row in flashRows) {
                repeat(20) {
                    val sx = xPad + rng.nextFloat() * cellSize * GameConfig.COLS
                    val sy = yPad + (row + rng.nextFloat()) * cellSize
                    drawCircle(
                        color = LightningWhite.copy(alpha = 0.8f),
                        radius = 2f + rng.nextFloat() * 3f,
                        center = Offset(sx, sy)
                    )
                }
            }
        }
    }
}

private fun computeGhost(board: List<List<PieceType?>>, piece: com.xd.xdtglobal.game.ActivePiece): com.xd.xdtglobal.game.ActivePiece {
    var ghost = piece
    while (true) {
        val candidate = ghost.copy(row = ghost.row + 1)
        val ok = Pieces.cellsOf(candidate).all { (r, c) ->
            c in 0 until GameConfig.COLS &&
                r < GameConfig.ROWS &&
                (r < 0 || board[r][c] == null)
        }
        if (!ok) return ghost
        ghost = candidate
    }
}

private fun DrawScope.drawCellGem(type: PieceType, x: Float, y: Float, size: Float) {
    drawGem(type, x, y, size)
}

private fun DrawScope.drawGem(type: PieceType, x: Float, y: Float, side: Float) {
    val pad = side * 0.08f
    val rect = androidx.compose.ui.geometry.Rect(
        Offset(x + pad, y + pad),
        Size(side - pad * 2, side - pad * 2)
    )
    drawRoundRect(
        brush = Brush.linearGradient(
            colors = listOf(
                lighten(type.core, 0.20f),
                type.core,
                darken(type.core, 0.30f)
            ),
            start = Offset(rect.left, rect.top),
            end = Offset(rect.right, rect.bottom)
        ),
        topLeft = Offset(rect.left, rect.top),
        size = rect.size,
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(side * 0.18f, side * 0.18f)
    )
    val inset = side * 0.18f
    val facetTop = Path().apply {
        moveTo(rect.left + inset, rect.top + inset)
        lineTo(rect.right - inset, rect.top + inset)
        lineTo(rect.center.x, rect.center.y)
        close()
    }
    drawPath(
        path = facetTop,
        brush = Brush.verticalGradient(
            colors = listOf(type.highlight.copy(alpha = 0.85f), Color.Transparent),
            startY = rect.top,
            endY = rect.center.y
        )
    )
    val facetSide = Path().apply {
        moveTo(rect.left + inset, rect.top + inset)
        lineTo(rect.left + inset, rect.bottom - inset)
        lineTo(rect.center.x, rect.center.y)
        close()
    }
    drawPath(
        path = facetSide,
        brush = Brush.linearGradient(
            colors = listOf(lighten(type.core, 0.10f), darken(type.core, 0.15f)),
            start = Offset(rect.left, rect.center.y),
            end = Offset(rect.center.x, rect.center.y)
        )
    )
    drawCircle(
        color = type.highlight.copy(alpha = 0.85f),
        radius = side * 0.07f,
        center = Offset(rect.left + inset * 1.05f, rect.top + inset * 0.85f)
    )
    drawRoundRect(
        color = type.rim,
        topLeft = Offset(rect.left, rect.top),
        size = rect.size,
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(side * 0.18f, side * 0.18f),
        style = Stroke(width = side * 0.05f)
    )
}

private fun lighten(c: Color, amount: Float) = Color(
    red = (c.red + (1f - c.red) * amount).coerceIn(0f, 1f),
    green = (c.green + (1f - c.green) * amount).coerceIn(0f, 1f),
    blue = (c.blue + (1f - c.blue) * amount).coerceIn(0f, 1f),
    alpha = c.alpha
)

private fun darken(c: Color, amount: Float) = Color(
    red = (c.red * (1f - amount)).coerceIn(0f, 1f),
    green = (c.green * (1f - amount)).coerceIn(0f, 1f),
    blue = (c.blue * (1f - amount)).coerceIn(0f, 1f),
    alpha = c.alpha
)

@Composable
private fun BottomControls(
    onLeft: () -> Unit,
    onRotate: () -> Unit,
    onRight: () -> Unit,
    enabled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ControlButton(
            label = "◀",
            cooldown = 130L,
            enabled = enabled,
            onClick = onLeft
        )
        ControlButton(
            label = "↻",
            cooldown = 170L,
            enabled = enabled,
            onClick = onRotate
        )
        ControlButton(
            label = "▶",
            cooldown = 130L,
            enabled = enabled,
            onClick = onRight
        )
    }
}

@Composable
private fun ControlButton(label: String, cooldown: Long, enabled: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .pressableWithCooldown(cooldownMillis = cooldown, enabled = enabled, onClick = onClick)
            .size(72.dp)
            .background(
                Brush.radialGradient(listOf(OlympusViolet, OlympusDeep)),
                RoundedCornerShape(percent = 50)
            )
            .border(2.dp, ZeusGold.copy(alpha = if (enabled) 1f else 0.4f), RoundedCornerShape(percent = 50)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineMedium,
            color = if (enabled) ZeusGold else ZeusGold.copy(alpha = 0.4f)
        )
    }
}

@Composable
private fun PauseOverlay(onResume: () -> Unit, onMenu: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures { /* consume taps */ } }
            .background(OlympusNight.copy(alpha = 0.78f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .background(
                    Brush.verticalGradient(listOf(OlympusViolet, OlympusMid, OlympusDeep)),
                    RoundedCornerShape(22.dp)
                )
                .border(2.dp, ZeusGold, RoundedCornerShape(22.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenTitle(text = "PAUSED")
            Spacer(modifier = Modifier.height(16.dp))
            ZeusButton(
                text = "RESUME",
                style = ZeusButtonStyle.Primary,
                modifier = Modifier.fillMaxWidth(),
                cooldownMillis = 800L,
                onClick = onResume
            )
            Spacer(modifier = Modifier.height(12.dp))
            ZeusButton(
                text = "MENU",
                style = ZeusButtonStyle.Stone,
                modifier = Modifier.fillMaxWidth(),
                cooldownMillis = 900L,
                onClick = onMenu
            )
        }
    }
}

@Composable
private fun ZeusCharacter(
    phrase: String?,
    visible: Boolean,
    onPhraseEnded: () -> Unit
) {
    val infinite = rememberInfiniteTransition(label = "zeusChar")
    val bob by infinite.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bob"
    )
    val glow by animateFloatAsState(
        targetValue = if (visible) 1f else 0.4f,
        animationSpec = tween(400),
        label = "glow"
    )

    LaunchedEffect(visible) {
        if (!visible) onPhraseEnded()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Speech bubble above the character
        AnimatedVisibility(
            visible = visible && phrase != null,
            enter = fadeIn() + scaleIn(initialScale = 0.5f),
            exit = fadeOut() + scaleOut(targetScale = 0.5f),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .background(
                        Brush.linearGradient(listOf(LightningWhite, ElectricCyan)),
                        RoundedCornerShape(12.dp)
                    )
                    .border(2.dp, ZeusGold, RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = phrase ?: "",
                    style = MaterialTheme.typography.labelMedium,
                    color = OlympusNight,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Character
        Canvas(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.BottomCenter)
        ) {
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h / 2f + bob

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ZeusGold.copy(alpha = 0.6f * glow),
                        ElectricCyan.copy(alpha = 0.25f * glow),
                        Color.Transparent
                    )
                ),
                radius = w * 0.65f,
                center = Offset(cx, cy)
            )

            // Body
            drawRoundRect(
                color = LightningWhite.copy(alpha = 0.95f),
                topLeft = Offset(cx - w * 0.18f, cy - h * 0.08f),
                size = Size(w * 0.36f, h * 0.34f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
            )

            // Head
            drawCircle(
                color = LightningWhite,
                radius = w * 0.16f,
                center = Offset(cx, cy - h * 0.18f)
            )

            // Beard
            drawCircle(
                color = Color(0xFFB8C4D6),
                radius = w * 0.10f,
                center = Offset(cx, cy - h * 0.10f)
            )

            // Tiny bolt
            val bolt = Path().apply {
                moveTo(cx + w * 0.20f, cy - h * 0.08f)
                lineTo(cx + w * 0.32f, cy - h * 0.20f)
                lineTo(cx + w * 0.26f, cy - h * 0.18f)
                lineTo(cx + w * 0.34f, cy - h * 0.30f)
                lineTo(cx + w * 0.22f, cy - h * 0.18f)
                lineTo(cx + w * 0.28f, cy - h * 0.16f)
                close()
            }
            drawPath(
                path = bolt,
                brush = Brush.linearGradient(listOf(ZeusGold, LightningWhite))
            )
        }
    }
}

@Composable
private fun ScorePopup(value: Int, visible: Boolean, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(initialScale = 0.6f),
        exit = fadeOut() + scaleOut(targetScale = 1.4f),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(listOf(ZeusGold, LightningWhite)),
                    RoundedCornerShape(12.dp)
                )
                .border(2.dp, ZeusGold, RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text(
                text = "+$value",
                style = MaterialTheme.typography.titleLarge,
                color = OlympusNight
            )
        }
    }
}

@Suppress("unused")
private fun unusedSinHelper(t: Float): Float = sin(t * 2 * PI.toFloat())

@Suppress("unused")
@Composable
private fun unusedGemPreview() {
    GemstoneCell(type = PieceType.O, size = 24.dp)
}
