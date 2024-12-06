package com.sayid.studypath.data.remote.response

import com.google.gson.annotations.SerializedName

// GET QUIZ
data class QuizResponse(
    @field:SerializedName("data")
    val data: Data,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("status")
    val status: String,
)

data class Data(
    @field:SerializedName("quiz")
    val quiz: List<QuizItem>,
)

data class QuizItem(
    @field:SerializedName("question_text_id")
    val questionTextID: String,
    @field:SerializedName("question_text_en")
    val questionTextEN: String,
    @field:SerializedName("question_code")
    val questionCode: String,
)

// POST QUIZ
data class QuizAnswer(
    @field:SerializedName("question_code")
    val questionCode: String,
    @field:SerializedName("answer_value")
    val answerValue: Int,
)

data class QuizAnswerRequest(
    @field:SerializedName("answers")
    val answers: List<QuizAnswer>,
)

data class QuizAnswerResponse(
    @field:SerializedName("status")
    val status: String,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("data")
    val data: DataResult,
)

data class DataResult(
    @field:SerializedName("prediction")
    val prediction: Prediction,
)

data class UserPredictionResult(
    @field:SerializedName("status")
    val status: String,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("data")
    val data: Personality,
)

data class Personality(
    @field:SerializedName("personality")
    val personality: Prediction,
)

data class Prediction(
    @field:SerializedName("Ketelitian")
    val ketelitian: Float,
    @field:SerializedName("Kestabilan Emosi")
    val kestabilanEmosi: Float,
    @field:SerializedName("Keterbukaan terhadap Pengalaman")
    val keterbukaanTerhadapPengalaman: Float,
    @field:SerializedName("Kesepakatan")
    val kesepakatan: Float,
    @field:SerializedName("Keterbukaan Sosial, Energi, dan Antusiasme")
    val keterbukaanSosialEnergiDanAntusiasme: Float,
)
