package com.thejv04.noteapp.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** Cyan color used as the primary brand color across the app. */
val Cyan = Color(0xFF00BCD4)

/** Darker shade of cyan used as secondary color. */
val CyanDark = Color(0xFF0097A7)

/** Light shade of cyan used as tertiary color. */
val CyanLight = Color(0xFFE0F7FA)

/**
 * Dark color scheme for the application.
 * Uses cyan as primary color on a dark background.
 */

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

/**
 * Light color scheme for the application.
 * Uses cyan as primary color on a light background.
 */

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

/**
 * Main theme composable for the NoteApp application.
 * Applies the appropriate color scheme based on the [darkTheme] parameter
 * and wraps the content with [MaterialTheme].
 *
 * @param darkTheme Whether to use the dark color scheme. Defaults to the system setting.
 * @param content The composable content to be themed.
 */
 
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