package com.su.service.data.source.remote

import com.google.gson.GsonBuilder
import com.su.service.BuildConfig
import com.su.service.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Api {
    companion object{
        val gson = GsonBuilder().create()
        val client = OkHttpClient()
        private val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val service = retrofit.create(ApiService::class.java)

        private val youtubeRetrofit = Retrofit.Builder()
            .baseUrl(Constants.YOUTUBE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val youtubeService = youtubeRetrofit.create(ApiService::class.java)
    }


}