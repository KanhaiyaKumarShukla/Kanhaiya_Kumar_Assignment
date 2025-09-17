package com.example.kanhaiya_kumar_assignment.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import java.io.IOException

private val THEME_KEY = booleanPreferencesKey("is_dark_theme")
private const val DATASTORE_NAME = "settings"
private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

@Singleton
class ThemeManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()
    
    init {
        // Load persisted value
        scope.launch {
            context.dataStore.data
                .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
                .map { prefs -> prefs[THEME_KEY] ?: false }
                .collect { value -> _isDarkTheme.value = value }
        }
    }

    fun toggleTheme() {
        scope.launch {
            val newValue = !isDarkTheme.value
            context.dataStore.edit { prefs -> prefs[THEME_KEY] = newValue }
            _isDarkTheme.value = newValue
        }
    }
    
    fun setDarkTheme(isDark: Boolean) {
        scope.launch {
            context.dataStore.edit { prefs -> prefs[THEME_KEY] = isDark }
            _isDarkTheme.value = isDark
        }
    }
}

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeManager: ThemeManager
) : ViewModel() {
    
    val isDarkTheme = themeManager.isDarkTheme
    
    fun toggleTheme() {
        viewModelScope.launch {
            themeManager.toggleTheme()
        }
    }
}
