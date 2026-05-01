package com.xd.xdtglobal.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.xd.xdtglobal.LocalAppContainer
import com.xd.xdtglobal.ui.components.GoldDivider
import com.xd.xdtglobal.ui.components.OlympusBackdrop
import com.xd.xdtglobal.ui.components.ScreenTitle
import com.xd.xdtglobal.ui.components.ZeusButton
import com.xd.xdtglobal.ui.components.ZeusButtonStyle
import com.xd.xdtglobal.ui.components.pressableWithCooldown
import com.xd.xdtglobal.ui.theme.ElectricBlue
import com.xd.xdtglobal.ui.theme.ElectricCyan
import com.xd.xdtglobal.ui.theme.LightningWhite
import com.xd.xdtglobal.ui.theme.OlympusDeep
import com.xd.xdtglobal.ui.theme.OlympusMid
import com.xd.xdtglobal.ui.theme.OlympusViolet
import com.xd.xdtglobal.ui.theme.ZeusGold
import com.xd.xdtglobal.ui.theme.ZeusGoldDeep

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onHowToPlay: () -> Unit,
    onPrivacy: () -> Unit
) {
    val container = LocalAppContainer.current
    val settings = container.settings
    val isInPreview = LocalInspectionMode.current

    OlympusBackdrop(showColumns = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenTitle(text = "SETTINGS")
            GoldDivider()
            Spacer(modifier = Modifier.height(20.dp))

            SettingRow(
                label = "Music",
                checked = settings.musicEnabled,
                onToggle = {
                    settings.setMusic(it)
                    container.audio.applyMusicSetting()
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingRow(
                label = "Win / Lose Sound",
                checked = settings.winLoseSoundEnabled,
                onToggle = settings::setWinLoseSound
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingRow(
                label = "Lightning Sound",
                checked = settings.lightningSoundEnabled,
                onToggle = settings::setLightningSound
            )

            Spacer(modifier = Modifier.height(28.dp))
            ZeusButton(
                text = "HOW TO PLAY",
                style = ZeusButtonStyle.Secondary,
                modifier = Modifier.fillMaxWidth(),
                cooldownMillis = 900L,
                onClick = onHowToPlay
            )
            Spacer(modifier = Modifier.height(12.dp))
            ZeusButton(
                text = "PRIVACY POLICY",
                style = ZeusButtonStyle.Secondary,
                modifier = Modifier.fillMaxWidth(),
                cooldownMillis = 900L,
                onClick = onPrivacy
            )

            Spacer(modifier = Modifier.weight(1f))

            if (!isInPreview) {
                AndroidView(
                    factory = {
                        val adView = AdView(it)
                        adView.setAdSize(AdSize.BANNER)
                        adView.adUnitId = "ca-app-pub-3940256099942544/9214589741"
                        adView.loadAd(AdRequest.Builder().build())
                        adView
                    }
                )
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
private fun SettingRow(
    label: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(listOf(OlympusViolet, OlympusMid, OlympusDeep)),
                RoundedCornerShape(16.dp)
            )
            .border(2.dp, ZeusGoldDeep.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = LightningWhite
        )
        AnimatedToggle(checked = checked, onToggle = { onToggle(!checked) })
    }
}

@Composable
private fun AnimatedToggle(checked: Boolean, onToggle: () -> Unit) {
    val rawOffset by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = spring(),
        label = "toggle"
    )
    val offset = rawOffset.coerceIn(0f, 1f)
    val trackBrush = if (checked) {
        Brush.horizontalGradient(listOf(ZeusGoldDeep, ZeusGold))
    } else {
        Brush.horizontalGradient(listOf(OlympusDeep, OlympusMid))
    }
    Box(
        modifier = Modifier
            .pressableWithCooldown(cooldownMillis = 200L, onClick = onToggle)
            .width(64.dp)
            .height(32.dp)
            .clip(RoundedCornerShape(50))
            .background(trackBrush)
            .border(
                2.dp,
                if (checked) ZeusGold else ElectricBlue.copy(alpha = 0.6f),
                RoundedCornerShape(50)
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .padding(start = (4 + offset * 32).dp, top = 4.dp)
                .size(24.dp)
                .clip(RoundedCornerShape(50))
                .background(
                    Brush.radialGradient(
                        listOf(
                            LightningWhite,
                            if (checked) ZeusGold else ElectricCyan
                        )
                    )
                )
        )
    }
}
