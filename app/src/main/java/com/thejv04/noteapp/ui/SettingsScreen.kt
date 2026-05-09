package com.thejv04.noteapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext

/**
 * Settings screen that allows the user to customize the app experience.
 * Provides options for toggling dark mode, selecting the app language,
 * and displays information about the app including license and repository link.
 *
 * @param isDarkMode Whether dark mode is currently enabled.
 * @param onDarkModeToggle Callback invoked when the dark mode switch is toggled.
 * @param language The current language code ("es" or "en").
 * @param onLanguageChange Callback invoked when the user selects a different language.
 * @param onBack Callback invoked when the user navigates back.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit,
    language: String,
    onLanguageChange: (String) -> Unit,
    onBack: () -> Unit
) {
    val isSpanish = language == "es"
    val context = LocalContext.current
    val title = if (isSpanish) "Preferencias" else "Preferences"
    val darkModeTitle = if (isSpanish) "Modo oscuro" else "Dark mode"
    val darkModeSubtitle =
        if (isSpanish) "Cambiar apariencia de la app" else "Change app appearance"
    val languageTitle = if (isSpanish) "Idioma" else "Language"
    val aboutTitle = if (isSpanish) "Acerca de" else "About"
    val appVersion = if (isSpanish) "Versión" else "Version"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            /** Appearance section — dark mode toggle. */
            Text(
                text = if (isSpanish) "Apariencia" else "Appearance",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = darkModeTitle, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = darkModeSubtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(checked = isDarkMode, onCheckedChange = onDarkModeToggle)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            /** Language section — chip selector for Spanish and English. */
            Text(
                text = languageTitle,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = isSpanish,
                    onClick = { onLanguageChange("es") },
                    label = { Text("Español") }
                )
                FilterChip(
                    selected = !isSpanish,
                    onClick = { onLanguageChange("en") },
                    label = { Text("English") }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            /** About section — app info, license and repository link. */
            Text(
                text = aboutTitle,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "NoteApp",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$appVersion: 1.0.0",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isSpanish)
                            "Desarrollado por Javier Ignacio\nEstudiante de Ingeniería Civil en Informática\nUniversidad Católica de Temuco"
                        else
                            "Developed by Javier Ignacio\nComputer Science Engineering Student\nCatholic University of Temuco",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isSpanish) "Licencia" else "License",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "MIT License © 2026 Javier Ignacio",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isSpanish) "Repositorio" else "Repository",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "github.com/thejv04/NoteApp",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            val intent = android.content.Intent(
                                android.content.Intent.ACTION_VIEW,
                                android.net.Uri.parse("https://github.com/thejv04/NoteApp")
                            )
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}