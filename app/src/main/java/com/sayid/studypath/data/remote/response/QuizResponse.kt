package com.sayid.studypath.data.remote.response

import com.google.gson.annotations.SerializedName

data class QuizResponse(

    @field:SerializedName("data")
    val data: Data,

    @field:SerializedName("status")
    val status: String
)

data class Data(

    @field:SerializedName("quiz")
    val quiz: List<QuizItem>
)

data class QuizItem(

    @field:SerializedName("question_text")
    val questionText: String,

    @field:SerializedName("question_code")
    val questionCode: String
)

data class QuizAnswer(
    @field:SerializedName("question_code")
    val questionCode: String,

    @field:SerializedName("answer_value")
    val answerValue: Int
)

data class QuizAnswerRequest(
    @field:SerializedName("answers")
    val answers: MutableList<QuizAnswer>?
)

data class QuizAnswerResponse(
    @field:SerializedName("status")
    val status: String
)