package com.xd.xdtglobal.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.xd.xdtglobal.ui.theme.ElectricBlue
import com.xd.xdtglobal.ui.theme.ElectricCyan
import com.xd.xdtglobal.ui.theme.LightningWhite
import com.xd.xdtglobal.ui.theme.OlympusDeep
import com.xd.xdtglobal.ui.theme.OlympusMid
import com.xd.xdtglobal.ui.theme.OlympusPurple
import com.xd.xdtglobal.ui.theme.OlympusViolet
import com.xd.xdtglobal.ui.theme.ZeusGold
import com.xd.xdtglobal.ui.theme.ZeusGoldDeep

enum class ZeusButtonStyle { Primary, Secondary, Stone }

@Composable
fun ZeusButton(
    text: String,
    modifier: Modifier = Modifier,
    style: ZeusButtonStyle = ZeusButtonStyle.Primary,
    enabled: Boolean = true,
    cooldownMillis: Long = 1000L,
    height: Dp = 64.dp,
    leadingIcon: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    val infinite = rememberInfiniteTransition(label = "ZeusButton")
    val shimmer by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val (bgBrush, borderColor, textColor) = when (style) {
        ZeusButtonStyle.Primary -> Triple(
            Brush.linearGradient(listOf(ZeusGoldDeep, ZeusGold, LightningWhite)),
            ZeusGoldDeep,
            OlympusDeep
        )
        ZeusButtonStyle.Secondary -> Triple(
            Brush.linearGradient(listOf(OlympusViolet, OlympusPurple, ElectricBlue)),
            ElectricCyan,
            LightningWhite
        )
        ZeusButtonStyle.Stone -> Triple(
            Brush.linearGradient(listOf(OlympusDeep, OlympusMid)),
            ZeusGold.copy(alpha = 0.55f),
            LightningWhite
        )
    }

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = height)
            .height(height)
            .pressableWithCooldown(
                cooldownMillis = cooldownMillis,
                enabled = enabled,
                onClick = onClick
            )
            .background(
                brush = bgBrush,
                shape = RoundedCornerShape(percent = 50)
            )
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(percent = 50)
            )
            .padding(PaddingValues(horizontal = 22.dp, vertical = 8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            if (leadingIcon != null) {
                Box(modifier = Modifier.size(height * 0.55f).align(Alignment.CenterStart)) {
                    leadingIcon()
                }
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = textColor.copy(alpha = if (enabled) 1f else 0.55f),
                textAlign = TextAlign.Center
            )
        }
        // shimmer line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .padding(horizontal = (40 + 240 * shimmer).dp.coerceAtMost(120.dp))
                .background(
                    color = if (style == ZeusButtonStyle.Primary)
                        LightningWhite.copy(alpha = 0.6f * (1f - kotlin.math.abs(shimmer - 0.5f) * 2f))
                    else
                        ElectricCyan.copy(alpha = 0.55f * (1f - kotlin.math.abs(shimmer - 0.5f) * 2f))
                )
                .align(Alignment.TopCenter)
        )
        Spacer(modifier = Modifier.width(0.dp))
    }
}

@Composable
fun GoldDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color.Transparent,
                        ZeusGold.copy(alpha = 0.7f),
                        ZeusGold,
                        ZeusGold.copy(alpha = 0.7f),
                        Color.Transparent
                    )
                )
            )
    )
}
