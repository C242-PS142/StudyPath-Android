package com.sayid.studypath.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayid.studypath.data.Result
import com.sayid.studypath.data.remote.api.ApiConfig
import com.sayid.studypath.data.remote.response.LoginRequest
import com.sayid.studypath.data.remote.response.RegisterResponse
import com.sayid.studypath.data.remote.response.UserResponse
import com.sayid.studypath.data.repository.AuthRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class NewUserDataViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _userResponse = MutableLiveData<Result<UserResponse>>()
    val userResponse: LiveData<Result<UserResponse>> get() = _userResponse

    private val _registerResponse = MutableLiveData<Result<RegisterResponse>>()
    val registerResponse: LiveData<Result<RegisterResponse>> get() = _registerResponse

    init {
        login()
    }

    private fun login() {
        viewModelScope.launch {
            _userResponse.value = Result.Loading

            val idToken = authRepository.getIdToken()

            try {
                if (idToken == null) throw NullPointerException("Value is null")

                val response = ApiConfig.getApiService().login(LoginRequest(idToken))
                _userResponse.value = Result.Success(response)
            } catch (e: Exception) {
                _userResponse.value = Result.Error("Gagal: ${e.message}")
            }
        }
    }

    @Suppress("NAME_SHADOWING")
    fun register(
        name: String,
        dateBirth: String,
        gender: String,
        avatar: File,
    ) {
        viewModelScope.launch {
            _registerResponse.value = Result.Loading
            val firebaseUser = authRepository.getCurrentUser()

            val uid = firebaseUser!!.uid.toRequestBody("text/plain".toMediaTypeOrNull())
            val name = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val email = firebaseUser.email!!.toRequestBody("text/plain".toMediaTypeOrNull())
            val dateBirth = dateBirth.toRequestBody("text/plain".toMediaTypeOrNull())
            val gender = gender.toRequestBody("text/plain".toMediaTypeOrNull())
            val requestFile = avatar.asRequestBody("image/*".toMediaTypeOrNull())
            val avatarPart = MultipartBody.Part.createFormData("avatar", avatar.name, requestFile)

            try {
                val response =
                    ApiConfig.getApiService().register(
                        id = uid,
                        name = name,
                        email = email,
                        dateBirth = dateBirth,
                        gender = gender,
                        avatar = avatarPart,
                    )
                _registerResponse.value = Result.Success(response)
            } catch (e: Exception) {
                _registerResponse.value = Result.Error("Gagal: ${e.message}")
            }
        }
    }
}
