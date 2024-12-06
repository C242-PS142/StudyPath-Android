package com.sayid.studypath.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.sayid.studypath.data.remote.response.QuizAnswer

object QuizAnswerSingleton {
    private val _listQuizAnswer = MutableLiveData<MutableList<QuizAnswer>>(mutableListOf())
    val listQuizAnswer: LiveData<List<QuizAnswer>> = _listQuizAnswer.map { it.toList() }

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

    fun clearAllAnswers() {
        _listQuizAnswer.value = mutableListOf()
    }
}
