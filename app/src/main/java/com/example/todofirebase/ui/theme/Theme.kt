package com.example.todofirebase.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = TailwindColor.Amber.shade600,
    secondary = TailwindColor.Blue.shade600,
    tertiary = TailwindColor.Purple.shade600,

    background = TailwindColor.Neutral.shade900,
    surface = TailwindColor.Neutral.shade800,
    onPrimary = TailwindColor.Neutral.shade50,
    onSecondary = TailwindColor.Neutral.shade50,
    onTertiary = TailwindColor.Neutral.shade50,
    onBackground = TailwindColor.Neutral.shade50,
    onSurface = TailwindColor.Neutral.shade50,
)

private val LightColorScheme = lightColorScheme(
    primary = TailwindColor.Amber.shade600,
    secondary = TailwindColor.Blue.shade600,
    tertiary = TailwindColor.Purple.shade600,


    background = TailwindColor.Neutral.shade900,
    surface = TailwindColor.Neutral.shade800,
    onPrimary = TailwindColor.Neutral.shade100,
    onSecondary = TailwindColor.Neutral.shade50,
    onTertiary = TailwindColor.Neutral.shade50,
    onBackground = TailwindColor.Neutral.shade50,
    onSurface = TailwindColor.Neutral.shade50,

    onSurfaceVariant = TailwindColor.Neutral.shade700,

)

@Composable
fun TodoFirebaseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
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