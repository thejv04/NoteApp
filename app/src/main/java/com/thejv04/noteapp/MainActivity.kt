package com.thejv04.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.thejv04.noteapp.data.NoteViewModel
import com.thejv04.noteapp.data.UserPreferences
import com.thejv04.noteapp.ui.NoteAppTheme
import com.thejv04.noteapp.ui.NoteEditScreen
import com.thejv04.noteapp.ui.NoteListScreen
import com.thejv04.noteapp.ui.SettingsScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val userPreferences = remember { UserPreferences(context) }
            val isDarkMode by userPreferences.isDarkMode.collectAsState(initial = false)
            val language by userPreferences.language.collectAsState(initial = "es")
            val scope = rememberCoroutineScope()

            NoteAppTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "note_list") {
                    composable("note_list") {
                        NoteListScreen(
                            viewModel = viewModel,
                            onNoteClick = { note -> navController.navigate("note_edit/${note.id}/false") },
                            onAddNote = { isChecklist ->
                                navController.navigate("note_edit/-1/$isChecklist")
                            },
                            onSettings = { navController.navigate("settings") },
                            language = language
                        )
                    }
                    composable(
                        route = "note_edit/{noteId}/{isChecklist}",
                        arguments = listOf(
                            navArgument("noteId") { type = NavType.IntType },
                            navArgument("isChecklist") { type = NavType.BoolType }
                        )
                    ) { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getInt("noteId")
                        val isChecklist = backStackEntry.arguments?.getBoolean("isChecklist") ?: false
                        NoteEditScreen(
                            viewModel = viewModel,
                            noteId = noteId,
                            isChecklistDefault = isChecklist,
                            onBack = { navController.popBackStack() },
                            language = language
                        )
                    }
                    composable("settings") {
                        SettingsScreen(
                            isDarkMode = isDarkMode,
                            onDarkModeToggle = { enabled ->
                                scope.launch { userPreferences.setDarkMode(enabled) }
                            },
                            language = language,
                            onLanguageChange = { lang ->
                                scope.launch { userPreferences.setLanguage(lang) }
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}