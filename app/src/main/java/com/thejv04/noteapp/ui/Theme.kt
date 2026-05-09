package com.thejv04.noteapp.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Cyan = Color(0xFF00BCD4)
val CyanDark = Color(0xFF0097A7)
val CyanLight = Color(0xFFE0F7FA)

private val DarkColorScheme = darkColorScheme(
    primary = Cyan,
    secondary = CyanDark,
    tertiary = CyanLight,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Cyan,
    secondary = CyanDark,
    tertiary = CyanLight,
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    onPrimary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun NoteAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}