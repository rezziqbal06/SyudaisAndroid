package com.su.service.ui.video

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.su.service.model.beranda.BerandaResponse
import com.su.service.data.source.remote.Api
import com.su.service.model.youtube.YoutubeResponse
import com.su.service.utils.Constants
import retrofit2.Call
import retrofit2.Response

class VideoViewModel : ViewModel() {
    private var context: Context? = null
    val data = MutableLiveData<YoutubeResponse>()
    val token = MutableLiveData<String>()
    fun getContext(context: Context){
        this.context = context
    }
    fun getData(pageToken: String?): LiveData<YoutubeResponse> {
        Api.youtubeService.youtubeChannel(Constants.google_apikey,Constants.channelId,"10",pageToken).enqueue(object: retrofit2.Callback<YoutubeResponse>{
            override fun onFailure(call: Call<YoutubeResponse>, t: Throwable) {
                Log.d("request",call.request().toString())
                data.value = null
            }

            override fun onResponse(
                call: Call<YoutubeResponse>,
                response: Response<YoutubeResponse>
            ) {
                Log.d("request",call.request().toString())
                if(response.isSuccessful){
                    data.value = response.body()
                }else{
                    data.value = null
                }
            }
        })
        return data
    }

    fun calculateTotalPage(produkCount: String): LiveData<Int>{
        val result = MutableLiveData<Int>()
        var hasilPembagian: Int = produkCount.toInt().div(12)
        val sisa: Int = produkCount.toInt().rem(12)
        if (hasilPembagian != 0) {
            if (sisa > 0) {
                hasilPembagian++
            }
        }
        result.setValue(hasilPembagian)
        return result
    }
    fun setToken(token: String){
        this.token.value = token
    }

    fun getToken(): LiveData<String>{
        return token
    }
}