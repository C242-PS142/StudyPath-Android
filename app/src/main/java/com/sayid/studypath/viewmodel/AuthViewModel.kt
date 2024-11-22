package com.sayid.studypath.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.sayid.studypath.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _authResult = MutableLiveData<Result<FirebaseUser>>()
    val authResult: LiveData<Result<FirebaseUser>> = _authResult

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            val result = authRepository.firebaseAuthWithGoogle(idToken)
            _authResult.postValue(result)
        }
    }

    fun getGoogleSignInIntent() = authRepository.getGoogleSignInIntent()

    fun isLoggedIn(): Boolean = authRepository.isLoggedIn()

    fun getCurrentUser(): FirebaseUser? = authRepository.getCurrentUser()

    fun signOut() {
        authRepository.signOut()
    }
}
