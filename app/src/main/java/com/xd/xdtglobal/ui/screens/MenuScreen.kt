package com.xd.xdtglobal.ui.screens

import android.app.Activity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.xd.xdtglobal.game.PieceType
import com.xd.xdtglobal.ui.components.GemstoneCell
import com.xd.xdtglobal.ui.components.OlympusBackdrop
import com.xd.xdtglobal.ui.components.ScreenTitle
import com.xd.xdtglobal.ui.components.ZeusButton
import com.xd.xdtglobal.ui.components.ZeusButtonStyle
import com.xd.xdtglobal.ui.theme.ElectricCyan
import com.xd.xdtglobal.ui.theme.LightningWhite
import com.xd.xdtglobal.ui.theme.ZeusGold
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun MenuScreen(
    onPlay: () -> Unit,
    onLevels: () -> Unit,
    onLeaderboard: () -> Unit,
    onSettings: () -> Unit
) {
    val context = LocalContext.current
    OlympusBackdrop(showColumns = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                AnimatedTitleLightning()
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ScreenTitle(
                        text = "ZEUS",
                        style = MaterialTheme.typography.displayLarge.copy(textAlign = TextAlign.Center)
                    )
                    ScreenTitle(
                        text = "LIGHTNING GATES",
                        style = MaterialTheme.typography.headlineLarge.copy(textAlign = TextAlign.Center)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            FloatingGems()

            Spacer(modifier = Modifier.height(8.dp))

            ZeusStatue()

            Spacer(modifier = Modifier.height(28.dp))

            ZeusButton(
                text = "PLAY",
                style = ZeusButtonStyle.Primary,
                modifier = Modifier.fillMaxWidth(),
                cooldownMillis = 900L,
                onClick = onPlay
            )
            Spacer(modifier = Modifier.height(14.dp))
            ZeusButton(
                text = "LEADERBOARD",
                style = ZeusButtonStyle.Secondary,
                modifier = Modifier.fillMaxWidth(),
                cooldownMillis = 900L,
                onClick = onLeaderboard
            )
            Spacer(modifier = Modifier.height(14.dp))
            ZeusButton(
                text = "SETTINGS",
                style = ZeusButtonStyle.Secondary,
                modifier = Modifier.fillMaxWidth(),
                cooldownMillis = 900L,
                onClick = onSettings
            )
            Spacer(modifier = Modifier.height(14.dp))
            ZeusButton(
                text = "EXIT",
                style = ZeusButtonStyle.Stone,
                modifier = Modifier.fillMaxWidth(),
                cooldownMillis = 900L,
                onClick = {
                    (context as? Activity)?.finishAffinity()
                }
            )
        }
    }
}

@Composable
private fun AnimatedTitleLightning() {
    val infinite = rememberInfiniteTransition(label = "titleLightning")
    val phase by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )
    val seed = remember { Random(99) }
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        val w = size.width
        val h = size.height

        // glow halo
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    ZeusGold.copy(alpha = 0.30f),
                    ElectricCyan.copy(alpha = 0.18f),
                    Color.Transparent
                )
            ),
            radius = w * 0.55f,
            center = Offset(w * 0.5f, h * 0.5f)
        )

        // 3 zigzag bolts
        val bolts = 3
        repeat(bolts) { i ->
            val baseX = w * (0.18f + i * 0.32f)
            val path = Path().apply {
                moveTo(baseX, 0f)
                var x = baseX
                var y = 0f
                while (y < h) {
                    y += h * 0.12f
                    x += (seed.nextFloat() - 0.5f) * w * 0.14f
                    lineTo(x, y)
                }
            }
            val alpha = 0.30f + 0.45f * (0.5f + 0.5f * sin((phase * 2 * PI + i * 1.7).toDouble())).toFloat()
            drawPath(
                path = path,
                color = LightningWhite.copy(alpha = alpha.coerceAtMost(0.85f)),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f)
            )
            drawPath(
                path = path,
                color = ElectricCyan.copy(alpha = alpha * 0.4f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 8f)
            )
        }
    }
}

@Composable
private fun FloatingGems() {
    val infinite = rememberInfiniteTransition(label = "floatingGems")
    val sway by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sway"
    )
    val gems = listOf(PieceType.O, PieceType.T, PieceType.I, PieceType.L, PieceType.S)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        gems.forEachIndexed { i, type ->
            val yOffset = (sin((sway * PI * 2 + i).toDouble()).toFloat() * 6f).dp
            Box(
                modifier = Modifier
                    .offset(y = yOffset)
                    .size(40.dp)
            ) {
                GemstoneCell(type = type, size = 40.dp, glow = 0.4f * sway)
            }
        }
    }
}

@Composable
private fun ZeusStatue() {
    val infinite = rememberInfiniteTransition(label = "zeusStatue")
    val pulse by infinite.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(140.dp)) {
            val w = size.width
            val h = size.height
            val cx = w / 2f

            // Halo
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ZeusGold.copy(alpha = 0.45f * pulse),
                        ElectricCyan.copy(alpha = 0.20f),
                        Color.Transparent
                    )
                ),
                radius = w * 0.75f,
                center = Offset(cx, h * 0.45f)
            )

            // Pedestal
            drawRoundRect(
                color = LightningWhite.copy(alpha = 0.15f),
                topLeft = Offset(cx - w * 0.30f, h * 0.85f),
                size = androidx.compose.ui.geometry.Size(w * 0.60f, h * 0.10f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
            )

            // Body (robe) - pentagon
            val body = Path().apply {
                moveTo(cx - w * 0.20f, h * 0.85f)
                lineTo(cx + w * 0.20f, h * 0.85f)
                lineTo(cx + w * 0.14f, h * 0.55f)
                lineTo(cx - w * 0.14f, h * 0.55f)
                close()
            }
            drawPath(
                path = body,
                brush = Brush.verticalGradient(
                    colors = listOf(LightningWhite.copy(alpha = 0.85f), Color(0xFFB8C4D6))
                )
            )

            // Shoulders
            drawCircle(
                color = LightningWhite.copy(alpha = 0.85f),
                radius = w * 0.10f,
                center = Offset(cx - w * 0.18f, h * 0.55f)
            )
            drawCircle(
                color = LightningWhite.copy(alpha = 0.85f),
                radius = w * 0.10f,
                center = Offset(cx + w * 0.18f, h * 0.55f)
            )

            // Head
            drawCircle(
                color = LightningWhite,
                radius = w * 0.12f,
                center = Offset(cx, h * 0.40f)
            )
            // Beard
            val beard = Path().apply {
                moveTo(cx - w * 0.10f, h * 0.43f)
                lineTo(cx + w * 0.10f, h * 0.43f)
                lineTo(cx + w * 0.06f, h * 0.55f)
                lineTo(cx - w * 0.06f, h * 0.55f)
                close()
            }
            drawPath(beard, color = Color(0xFFB8C4D6))

            // Bolt in hand
            val bolt = Path().apply {
                moveTo(cx + w * 0.30f, h * 0.50f)
                lineTo(cx + w * 0.40f, h * 0.35f)
                lineTo(cx + w * 0.34f, h * 0.34f)
                lineTo(cx + w * 0.42f, h * 0.20f)
                lineTo(cx + w * 0.32f, h * 0.32f)
                lineTo(cx + w * 0.36f, h * 0.34f)
                close()
            }
            drawPath(
                path = bolt,
                brush = Brush.linearGradient(listOf(ZeusGold, LightningWhite))
            )
            drawPath(
                path = bolt,
                color = ZeusGold.copy(alpha = pulse),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f)
            )
        }
    }
}
