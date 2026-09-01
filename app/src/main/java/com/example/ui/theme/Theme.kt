package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SophisticatedDarkColorScheme = darkColorScheme(
    primary = SophisticatedGold,
    onPrimary = Color(0xFF121212),
    primaryContainer = SophisticatedGoldContainer,
    onPrimaryContainer = SophisticatedOnGoldContainer,
    secondary = Color(0xFFE5C37E),
    onSecondary = Color(0xFF1E1E1E),
    secondaryContainer = Color(0xFF352C1C),
    onSecondaryContainer = Color(0xFFF8EED9),
    tertiary = Color(0xFFC4A47C),
    onTertiary = Color(0xFF121212),
    tertiaryContainer = Color(0xFF2F241E),
    onTertiaryContainer = Color(0xFFF5DDCB),
    background = SophisticatedBackground,
    surface = SophisticatedSurface,
    surfaceVariant = SophisticatedSurfaceVariant,
    surfaceContainer = SophisticatedSurfaceContainer,
    onBackground = SophisticatedOnBackground,
    onSurface = SophisticatedOnSurface,
    onSurfaceVariant = SophisticatedOnSurfaceVariant,
    outline = Color(0xFF3E3E3E),
    outlineVariant = SophisticatedGlassBorder,
    error = Color(0xFFE57373),
    onError = Color(0xFF1E1E1E),
    errorContainer = Color(0xFF3E1818),
    onErrorContainer = Color(0xFFFFCDD2)
)

@Composable
fun EduTributeTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SophisticatedDarkColorScheme,
        typography = Typography,
        content = content
    )
}
