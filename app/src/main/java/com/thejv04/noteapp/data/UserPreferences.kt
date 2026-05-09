package com.thejv04.noteapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Extension property that provides a [DataStore] instance for the given [Context].
 * Uses the Singleton pattern to ensure only one instance is created.
 */

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * Manages user preferences using Jetpack DataStore.
 * Stores and retrieves persistent user settings such as
 * dark mode preference and language selection.
 *
 * @param context The application context used to access the DataStore.
 */

class UserPreferences(private val context: Context) {

    companion object {
        /** Key for storing the dark mode preference. */
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        /** Key for storing the language preference. */
        val LANGUAGE_KEY = stringPreferencesKey("language")
    }

    /**
     * A [Flow] that emits the current dark mode preference.
     * Defaults to false (light mode) if not set.
     */

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DARK_MODE_KEY] ?: false
    }

    /**
     * A [Flow] that emits the current language preference.
     * Defaults to Spanish ("es") if not set.
     */

    val language: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LANGUAGE_KEY] ?: "es"
    }

    /**
     * Updates the dark mode preference in the DataStore.
     *
     * @param enabled True to enable dark mode, false for light mode.
     */

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }

    /**
     * Updates the language preference in the DataStore.
     *
     * @param lang The language code to set (e.g. "es" for Spanish, "en" for English).
     */

    suspend fun setLanguage(lang: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = lang
        }
    }
}