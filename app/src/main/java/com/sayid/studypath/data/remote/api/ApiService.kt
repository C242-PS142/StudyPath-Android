package com.sayid.studypath.data.remote.api

import com.sayid.studypath.data.remote.response.LoginRequest
import com.sayid.studypath.data.remote.response.QuizAnswerRequest
import com.sayid.studypath.data.remote.response.QuizAnswerResponse
import com.sayid.studypath.data.remote.response.QuizResponse
import com.sayid.studypath.data.remote.response.RegisterResponse
import com.sayid.studypath.data.remote.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    // GET Quiz
    @GET("api/quiz")
    fun getListQuiz(): Call<QuizResponse>

    // Post Quiz after answer it
    @POST("api/quiz")
    fun submitQuizAnswers(
        @Body quizAnswerRequest: QuizAnswerRequest,
    ): Call<QuizAnswerResponse>

    // Login with Access Token
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest,
    ): UserResponse

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
}
