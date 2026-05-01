package com.xd.xdtglobal.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.xd.xdtglobal.ui.theme.ElectricCyan
import com.xd.xdtglobal.ui.theme.LightningWhite
import com.xd.xdtglobal.ui.theme.ZeusFont
import com.xd.xdtglobal.ui.theme.ZeusGold
import com.xd.xdtglobal.ui.theme.ZeusGoldDeep

@Composable
fun ScreenTitle(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.headlineLarge
) {
    val infinite = rememberInfiniteTransition(label = "ScreenTitle")
    val pulse by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = style.copy(
                fontFamily = ZeusFont,
                brush = Brush.linearGradient(
                    listOf(
                        ZeusGoldDeep,
                        ZeusGold,
                        LightningWhite,
                        ElectricCyan,
                        ZeusGold
                    )
                ),
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = ElectricCyan.copy(alpha = 0.45f + pulse * 0.4f),
                    blurRadius = 18f + pulse * 10f
                )
            ),
            textAlign = TextAlign.Center
        )
    }
}
