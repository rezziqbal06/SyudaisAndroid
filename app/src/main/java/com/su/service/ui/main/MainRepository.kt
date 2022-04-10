package com.su.service.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.su.service.data.source.remote.Api
import com.su.service.model.update.UpdateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository{
    val TAG = MainRepository::class.java.simpleName
    fun checkUpdate(
        apikey: String?,
        versionName: String?
    ): MutableLiveData<UpdateResponse>? {
        val data: MutableLiveData<UpdateResponse> = MutableLiveData<UpdateResponse>()
        Api.service.checkUpdate(apikey, versionName)
            ?.enqueue(object : Callback<UpdateResponse> {
                override fun onResponse(
                    call: Call<UpdateResponse>,
                    response: Response<UpdateResponse>
                ) {
                    Log.d(TAG, call.request().toString())
                    if (response.isSuccessful()) {
                        data.postValue(response.body())
                    } else {
                        data.postValue(null)
                    }
                }

                override fun onFailure(
                    call: Call<UpdateResponse>,
                    t: Throwable
                ) {
                    Log.d(TAG, call.request().toString())
                    data.postValue(null)
                }
            })
        return data
    }
}