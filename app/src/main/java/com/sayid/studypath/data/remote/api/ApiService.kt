package com.sayid.studypath.data.remote.api

import com.sayid.studypath.data.remote.response.LoginRequest
import com.sayid.studypath.data.remote.response.QuizAnswerRequest
import com.sayid.studypath.data.remote.response.QuizAnswerResponse
import com.sayid.studypath.data.remote.response.QuizResponse
import com.sayid.studypath.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    // GET Quiz
    @GET("quiz")
    fun getListQuiz(): Call<QuizResponse>

    // Post Quiz after answer it
    @POST("quiz")
    fun submitQuizAnswers(
        @Body quizAnswerRequest: QuizAnswerRequest,
    ): Call<QuizAnswerResponse>

    // Login with Access Token
    @POST("login")
    suspend fun login(
        @Body request: LoginRequest,
    ): UserResponse
}
