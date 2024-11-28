package com.sayid.studypath.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sayid.studypath.data.model.LocalPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val localPreferences: LocalPreferences,
) : ViewModel() {
    val isOnboardingCompleted: LiveData<Boolean> = localPreferences.isOnboardingCompleted.asLiveData()

    suspend fun isOnboardingCompleted(): Boolean = localPreferences.isOnboardingCompleted.first()

    suspend fun isDarkTheme(): Boolean = localPreferences.isDarkTheme.first()

    fun completeOnboarding() {
        viewModelScope.launch {
            localPreferences.setOnboardingCompleted(true)
        }
    }
}
