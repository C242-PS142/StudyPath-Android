package com.sayid.studypath.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sayid.studypath.data.model.OnboardingPreference

class OnboardingViewModel(
    private val onboardingPreference: OnboardingPreference,
) : ViewModel() {
    private val _onboardingCompleted = MutableLiveData<Boolean>()
    val onboardingCompleted: LiveData<Boolean> get() = _onboardingCompleted

    init {
        _onboardingCompleted.value = onboardingPreference.isOnboardingCompleted()
    }

    fun completeOnboarding() {
        onboardingPreference.setOnboardingCompleted(true)
        _onboardingCompleted.value = true
    }
}
