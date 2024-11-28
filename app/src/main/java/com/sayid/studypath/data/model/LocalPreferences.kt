package com.sayid.studypath.data.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalPreferences(
    private val dataStore: DataStore<Preferences>,
) {
    private val isOnboardingCompletedKey = booleanPreferencesKey(IS_ONBOARDING_COMPLETED_KEY)
    private val isDarkThemeKey = booleanPreferencesKey(IS_DARK_THEME_KEY)

    val isOnboardingCompleted: Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[isOnboardingCompletedKey] ?: false
        }

    val isDarkTheme: Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[isDarkThemeKey] ?: false
        }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[isOnboardingCompletedKey] = completed
        }
    }

    suspend fun setDarkTheme(active: Boolean) {
        dataStore.edit { preferences ->
            preferences[isDarkThemeKey] = active
        }
    }

    companion object {
        private const val IS_ONBOARDING_COMPLETED_KEY = "is_onboarding_completed"
        private const val IS_DARK_THEME_KEY = "is_dark_theme_key"
    }
}

// Extension for creating the DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "study_path")
