package com.sayid.studypath.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("accessToken")
    val accessToken: String,
)

data class UserLoginResponse(
    @field:SerializedName("status")
    val status: String,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("data")
    val data: LoginData,
)

data class LoginData(
    @field:SerializedName("isRegister")
    val isRegister: Boolean,
    @field:SerializedName("isAnswerQuiz")
    val isAnswerQuiz: Boolean,
    @field:SerializedName("result")
    val result: UserLoginData,
)

data class UserLoginData(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("email")
    val email: String,
    @field:SerializedName("date_birth")
    val dateOfBirth: String,
    @field:SerializedName("gender")
    val gender: String,
    @field:SerializedName("avatar")
    val avatar: String,
    @field:SerializedName("created_at")
    val createdAt: String,
)

data class RegisterResponse(
    @field:SerializedName("data")
    val data: UserDataObject,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("status")
    val status: String,
)

data class UserData(
    @field:SerializedName("date_birth")
    val dateBirth: String,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("avatar")
    val avatar: String,
    @field:SerializedName("email")
    val email: String,
)

data class UserDataObject(
    @field:SerializedName("user")
    val user: UserData,
)
