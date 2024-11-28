package com.sayid.studypath.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sayid.studypath.data.model.LocalPreferences
import com.sayid.studypath.data.repository.AuthRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val authRepository: AuthRepository,
    private val localPreferences: LocalPreferences,
) : ViewModel() {
    val isDarkTheme: LiveData<Boolean> = localPreferences.isDarkTheme.asLiveData()

    fun setDarkTheme(active: Boolean) {
        viewModelScope.launch {
            localPreferences.setDarkTheme(active)
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}
