/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.su.service.data.source.remote

import android.util.Log
import com.su.service.BuildConfig
import com.su.service.model.dzikir.Dzikir
import com.su.service.model.dzikir.DzikirResponse
import com.su.service.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val TAG = "DzikirService"

/**
 * Search repos based on a query.
 * Trigger a request to the Github searchRepo API with the following params:
 * @param query searchRepo keyword
 * @param page request page index
 * @param itemsPerPage number of repositories to be returned by the Github API per page
 *
 * The result of the request is handled by the implementation of the functions passed as params
 * @param onSuccess function that defines how to handle the list of repos received
 * @param onError function that defines how to handle request failure
 */
fun queryDzikir(
    service: DzikirService,
    query: String,
    page: String,
    itemsPerPage: String,
    onSuccess: (repos: List<Dzikir>) -> Unit,
    onError: (error: String) -> Unit
) {
    Log.d(TAG, "query: page: $page, query $query itemsPerPage: $itemsPerPage")
    service.queryDzikir(Constants.APIKEY, "", page, itemsPerPage).enqueue(
        object : Callback<DzikirResponse> {
            override fun onFailure(call: Call<DzikirResponse>?, t: Throwable) {
                Log.d(TAG, "fail to get data")
                onError(t.message ?: "unknown error")
            }

            override fun onResponse(
                call: Call<DzikirResponse>?,
                response: Response<DzikirResponse>
            ) {
                Log.d(TAG, "got a response $response")
                if (response.isSuccessful) {
                    val repos = response.body()?.result?.dzikir?: emptyList()
                    onSuccess(repos)
                } else {
                    onError(response.errorBody()?.string() ?: "Unknown error")
                }
            }
        }
    )
}

/**
 * Github API communication setup via Retrofit.
 */
interface DzikirService {
    /**
     * Get repos ordered by stars.
     */

    @GET("dzikir/")
    fun queryDzikir(
        @Query("apikey") apikey: String,
        @Query("keyword") query:String,
        @Query("page") page:String,
        @Query("pagesize") pagesize:String
    ): Call<DzikirResponse>

    companion object {

        fun create(): DzikirService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
            return Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(DzikirService::class.java)
        }
    }
}