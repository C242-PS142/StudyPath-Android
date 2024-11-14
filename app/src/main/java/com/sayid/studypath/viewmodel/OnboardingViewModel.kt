package com.sayid.studypath.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sayid.studypath.data.model.OnboardingPreference

class OnboardingViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val onboardingPreference = OnboardingPreference(application)

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
