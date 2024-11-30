package com.sayid.studypath.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayid.studypath.data.Result
import com.sayid.studypath.data.remote.api.ApiConfig
import com.sayid.studypath.data.remote.response.UserLoginResponse
import com.sayid.studypath.data.repository.AuthRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EditProfileViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _userResponse = MutableLiveData<Result<UserLoginResponse>>()
    val userResponse: LiveData<Result<UserLoginResponse>> get() = _userResponse

    fun editProfile(
        name: String,
        avatar: File,
    ) {
        viewModelScope.launch {
            _userResponse.value = Result.Loading
            try {
                val idToken =
                    authRepository.getIdToken()
                        ?: throw NullPointerException("ID Token is null")
                val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
                val avatarRequestBody = avatar.asRequestBody("image/*".toMediaTypeOrNull())
                val avatarPart =
                    MultipartBody.Part.createFormData("avatar", avatar.name, avatarRequestBody)
                val response =
                    ApiConfig
                        .getApiService()
                        .editProfile("Bearer $idToken", nameRequestBody, avatarPart)

                _userResponse.value = Result.Success(response)
            } catch (e: Exception) {
                _userResponse.value = Result.Error("Gagal: ${e.message}")
            }
        }
    }

    fun editProfile(
        name: String,
        avatar: String,
    ) {
        viewModelScope.launch {
            _userResponse.value = Result.Loading
            try {
                val idToken =
                    authRepository.getIdToken()
                        ?: throw NullPointerException("ID Token is null")
                val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
                val avatarRequestBody = avatar.toRequestBody("text/plain".toMediaTypeOrNull())
                val response =
                    ApiConfig
                        .getApiService()
                        .editProfile("Bearer $idToken", nameRequestBody, avatarRequestBody)

                _userResponse.value = Result.Success(response)
            } catch (e: Exception) {
                _userResponse.value = Result.Error("Gagal: ${e.message}")
            }
        }
    }
}
