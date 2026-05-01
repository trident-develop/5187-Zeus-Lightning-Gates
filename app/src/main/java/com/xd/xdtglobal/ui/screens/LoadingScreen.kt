package com.xd.xdtglobal.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.xd.xdtglobal.ui.components.OlympusBackdrop
import com.xd.xdtglobal.ui.components.ScreenTitle
import com.xd.xdtglobal.ui.theme.ElectricCyan
import com.xd.xdtglobal.ui.theme.LightningWhite
import com.xd.xdtglobal.ui.theme.ZeusGold
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun LoadingScreen() {
    BackHandler(enabled = true) {}
    OlympusBackdrop {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ScreenTitle(
                text = "ZEUS\nLIGHTNING GATES",
                style = MaterialTheme.typography.displayMedium.copy(textAlign = TextAlign.Center)
            )

            Spacer(modifier = Modifier.height(28.dp))

            ThunderboltIcon(
                modifier = Modifier
                    .size(220.dp)
            )

            Spacer(modifier = Modifier.height(36.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = ZeusGold,
                trackColor = LightningWhite.copy(alpha = 0.18f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Summoning the storm…",
                style = MaterialTheme.typography.titleMedium,
                color = ElectricCyan,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ThunderboltIcon(modifier: Modifier = Modifier) {
    val infinite = rememberInfiniteTransition(label = "thunderbolt")
    val glow by infinite.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    val rotate by infinite.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotate"
    )

    var time by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        var start = 0L
        while (true) {
            withFrameMillis { now ->
                if (start == 0L) start = now
                time = (now - start) / 1000f
            }
        }
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h / 2f

            // Outer glow halo
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ZeusGold.copy(alpha = 0.55f * glow),
                        ElectricCyan.copy(alpha = 0.30f * glow),
                        Color.Transparent
                    )
                ),
                radius = w * 0.55f,
                center = Offset(cx, cy)
            )

            // Sparks orbit
            repeat(12) { i ->
                val angle = (time * 1.6f + i * (PI.toFloat() * 2f / 12f))
                val r = w * (0.32f + 0.05f * sin(angle * 1.8f))
                val sx = cx + cos(angle) * r
                val sy = cy + sin(angle) * r
                drawCircle(
                    color = LightningWhite.copy(alpha = (0.45f + 0.55f * glow) *
                        (0.6f + 0.4f * sin(time * 4f + i).toFloat())),
                    radius = w * 0.012f,
                    center = Offset(sx, sy)
                )
            }

            // Thunderbolt path
            rotate(rotate, pivot = Offset(cx, cy)) {
                val path = Path().apply {
                    moveTo(cx - w * 0.10f, cy - h * 0.36f)
                    lineTo(cx + w * 0.07f, cy - h * 0.06f)
                    lineTo(cx - w * 0.02f, cy - h * 0.04f)
                    lineTo(cx + w * 0.14f, cy + h * 0.34f)
                    lineTo(cx - w * 0.04f, cy + h * 0.04f)
                    lineTo(cx + w * 0.06f, cy + h * 0.02f)
                    close()
                }

                drawPath(
                    path = path,
                    brush = Brush.linearGradient(
                        colors = listOf(LightningWhite, ZeusGold)
                    )
                )
                drawPath(
                    path = path,
                    color = ZeusGold,
                    style = Stroke(width = 4f)
                )
                drawPath(
                    path = path,
                    color = ElectricCyan.copy(alpha = 0.7f * glow),
                    style = Stroke(width = 12f)
                )
            }

            // small random sparks
            val rng = Random(((time * 12).toInt()))
            repeat(6) {
                val sx = cx + (rng.nextFloat() - 0.5f) * w * 0.7f
                val sy = cy + (rng.nextFloat() - 0.5f) * h * 0.7f
                drawCircle(
                    color = LightningWhite.copy(alpha = 0.5f * glow),
                    radius = w * 0.006f,
                    center = Offset(sx, sy)
                )
            }
        }
    }
}
