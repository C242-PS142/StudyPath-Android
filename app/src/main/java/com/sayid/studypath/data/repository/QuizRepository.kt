package com.sayid.studypath.data.repository

import com.sayid.studypath.data.remote.api.ApiConfig
import com.sayid.studypath.data.remote.response.QuizAnswerRequest
import com.sayid.studypath.data.remote.response.QuizAnswerResponse
import com.sayid.studypath.data.remote.response.QuizResponse

class QuizRepository {
    suspend fun getListQuiz(): Result<QuizResponse> =
        try {
            val getListQuiz = ApiConfig.getApiService().getListQuiz()

            if (getListQuiz.status == "success") {
                Result.success(getListQuiz)
            } else {
                Result.failure(Exception(getListQuiz.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun postQuizAnswers(idToken: String, listAnswers: QuizAnswerRequest): Result<QuizAnswerResponse> =
        try {
            val postQuizAnswers = ApiConfig.getApiService().submitQuizAnswers("Bearer $idToken", listAnswers)

            if(postQuizAnswers.status == "success"){
                Result.success(postQuizAnswers)
            }else{
                Result.failure(Exception(postQuizAnswers.message))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
}