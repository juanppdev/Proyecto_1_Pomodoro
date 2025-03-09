package com.mundocode.pomodoro.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(private val themePreferences: ThemePreferences) : ViewModel() {

    private val _selectedTheme = MutableStateFlow("Tema Claro")
    val selectedTheme: StateFlow<String> = _selectedTheme.asStateFlow()

    private val _currentTheme = MutableStateFlow("Tema Claro") // Valor por defecto
    val currentTheme: StateFlow<String> = _currentTheme.asStateFlow()

    init {
        viewModelScope.launch {
            themePreferences.selectedTheme.collect { theme ->
                _selectedTheme.value = theme
                Timber.tag("ThemeViewModel").d("🎨 Tema cargado: $theme")
            }
        }
    }

    fun changeTheme(themeName: String) {
        viewModelScope.launch {
            themePreferences.saveTheme(themeName)
            _selectedTheme.value = themeName
            Timber.tag("ThemeViewModel").d("🎨 Tema cambiado a: $themeName")
        }
    }
}
