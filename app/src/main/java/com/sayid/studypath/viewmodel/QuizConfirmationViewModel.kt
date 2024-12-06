package com.sayid.studypath.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayid.studypath.data.Result
import com.sayid.studypath.data.remote.api.ApiConfig
import com.sayid.studypath.data.remote.response.UserPredictionResult
import com.sayid.studypath.data.repository.AuthRepository
import kotlinx.coroutines.launch

class QuizConfirmationViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _userPredictionResult = MutableLiveData<Result<UserPredictionResult>>()
    val userPredictionResult: LiveData<Result<UserPredictionResult>> get() = _userPredictionResult

    init {
        getUserPredictionResult()
    }

    private fun getUserPredictionResult() {
        viewModelScope.launch {
            _userPredictionResult.value = Result.Loading
            try {
                val idToken =
                    authRepository.getIdToken() ?: throw NullPointerException("ID Token is null")
                val response = ApiConfig.getApiService().userPredictionData("Bearer $idToken")

                _userPredictionResult.value = Result.Success(response)
            } catch (e: Exception) {
                _userPredictionResult.value = Result.Error("Gagal: ${e.message}")
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}
