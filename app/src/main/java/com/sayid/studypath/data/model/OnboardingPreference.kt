package com.sayid.studypath.data.model

import android.content.Context

class OnboardingPreference(
    context: Context,
) {
    private val preferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)

    fun isOnboardingCompleted(): Boolean = preferences.getBoolean(IS_ONBOARDING_COMPLETED_KEY, false)

    fun setOnboardingCompleted(completed: Boolean) {
        preferences.edit().putBoolean(IS_ONBOARDING_COMPLETED_KEY, completed).apply()
    }

    companion object {
        private const val PREF_KEY = "studypath_prefs"
        private const val IS_ONBOARDING_COMPLETED_KEY = "is_onboarding_completed"
    }
}
