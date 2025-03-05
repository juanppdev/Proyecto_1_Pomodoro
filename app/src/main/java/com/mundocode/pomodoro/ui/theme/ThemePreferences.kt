package com.mundocode.pomodoro.ui.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_prefs")

class ThemePreferences(private val context: Context) {

    companion object {
        private val THEME_KEY = stringPreferencesKey("theme_key") // ✅ Se asegura de usar la clave correcta
    }

    val selectedTheme: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: "Claro"
    }

    suspend fun saveTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme
        }
        Timber.tag("ThemePreferences").d("✅ Tema guardado: $theme")
    }
}
