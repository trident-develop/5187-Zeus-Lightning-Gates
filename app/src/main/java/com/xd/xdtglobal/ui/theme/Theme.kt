package com.xd.xdtglobal.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val OlympusColorScheme = darkColorScheme(
    primary = ZeusGold,
    onPrimary = OlympusNight,
    secondary = ElectricBlue,
    onSecondary = OlympusNight,
    tertiary = OlympusPurple,
    onTertiary = LightningWhite,
    background = OlympusNight,
    onBackground = LightningWhite,
    surface = OlympusDeep,
    onSurface = LightningWhite,
    surfaceVariant = OlympusMid,
    onSurfaceVariant = LightningWhite
)

@Composable
fun ZeusLightningGatesTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = OlympusColorScheme,
        typography = Typography,
        content = content
    )
}
