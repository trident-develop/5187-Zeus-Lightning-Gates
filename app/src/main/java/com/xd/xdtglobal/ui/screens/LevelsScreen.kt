package com.xd.xdtglobal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.xd.xdtglobal.LocalAppContainer
import com.xd.xdtglobal.game.GameConfig
import com.xd.xdtglobal.ui.components.GoldDivider
import com.xd.xdtglobal.ui.components.OlympusBackdrop
import com.xd.xdtglobal.ui.components.ScreenTitle
import com.xd.xdtglobal.ui.components.ZeusButton
import com.xd.xdtglobal.ui.components.ZeusButtonStyle
import com.xd.xdtglobal.ui.components.pressableWithCooldown
import com.xd.xdtglobal.ui.theme.ElectricCyan
import com.xd.xdtglobal.ui.theme.LightningWhite
import com.xd.xdtglobal.ui.theme.OlympusDeep
import com.xd.xdtglobal.ui.theme.OlympusMid
import com.xd.xdtglobal.ui.theme.OlympusViolet
import com.xd.xdtglobal.ui.theme.ZeusGold
import com.xd.xdtglobal.ui.theme.ZeusGoldDeep

@Composable
fun LevelsScreen(
    onLevelSelected: (Int) -> Unit,
    onBack: () -> Unit
) {
    val container = LocalAppContainer.current
    val progress = container.progress

    OlympusBackdrop(showColumns = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .padding(top = 32.dp)
        ) {
            ScreenTitle(text = "LEVELS")
            GoldDivider()
            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items((1..GameConfig.LEVEL_COUNT).toList()) { level ->
                    LevelCard(
                        level = level,
                        unlocked = progress.isLevelUnlocked(level),
                        completed = progress.isLevelCompleted(level),
                        bestScore = progress.bestScoreFor(level),
                        targetScore = GameConfig.targetScoreFor(level),
                        onClick = {
                            if (progress.isLevelUnlocked(level)) onLevelSelected(level)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
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
private fun LevelCard(
    level: Int,
    unlocked: Boolean,
    completed: Boolean,
    bestScore: Int,
    targetScore: Int,
    onClick: () -> Unit
) {
    val borderColor = when {
        completed -> ZeusGold
        unlocked -> ElectricCyan
        else -> Color(0xFF2C3357)
    }
    val gradient = if (unlocked) {
        Brush.linearGradient(listOf(OlympusViolet, OlympusMid, OlympusDeep))
    } else {
        Brush.linearGradient(listOf(Color(0xFF1A1F3D), Color(0xFF14193A)))
    }
    Box(
        modifier = Modifier
            .aspectRatio(0.85f)
            .pressableWithCooldown(
                cooldownMillis = 800L,
                enabled = unlocked,
                onClick = onClick
            )
            .alpha(if (unlocked) 1f else 0.55f)
            .background(gradient, RoundedCornerShape(18.dp))
            .border(2.dp, borderColor, RoundedCornerShape(18.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (unlocked) "LV $level" else "🔒",
                style = MaterialTheme.typography.titleLarge,
                color = if (completed) ZeusGold else if (unlocked) LightningWhite else Color(0xFF7A82A6)
            )
            Spacer(modifier = Modifier.height(2.dp))
            if (unlocked) {
                Text(
                    text = "Goal: $targetScore",
                    style = MaterialTheme.typography.labelMedium,
                    color = ElectricCyan
                )
                if (bestScore > 0) {
                    Text(
                        text = "Best: $bestScore",
                        style = MaterialTheme.typography.labelMedium,
                        color = ZeusGoldDeep
                    )
                }
                if (completed) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "★",
                            style = MaterialTheme.typography.titleMedium,
                            color = ZeusGold
                        )
                    }
                }
            } else {
                Text(
                    text = "Complete\nprevious level",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF7A82A6),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
