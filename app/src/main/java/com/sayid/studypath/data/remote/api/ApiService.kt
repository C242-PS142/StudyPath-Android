package com.sayid.studypath.data.remote.api

import com.sayid.studypath.data.remote.response.LoginRequest
import com.sayid.studypath.data.remote.response.QuizAnswerRequest
import com.sayid.studypath.data.remote.response.QuizAnswerResponse
import com.sayid.studypath.data.remote.response.QuizResponse
import com.sayid.studypath.data.remote.response.RegisterResponse
import com.sayid.studypath.data.remote.response.UserLoginResponse
import com.sayid.studypath.data.remote.response.UserPredictionResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface ApiService {
    // GET Quiz
    @GET("api/quiz")
    suspend fun getListQuiz(): QuizResponse

    // Post Quiz after answer it
    @POST("api/quiz")
    suspend fun submitQuizAnswers(
        @Header("Authorization") token: String,
        @Body quizAnswerRequest: QuizAnswerRequest,
    ): QuizAnswerResponse

    // Login with Access Token
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest,
    ): UserLoginResponse

    // Register to the server
    @Multipart
    @POST("auth/register")
    suspend fun register(
        @Part("id") id: RequestBody,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("date_birth") dateBirth: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part avatar: MultipartBody.Part,
    ): RegisterResponse

    @Multipart
    @PUT("auth/edit")
    suspend fun editProfile(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part avatar: MultipartBody.Part,
    ): UserLoginResponse

    @Multipart
    @PUT("auth/edit")
    suspend fun editProfile(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("avatar") avatar: RequestBody,
    ): UserLoginResponse

    @GET("api/predict")
    suspend fun userPredictionData(
        @Header("Authorization") token: String,
    ): UserPredictionResult
}
