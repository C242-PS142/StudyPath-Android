package com.sayid.studypath.data.remote.api

import com.sayid.studypath.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val clientBuilder =
                OkHttpClient
                    .Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) {
                val loggingInterceptor =
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                clientBuilder.addInterceptor(loggingInterceptor)
            }

            val client = clientBuilder.build()

            val retrofit =
                Retrofit
                    .Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}
