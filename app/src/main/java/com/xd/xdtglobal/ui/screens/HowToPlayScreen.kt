package com.xd.xdtglobal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
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
fun HowToPlayScreen(onBack: () -> Unit) {
    OlympusBackdrop(showColumns = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .padding(top = 32.dp)
        ) {
            ScreenTitle(text = "HOW TO PLAY")
            GoldDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .background(
                        Brush.verticalGradient(listOf(OlympusViolet, OlympusMid, OlympusDeep)),
                        RoundedCornerShape(20.dp)
                    )
                    .border(2.dp, ZeusGold.copy(alpha = 0.55f), RoundedCornerShape(20.dp))
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Tip("⚡", "Move gemstone pieces left and right with the side buttons.")
                Tip("🔄", "Rotate pieces with the rotate button to fit the gaps.")
                Tip("📏", "Complete full horizontal lines to clear gemstones.")
                Tip("✨", "Cleared lines award points: 1=100, 2=250, 3=500, 4=900.")
                Tip("🏆", "Reach the level's target score to win the round and unlock the next level.")
                Tip("⏸", "Tap Pause anytime to stop the storm and resume later.")
                Tip("🎵", "Toggle music and sound effects from Settings.")
                Tip("👑", "Zeus watches over your run — clear multi-line combos for divine praise.")
            }

            Spacer(modifier = Modifier.height(16.dp))
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
private fun Tip(icon: String, text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Text(
            text = icon,
            style = MaterialTheme.typography.titleMedium,
            color = ZeusGold,
            modifier = Modifier.padding(end = 12.dp, top = 2.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = LightningWhite
        )
    }
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(ElectricCyan.copy(alpha = 0.18f))
    )
}
