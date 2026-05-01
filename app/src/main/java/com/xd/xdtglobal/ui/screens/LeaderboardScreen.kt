package com.xd.xdtglobal.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.xd.xdtglobal.ui.theme.ZeusGoldDeep

@Composable
fun LeaderboardScreen(onBack: () -> Unit) {
    val progress = LocalAppContainer.current.progress
    OlympusBackdrop(showColumns = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .padding(top = 32.dp)
        ) {
            ScreenTitle(text = "LEADERBOARD")
            GoldDivider()
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("LEVEL", style = MaterialTheme.typography.labelMedium, color = ElectricCyan)
                Text("BEST", style = MaterialTheme.typography.labelMedium, color = ElectricCyan)
                Text("STATUS", style = MaterialTheme.typography.labelMedium, color = ElectricCyan)
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items((1..GameConfig.LEVEL_COUNT).toList()) { level ->
                    LeaderboardRow(
                        level = level,
                        bestScore = progress.bestScoreFor(level),
                        completed = progress.isLevelCompleted(level)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            ZeusButton(
                text = "BACK",
                style = ZeusButtonStyle.Stone,
                modifier = Modifier.fillMaxWidth(),
                cooldownMillis = 800L,
                onClick = onBack
            )
        }
    }
}

@Composable
private fun LeaderboardRow(level: Int, bestScore: Int, completed: Boolean) {
    val infinite = rememberInfiniteTransition(label = "row")
    val pulse by infinite.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val borderColor = if (completed) ZeusGold.copy(alpha = pulse) else Color(0xFF2C3357)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(listOf(OlympusViolet, OlympusMid, OlympusDeep)),
                RoundedCornerShape(14.dp)
            )
            .border(2.dp, borderColor, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Level $level",
            style = MaterialTheme.typography.titleMedium,
            color = LightningWhite
        )
        Text(
            text = if (bestScore > 0) "$bestScore" else "—",
            style = MaterialTheme.typography.titleMedium,
            color = if (completed) ZeusGold else ZeusGoldDeep
        )
        Text(
            text = if (completed) "✦ Completed" else "Locked / In progress",
            style = MaterialTheme.typography.labelMedium,
            color = if (completed) ZeusGold else ElectricCyan.copy(alpha = 0.7f)
        )
    }
}
