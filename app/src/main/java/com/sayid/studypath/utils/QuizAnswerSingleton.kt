package com.sayid.studypath.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sayid.studypath.data.remote.response.QuizAnswer

object QuizAnswerSingleton {
    private val _listQuizAnswer = MutableLiveData<MutableList<QuizAnswer>>(mutableListOf())
    val listQuizAnswer: LiveData<MutableList<QuizAnswer>> = _listQuizAnswer

    // Fungsi untuk menambah item ke daftar
    fun addQuizAnswer(quizAnswer: QuizAnswer) {
        val currentList = _listQuizAnswer.value ?: mutableListOf()
        currentList.add(quizAnswer)
        _listQuizAnswer.value = currentList
    }

    // Fungsi untuk mendapatkan daftar item
    fun getQuizAnswers(): List<QuizAnswer> {
        return _listQuizAnswer.value ?: emptyList()
    }
}