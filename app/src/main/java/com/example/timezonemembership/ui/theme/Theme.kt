package com.example.timezonemembership.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val TimezoneLightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = LightSurface,
    primaryContainer = BluePrimaryLight,
    onPrimaryContainer = BluePrimaryDark,
    secondary = GoldSecondary,
    onSecondary = LightSurface,
    secondaryContainer = GoldSecondaryLight,
    onSecondaryContainer = GoldSecondaryDark,
    tertiary = TealAccent,
    onTertiary = LightSurface,
    tertiaryContainer = TealAccentLight,
    onTertiaryContainer = TealAccent,
    background = LightBackground,
    onBackground = OnLightSurface,
    surface = LightSurface,
    onSurface = OnLightSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = OnLightSurfaceVariant,
    error = ErrorRed,
    onError = LightSurface,
)

@Composable
fun TimezoneMembershipTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TimezoneLightColorScheme,
        typography = Typography,
        content = content
    )
}