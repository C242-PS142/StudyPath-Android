package com.sayid.studypath.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.sayid.studypath.data.remote.api.ApiConfig
import com.sayid.studypath.data.remote.response.LoginRequest
import com.sayid.studypath.data.remote.response.UserLoginResponse
import com.sayid.studypath.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _userResponse = MutableLiveData<com.sayid.studypath.data.Result<UserLoginResponse>>()
    val userResponse: LiveData<com.sayid.studypath.data.Result<UserLoginResponse>> get() = _userResponse

    private val _authResult = MutableLiveData<Result<FirebaseUser>?>()
    val authResult: LiveData<Result<FirebaseUser>?> = _authResult

    private val _idToken = MutableLiveData<String?>()
    val idToken: LiveData<String?> = _idToken

    init {
        getIdToken()
    }

    fun login() {
        viewModelScope.launch {
            _userResponse.value = com.sayid.studypath.data.Result.Loading
            val idToken = authRepository.getIdToken()
            try {
                if (idToken == null) throw NullPointerException("Value is null")

                val response = ApiConfig.getApiService().login(LoginRequest(idToken))
                _userResponse.value =
                    com.sayid.studypath.data.Result
                        .Success(response)
            } catch (e: Exception) {
                _userResponse.value =
                    com.sayid.studypath.data.Result
                        .Error("Gagal: ${e.message}")
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authResult.value = null
            try {
                val response = authRepository.firebaseAuthWithGoogle(idToken)
                _authResult.value = response
            } catch (e: Exception) {
                _authResult.value = Result.failure(e)
            }
        }
    }

    fun getGoogleSignInIntent() = authRepository.getGoogleSignInIntent()

    fun getCurrentUser(): FirebaseUser? = authRepository.getCurrentUser()

    private fun getIdToken() {
        viewModelScope.launch {
            val token = authRepository.getIdToken()
            _idToken.value = token
            if (token != null) {
                login()
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}
