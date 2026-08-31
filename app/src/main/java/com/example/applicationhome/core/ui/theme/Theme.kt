package com.example.applicationhome.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.applicationhome.data.data.model.ThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = Color.DarkOrange,
    background = Color.DarkCharcoal,
    surface = Color.CharcoalGray,
    surfaceContainerHigh = Color.DarkOrange,
    surfaceContainerHighest = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = Color.LightGray,
    outline = Color.Gray,
    onSecondary = Color.Gray,
    onTertiary = Color.DarkGray,
    onPrimary = Color.DarkOrange
)

private val LightColorScheme = lightColorScheme(
    primary = Color.DarkOrange,
    background = Color.VeryLightGray,
    surface = Color.White,
    surfaceContainerHigh = Color.LightGray,
    surfaceContainerHighest = Color.White,
    onSurface = Color.Black,
    onSurfaceVariant = Color.Gray,
    outline = Color.Gray,
    onSecondary = Color.LightGray,
    onTertiary = Color(0xFFECECEC),
    onPrimary = Color.DeepMatteBlack

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ApplicationHomeTheme(
    themeMode : ThemeMode = ThemeMode.SYSTEM,
    // Dynamic color is available on Android 12+
    dynamicColor : Boolean = false,
    content : @Composable () -> Unit
){
    val darkTheme = when(themeMode){
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}