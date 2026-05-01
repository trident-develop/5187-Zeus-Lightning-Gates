package com.xd.xdtglobal.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.xd.xdtglobal.R
import com.xd.xdtglobal.ui.theme.ElectricBlue
import com.xd.xdtglobal.ui.theme.ElectricCyan
import com.xd.xdtglobal.ui.theme.LightningWhite
import com.xd.xdtglobal.ui.theme.OlympusDeep
import com.xd.xdtglobal.ui.theme.OlympusMid
import com.xd.xdtglobal.ui.theme.OlympusNight
import com.xd.xdtglobal.ui.theme.OlympusViolet
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun OlympusBackdrop(
    modifier: Modifier = Modifier,
    showColumns: Boolean = false,
    flashIntensity: Float = 0f,
    content: @Composable () -> Unit = {}
) {
    val infinite = rememberInfiniteTransition(label = "OlympusBackdrop")
    val cloudShift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cloudShift"
    )
    val cloudShiftSlow by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 52000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cloudShiftSlow"
    )

    var lightningAlpha by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        var nextStrike = System.currentTimeMillis() + Random.nextLong(2200, 4500)
        var fadeStart = 0L
        while (true) {
            withFrameMillis { now ->
                if (now >= nextStrike && fadeStart == 0L) {
                    fadeStart = now
                }
                lightningAlpha = if (fadeStart != 0L) {
                    val elapsed = now - fadeStart
                    val a = when {
                        elapsed < 80 -> 1f
                        elapsed < 220 -> 0.55f
                        elapsed < 500 -> 0.25f
                        else -> 0f
                    }
                    if (elapsed > 700) {
                        fadeStart = 0L
                        nextStrike = now + Random.nextLong(2400, 5600)
                        0f
                    } else a
                } else 0f
            }
        }
    }

    val columnSeed = remember { Random(42) }
    val cloudSeed = remember { Random(7) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        OlympusNight,
                        OlympusDeep,
                        OlympusMid,
                        OlympusViolet
                    )
                )
            )
    ) {
        Image(
            painter = painterResource(R.drawable.bg_1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        OlympusViolet.copy(alpha = 0.55f),
                        Color.Transparent
                    )
                ),
                radius = w * 0.9f,
                center = Offset(w * 0.5f, h * 0.85f)
            )

            drawClouds(
                cloudSeed,
                shift = cloudShift,
                yFactor = 0.18f,
                count = 4,
                alpha = 0.20f
            )
            drawClouds(
                cloudSeed,
                shift = cloudShiftSlow,
                yFactor = 0.42f,
                count = 3,
                alpha = 0.13f
            )

            if (showColumns) {
                drawColumns(columnSeed)
            }

            if (lightningAlpha > 0f) {
                drawLightning(lightningAlpha)
            }

            if (flashIntensity > 0f) {
                drawRect(
                    color = LightningWhite.copy(alpha = (flashIntensity * 0.55f).coerceAtMost(0.7f))
                )
            }
        }
        content()
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawClouds(
    rng: Random,
    shift: Float,
    yFactor: Float,
    count: Int,
    alpha: Float
) {
    val w = size.width
    val h = size.height
    val cloudWidth = w * 0.55f
    val baseY = h * yFactor

    repeat(count) { i ->
        val seed = rng.nextDouble().toFloat()
        val rawX = ((seed + shift + i.toFloat() / count) % 1f) * (w + cloudWidth) - cloudWidth * 0.5f
        val centerY = baseY + (sin((shift * 2f + i) * PI).toFloat()) * h * 0.02f
        val brush = Brush.radialGradient(
            colors = listOf(
                Color.White.copy(alpha = alpha),
                ElectricBlue.copy(alpha = alpha * 0.4f),
                Color.Transparent
            ),
            center = Offset(rawX, centerY),
            radius = cloudWidth * 0.55f
        )
        drawCircle(
            brush = brush,
            radius = cloudWidth * 0.55f,
            center = Offset(rawX, centerY)
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawColumns(rng: Random) {
    val w = size.width
    val h = size.height
    val columns = 4
    val gap = w / (columns + 1)
    repeat(columns) { i ->
        val cx = gap * (i + 1)
        val width = w * 0.07f
        val top = h * 0.45f
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.06f),
                    Color.White.copy(alpha = 0.02f),
                    Color.Transparent
                )
            ),
            topLeft = Offset(cx - width / 2f, top),
            size = androidx.compose.ui.geometry.Size(width, h - top)
        )
        drawRect(
            color = Color.White.copy(alpha = 0.05f),
            topLeft = Offset(cx - width / 2f * 1.3f, top - h * 0.02f),
            size = androidx.compose.ui.geometry.Size(width * 1.3f, h * 0.022f)
        )
    }
    rng.nextInt() // keep deterministic seed alive
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawLightning(alpha: Float) {
    val w = size.width
    val h = size.height
    val rng = Random(System.currentTimeMillis() / 600)
    val startX = w * (0.2f + rng.nextFloat() * 0.6f)
    val path = Path().apply {
        moveTo(startX, 0f)
        var x = startX
        var y = 0f
        while (y < h * 0.85f) {
            y += h * (0.04f + rng.nextFloat() * 0.07f)
            x += (rng.nextFloat() - 0.5f) * w * 0.16f
            lineTo(x, y)
        }
    }
    drawPath(
        path = path,
        color = LightningWhite.copy(alpha = (alpha * 0.85f).coerceAtMost(0.95f)),
        style = androidx.compose.ui.graphics.drawscope.Stroke(
            width = 6f,
            pathEffect = PathEffect.cornerPathEffect(8f)
        )
    )
    drawPath(
        path = path,
        color = ElectricCyan.copy(alpha = alpha * 0.45f),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 14f)
    )
}

@Composable
fun rememberSparkleField(): SparkleField {
    val field = remember { SparkleField() }
    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { now ->
                field.tick(now)
            }
        }
    }
    return field
}

class SparkleField {
    private val particles = mutableListOf<Sparkle>()
    private var lastEmit = 0L
    var visibleSnapshot by mutableStateOf<List<Sparkle>>(emptyList())
        private set

    fun emitBurst(originX: Float, originY: Float, color: Color, count: Int = 14) {
        repeat(count) {
            particles += Sparkle(
                x = originX,
                y = originY,
                vx = (Random.nextFloat() - 0.5f) * 800f,
                vy = (Random.nextFloat() - 0.5f) * 800f - 100f,
                life = 600 + Random.nextInt(400),
                bornAt = System.currentTimeMillis(),
                color = color,
                radius = 2.5f + Random.nextFloat() * 3f
            )
        }
        visibleSnapshot = particles.toList()
    }

    fun tick(nowMs: Long) {
        if (particles.isEmpty()) {
            if (visibleSnapshot.isNotEmpty()) visibleSnapshot = emptyList()
            return
        }
        val iter = particles.iterator()
        while (iter.hasNext()) {
            val p = iter.next()
            if (nowMs - p.bornAt > p.life) iter.remove()
        }
        visibleSnapshot = particles.toList()
        lastEmit = nowMs
    }
}

data class Sparkle(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val life: Int,
    val bornAt: Long,
    val color: Color,
    val radius: Float
)
