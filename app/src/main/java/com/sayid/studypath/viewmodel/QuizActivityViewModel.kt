package com.sayid.studypath.viewmodel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayid.studypath.data.remote.response.Prediction
import com.sayid.studypath.data.remote.response.QuizAnswerRequest
import com.sayid.studypath.data.remote.response.QuizAnswerResponse
import com.sayid.studypath.data.remote.response.QuizItem
import com.sayid.studypath.data.remote.response.QuizResponse
import com.sayid.studypath.data.repository.QuizRepository
import com.sayid.studypath.ui.activity.MainActivity
import kotlinx.coroutines.launch

class QuizActivityViewModel(private val quizRepository: QuizRepository) : ViewModel() {
    private val _listQuiz = MutableLiveData<Result<QuizResponse>>()

    private val _listQuizAnswerResponse = MutableLiveData<Result<QuizAnswerResponse>>()
    val listQuizAnswerResponse: LiveData<Result<QuizAnswerResponse>> = _listQuizAnswerResponse

    private val _listPrediction = MutableLiveData<Prediction>()
    val listPrediction: LiveData<Prediction> = _listPrediction

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

    private fun divideIntoStages(answers: List<QuizItem>): List<List<QuizItem>> {
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
        viewModelScope.launch {
            try {
                val response: Result<QuizResponse> = quizRepository.getListQuiz()
                _listQuiz.value = response

                response.getOrNull()?.let { quizResponse ->
                    val stages: List<List<QuizItem>> = divideIntoStages(quizResponse.data.quiz)
                    _quizStage1.value = stages[0]
                    _quizStage2.value = stages[1]
                    _quizStage3.value = stages[2]
                    _quizStage4.value = stages[3]
                    _quizStage5.value = stages[4]
                }
            } catch (e: Exception) {
                _listQuiz.value = Result.failure(e)
            }
        }
    }

    fun postQuizAnswers(idToken: String, listAnswers: QuizAnswerRequest) {
        viewModelScope.launch {
            try {
                val response = quizRepository.postQuizAnswers(idToken, listAnswers)
                _listQuizAnswerResponse.value = response

                response.getOrNull()?.let { data ->
                    if(data.status == "success"){
                        Log.d("Result Prediction", data.data.prediction.toString())
                        _listPrediction.value = data.data.prediction
                    }
                }
            } catch (e: Exception) {
                _listQuizAnswerResponse.value = Result.failure(e)
            }
        }
    }
}