package com.exemplo.copa2026.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CopaColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    primaryContainer = GreenLight,
    onPrimaryContainer = Color(0xFF1B5E20),
    secondary = BlueAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFBBDEFB),
    tertiary = Gold,
    background = GrayBackground,
    onBackground = OnGrayBackground,
    surface = GraySurface,
    onSurface = OnGraySurface,
    surfaceVariant = GraySurfaceVariant,
    onSurfaceVariant = OnGraySurfaceVariant,
    outline = Color(0xFFCCCCCC),
    outlineVariant = Color(0xFFE0E0E0)
)

@Composable
fun Copa2026Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CopaColorScheme,
        typography = CopaTypography,
        shapes = CopaShapes,
        content = content
    )
}
