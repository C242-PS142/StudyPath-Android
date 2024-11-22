package com.sayid.studypath.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sayid.studypath.data.remote.api.ApiConfig
import com.sayid.studypath.data.remote.response.QuizAnswerRequest
import com.sayid.studypath.data.remote.response.QuizAnswerResponse
import com.sayid.studypath.data.remote.response.QuizItem
import com.sayid.studypath.data.remote.response.QuizResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizActivityViewModel: ViewModel() {
    private val _listQuiz = MutableLiveData<QuizResponse>()
    val listQuiz: LiveData<QuizResponse> = _listQuiz

    private val _quizStage1 = MutableLiveData<List<QuizItem>>()
    val quizStage1: LiveData<List<QuizItem>> = _quizStage1

    private val _quizStage2 = MutableLiveData<List<QuizItem>>()
    val quizStage2: LiveData<List<QuizItem>> = _quizStage2

    private val _quizStage3 = MutableLiveData<List<QuizItem>>()
    val quizStage3: LiveData<List<QuizItem>> = _quizStage3

    private val _quizStage4 = MutableLiveData<List<QuizItem>>()
    val quizStage4: LiveData<List<QuizItem>> = _quizStage4

    private val _quizStage5 = MutableLiveData<List<QuizItem>>()
    val quizStage5: LiveData<List<QuizItem>> = _quizStage5

    init {
        getListQuiz()
    }

    fun divideIntoStages(answers: List<QuizItem>): List<List<QuizItem>> {
        // Hitung ukuran setiap stage
        val stageSize = answers.size / 5
        val remainder = answers.size % 5

        // Variabel untuk menyimpan hasil pembagian stage
        val stages = mutableListOf<List<QuizItem>>()

        var startIndex = 0

        for (i in 1..5) {
            val endIndex = startIndex + stageSize + if (i <= remainder) 1 else 0
            stages.add(answers.subList(startIndex, endIndex))
            startIndex = endIndex
        }

        return stages
    }

    private fun getListQuiz() {
        val client = ApiConfig.getApiService().getListQuiz()

        client.enqueue(object : Callback<QuizResponse> {
            override fun onResponse(call: Call<QuizResponse>, response: Response<QuizResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful) {
                    _listQuiz.value = responseBody!!

                    // Bagi menjadi 5 stage
                    val stages = divideIntoStages(responseBody.data.quiz)
                    _quizStage1.value = stages[0]
                    _quizStage2.value = stages[1]
                    _quizStage3.value = stages[2]
                    _quizStage4.value = stages[3]
                    _quizStage5.value = stages[4]

                } else {
                    Log.e("QuizModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<QuizResponse>, t: Throwable) {
                Log.e("QuizModel", "onFailure: ${t.message}")
            }
        })
    }

    fun postQuizAnswers(listAnswers: QuizAnswerRequest){
//        val quizAnswers = QuizAnswerRequest(
//            answers = listOf(
//                QuizAnswer("EXT1",  4),
//            )
//        )
        val client = ApiConfig.getApiService().submitQuizAnswers(listAnswers)

        client.enqueue(object: Callback<QuizAnswerResponse>{
            override fun onResponse(
                call: Call<QuizAnswerResponse>,
                response: Response<QuizAnswerResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("QuizAnswerModel", "onSuccess: ${response.message()}")
                } else {
                    Log.e("QuizAnswerModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<QuizAnswerResponse>, t: Throwable) {
                Log.e("QuizAnswerModel", "onFailure: ${t.message}")
            }
        })
    }
}