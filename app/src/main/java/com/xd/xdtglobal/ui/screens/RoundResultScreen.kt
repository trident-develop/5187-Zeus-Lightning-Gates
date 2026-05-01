package com.xd.xdtglobal.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.xd.xdtglobal.LocalAppContainer
import com.xd.xdtglobal.game.GameConfig
import com.xd.xdtglobal.ui.components.GoldDivider
import com.xd.xdtglobal.ui.components.OlympusBackdrop
import com.xd.xdtglobal.ui.components.ScreenTitle
import com.xd.xdtglobal.ui.components.ZeusButton
import com.xd.xdtglobal.ui.components.ZeusButtonStyle
import com.xd.xdtglobal.ui.theme.ElectricCyan
import com.xd.xdtglobal.ui.theme.LightningWhite
import com.xd.xdtglobal.ui.theme.OlympusDeep
import com.xd.xdtglobal.ui.theme.OlympusMid
import com.xd.xdtglobal.ui.theme.OlympusViolet
import com.xd.xdtglobal.ui.theme.ZeusGold

@Composable
fun RoundResultScreen(
    level: Int,
    score: Int,
    won: Boolean,
    onRetry: () -> Unit,
    onLevels: () -> Unit,
    onMenu: () -> Unit
) {
    val progress = LocalAppContainer.current.progress
    val infinite = rememberInfiniteTransition(label = "result")
    val pulse by infinite.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    OlympusBackdrop(showColumns = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenTitle(text = if (won) "VICTORY" else "FALLEN")
            GoldDivider()
            Spacer(modifier = Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(OlympusViolet, OlympusMid, OlympusDeep)),
                        RoundedCornerShape(22.dp)
                    )
                    .border(
                        width = 3.dp,
                        color = (if (won) ZeusGold else ElectricCyan).copy(alpha = pulse),
                        shape = RoundedCornerShape(22.dp)
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (won) "Olympus rejoices!" else "The storm calmed too soon.",
                        style = MaterialTheme.typography.titleLarge,
                        color = if (won) ZeusGold else LightningWhite,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "Level $level",
                        style = MaterialTheme.typography.headlineMedium,
                        color = LightningWhite
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Score: $score",
                        style = MaterialTheme.typography.titleLarge,
                        color = ElectricCyan
                    )
                    Text(
                        text = "Target: ${GameConfig.targetScoreFor(level)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = LightningWhite.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val best = progress.bestScoreFor(level)
                    Text(
                        text = "Best: $best",
                        style = MaterialTheme.typography.titleMedium,
                        color = ZeusGold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            ZeusButton(
                text = "RETRY",
                style = ZeusButtonStyle.Primary,
                modifier = Modifier.fillMaxWidth(),
                cooldownMillis = 900L,
                onClick = onRetry
            )
            Spacer(modifier = Modifier.height(12.dp))
            ZeusButton(
                text = "LEVELS",
                style = ZeusButtonStyle.Secondary,
                modifier = Modifier.fillMaxWidth(),
                cooldownMillis = 900L,
                onClick = onLevels
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
