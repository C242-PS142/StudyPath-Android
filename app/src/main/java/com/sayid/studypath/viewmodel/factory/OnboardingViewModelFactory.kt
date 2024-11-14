package com.sayid.studypath.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayid.studypath.data.model.OnboardingPreference
import com.sayid.studypath.viewmodel.OnboardingViewModel

class OnboardingViewModelFactory(
    private val onboardingPreference: OnboardingPreference,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnboardingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OnboardingViewModel(onboardingPreference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
