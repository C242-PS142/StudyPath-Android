package com.sayid.studypath.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sayid.studypath.data.Result
import com.sayid.studypath.data.model.LocalPreferences
import com.sayid.studypath.data.remote.api.ApiConfig
import com.sayid.studypath.data.remote.response.RecommendationResponse
import com.sayid.studypath.data.remote.response.UserPredictionResult
import com.sayid.studypath.data.repository.AuthRepository
import com.sayid.studypath.utils.PredictionResultSingleton
import kotlinx.coroutines.launch

class MainViewModel(
    localPreferences: LocalPreferences,
    private val authRepository: AuthRepository,
    ) : ViewModel() {
    val isDarkTheme: LiveData<Boolean?> = localPreferences.isDarkTheme.asLiveData()

    private val _userPredictionResultResponse = MutableLiveData<Result<UserPredictionResult>>()
    val userPredictionResultResponse: LiveData<Result<UserPredictionResult>> get() = _userPredictionResultResponse

    private val _recommendationResponse = MutableLiveData<Result<RecommendationResponse>>()
    val recommendationResponse: LiveData<Result<RecommendationResponse>> get() = _recommendationResponse

    init {
        getPredict()
    }

    private fun getPredict(){
        viewModelScope.launch {
            _userPredictionResultResponse.value = Result.Loading
            val idToken = authRepository.getIdToken()
            try {
                if (idToken == null) throw NullPointerException("Value is null")
                val response =
                    ApiConfig.getApiService().userPredictionData("Bearer $idToken")

                PredictionResultSingleton.updatePrediction(response.data.personality)

                _userPredictionResultResponse.value = Result.Success(response)
            } catch (e: Exception) {
                _userPredictionResultResponse.value = Result.Error("Gagal: ${e.message}")
            }
        }
    }

    fun getRecommendation(){
        viewModelScope.launch {
            _recommendationResponse.value = Result.Loading
            val idToken = authRepository.getIdToken()
            try {
                if (idToken == null) throw NullPointerException("Value is null")
                val response =
                    ApiConfig.getApiService().getRecommendation("Bearer $idToken")

                _recommendationResponse.value = Result.Success(response)
            } catch (e: Exception) {
                _recommendationResponse.value = Result.Error("Gagal: ${e.message}")
            }
        }
    }
}
