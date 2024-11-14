package com.sayid.studypath.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sayid.studypath.data.model.LoginPreference

class LoginViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val loginPreference = LoginPreference(application)

    private val _accessToken = MutableLiveData<String?>()
    val accessToken: LiveData<String?> get() = _accessToken

    init {
        loadAccessToken()
    }

    fun saveAccessToken(token: String) {
        loginPreference.saveAccessToken(token)
        _accessToken.value = token
    }

    private fun loadAccessToken() {
        _accessToken.value = loginPreference.getAccessToken()
    }

    fun clearAccessToken() {
        loginPreference.clearAccessToken()
        _accessToken.value = null
    }
}
