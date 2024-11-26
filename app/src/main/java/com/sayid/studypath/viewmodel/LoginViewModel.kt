package com.sayid.studypath.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.sayid.studypath.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _authResult = MutableLiveData<Result<FirebaseUser>?>()
    val authResult: LiveData<Result<FirebaseUser>?> = _authResult

    private val _idToken = MutableLiveData<String?>()
    val idToken: LiveData<String?> = _idToken

    init {
        getIdToken()
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
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}
