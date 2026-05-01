package com.xd.xdtglobal.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.xd.xdtglobal.game.PieceType

@Composable
fun GemstoneCell(
    type: PieceType,
    size: Dp,
    glow: Float = 0f,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier.size(size)
    ) {
        drawGemstone(
            type = type,
            glow = glow
        )
    }
}

fun DrawScope.drawGemstone(
    type: PieceType,
    glow: Float = 0f
) {
    val w = size.width
    val h = size.height
    val pad = w * 0.08f
    val inset = w * 0.18f

    if (glow > 0f) {
        drawRect(
            color = type.highlight.copy(alpha = 0.35f * glow),
            topLeft = Offset(-w * 0.15f, -h * 0.15f),
            size = androidx.compose.ui.geometry.Size(w * 1.3f, h * 1.3f)
        )
    }

    val rect = androidx.compose.ui.geometry.Rect(
        Offset(pad, pad),
        androidx.compose.ui.geometry.Size(w - pad * 2, h - pad * 2)
    )

    val baseBrush = Brush.linearGradient(
        colors = listOf(
            type.core.lighten(0.20f),
            type.core,
            type.core.darken(0.30f)
        ),
        start = Offset(rect.left, rect.top),
        end = Offset(rect.right, rect.bottom)
    )
    drawRoundRect(
        brush = baseBrush,
        topLeft = Offset(rect.left, rect.top),
        size = rect.size,
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.18f, w * 0.18f)
    )

    val facetPath = Path().apply {
        moveTo(rect.left + inset, rect.top + inset)
        lineTo(rect.right - inset, rect.top + inset)
        lineTo(rect.center.x, rect.center.y)
        close()
    }
    drawPath(
        path = facetPath,
        brush = Brush.verticalGradient(
            colors = listOf(type.highlight.copy(alpha = 0.85f), Color.Transparent),
            startY = rect.top,
            endY = rect.center.y
        )
    )

    val sideFacet = Path().apply {
        moveTo(rect.left + inset, rect.top + inset)
        lineTo(rect.left + inset, rect.bottom - inset)
        lineTo(rect.center.x, rect.center.y)
        close()
    }
    drawPath(
        path = sideFacet,
        brush = Brush.linearGradient(
            colors = listOf(type.core.lighten(0.10f), type.core.darken(0.15f)),
            start = Offset(rect.left, rect.center.y),
            end = Offset(rect.center.x, rect.center.y)
        )
    )

    drawCircle(
        color = type.highlight.copy(alpha = 0.85f),
        radius = w * 0.07f,
        center = Offset(rect.left + inset * 1.05f, rect.top + inset * 0.85f)
    )

    drawRoundRect(
        color = type.rim,
        topLeft = Offset(rect.left, rect.top),
        size = rect.size,
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.18f, w * 0.18f),
        style = Stroke(width = w * 0.05f)
    )
}

private fun Color.lighten(amount: Float): Color {
    return Color(
        red = (red + (1f - red) * amount).coerceIn(0f, 1f),
        green = (green + (1f - green) * amount).coerceIn(0f, 1f),
        blue = (blue + (1f - blue) * amount).coerceIn(0f, 1f),
        alpha = alpha
    )
}

private fun Color.darken(amount: Float): Color {
    return Color(
        red = (red * (1f - amount)).coerceIn(0f, 1f),
        green = (green * (1f - amount)).coerceIn(0f, 1f),
        blue = (blue * (1f - amount)).coerceIn(0f, 1f),
        alpha = alpha
    )
}

@Suppress("unused")
private val PreviewSize: Dp = 32.dp
