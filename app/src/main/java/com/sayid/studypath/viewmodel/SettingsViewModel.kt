package com.sayid.studypath.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sayid.studypath.data.Result
import com.sayid.studypath.data.model.LocalPreferences
import com.sayid.studypath.data.remote.api.ApiConfig
import com.sayid.studypath.data.remote.response.LoginRequest
import com.sayid.studypath.data.repository.AuthRepository
import com.sayid.studypath.utils.UserLoginDataSingleton
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val authRepository: AuthRepository,
    private val localPreferences: LocalPreferences,
) : ViewModel() {
    val isDarkTheme: LiveData<Boolean?> = localPreferences.isDarkTheme.asLiveData()

    val isReminderSet: LiveData<Boolean> = localPreferences.isReminderSet.asLiveData()

    init {
        getUserData()
    }

    fun getUserData() {
        viewModelScope.launch {
            UserLoginDataSingleton.updateLoginData(Result.Loading)
            val idToken = authRepository.getIdToken()
            try {
                if (idToken == null) throw NullPointerException("Value is null")
                val response = ApiConfig.getApiService().login(LoginRequest(idToken))
                UserLoginDataSingleton.updateLoginData(Result.Success(response.data.result))
            } catch (e: Exception) {
                UserLoginDataSingleton.updateLoginData(
                    Result
                        .Error("Gagal: ${e.message}"),
                )
            }
        }
    }

    fun setDarkTheme(active: Boolean) {
        viewModelScope.launch {
            localPreferences.setDarkTheme(active)
        }
    }

    fun setReminder(active: Boolean) {
        viewModelScope.launch {
            localPreferences.setReminder(active)
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}
