package com.sayid.studypath.data.remote.response

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(

	@field:SerializedName("data")
	val data: DataRecommendation,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class Extroversion(

	@field:SerializedName("rekomendasi")
	val rekomendasi: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("judul")
	val judul: String,

	@field:SerializedName("skor")
	val skor: Int
)

data class Recommendation(

	@field:SerializedName("conscientiousness")
	val conscientiousness: Conscientiousness,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("openness")
	val openness: Openness,

	@field:SerializedName("neuroticism")
	val neuroticism: Neuroticism,

	@field:SerializedName("extroversion")
	val extroversion: Extroversion,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("agreeableness")
	val agreeableness: Agreeableness
)

data class Agreeableness(

	@field:SerializedName("rekomendasi")
	val rekomendasi: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("judul")
	val judul: String,

	@field:SerializedName("skor")
	val skor: Int
)

data class Neuroticism(

	@field:SerializedName("rekomendasi")
	val rekomendasi: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("judul")
	val judul: String,

	@field:SerializedName("skor")
	val skor: Int
)

data class DataRecommendation(

	@field:SerializedName("recommendation")
	val recommendation: Recommendation
)

data class Conscientiousness(

	@field:SerializedName("rekomendasi")
	val rekomendasi: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("judul")
	val judul: String,

	@field:SerializedName("skor")
	val skor: Int
)

data class Openness(

	@field:SerializedName("rekomendasi")
	val rekomendasi: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("judul")
	val judul: String,

	@field:SerializedName("skor")
	val skor: Int
)
