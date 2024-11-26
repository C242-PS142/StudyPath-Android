package com.sayid.studypath.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("access_token") val accessToken: String,
)

data class UserResponse(
    @field:SerializedName("data")
    val data: UserData,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("status")
    val status: String,
)

data class UserData(
    @field:SerializedName("uid")
    val uid: String,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("avatar")
    val avatar: String,
    @field:SerializedName("email")
    val email: String,
)
