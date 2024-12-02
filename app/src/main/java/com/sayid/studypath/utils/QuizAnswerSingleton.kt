package com.sayid.studypath.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sayid.studypath.data.remote.response.QuizAnswer

object QuizAnswerSingleton {
    private val _listQuizAnswer = MutableLiveData<MutableList<QuizAnswer>>(mutableListOf())
    val listQuizAnswer: LiveData<MutableList<QuizAnswer>> = _listQuizAnswer

    // Fungsi untuk menambah item ke daftar
    fun addQuizAnswer(quizAnswer: QuizAnswer) {
        val currentList = _listQuizAnswer.value ?: mutableListOf()
        Log.d("QUIZ ANSWER", "Current list before addition: $currentList")
        if (!currentList.contains(quizAnswer)) {
            currentList.add(quizAnswer)
            _listQuizAnswer.value = currentList
            Log.d("QUIZ ANSWER", "Added: $quizAnswer")
        } else {
            Log.d("QUIZ ANSWER", "Duplicate not added: $quizAnswer")
        }
        Log.d("QUIZ ANSWER", "Updated list after addition: ${_listQuizAnswer.value}")
    }

    // Fungsi untuk mendapatkan daftar item
    fun getQuizAnswers(): List<QuizAnswer> = _listQuizAnswer.value ?: emptyList()
}
