package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ReviaDarkColorScheme = darkColorScheme(
    primary = VintageCrimson,
    onPrimary = Color.White,
    primaryContainer = ElectricVioletDark,
    onPrimaryContainer = TextPrimary,
    secondary = VintageGold,
    onSecondary = DarkBg,
    secondaryContainer = TechBlueDark,
    onSecondaryContainer = TextPrimary,
    tertiary = VintageSage,
    onTertiary = DarkBg,
    tertiaryContainer = TurquoiseAccent,
    onTertiaryContainer = DarkBg,
    background = DarkBg,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextSecondary,
    outline = DarkBorder,
    error = RedError,
    onError = Color.White
)

val ReviaLightColorScheme = lightColorScheme(
    primary = VintageCrimson,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8D0C9),
    onPrimaryContainer = Color(0xFF4A1810),
    secondary = VintageGold,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF1E5CB),
    onSecondaryContainer = Color(0xFF4D3811),
    tertiary = VintageSage,
    onTertiary = Color.White,
    background = Color(0xFFF7F2E8),
    onBackground = Color(0xFF261D15),
    surface = Color(0xFFFDFBF7),
    onSurface = Color(0xFF261D15),
    surfaceVariant = Color(0xFFECE2D0),
    onSurfaceVariant = Color(0xFF5A4D3E),
    outline = Color(0xFFCEBD9F),
    error = RedError,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to dark theme as requested
    dynamicColor: Boolean = false, // Preserve brand palette
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) ReviaDarkColorScheme else ReviaLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

