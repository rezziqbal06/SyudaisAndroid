package com.su.service.ui.beranda

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.su.service.model.beranda.BerandaResponse
import com.su.service.data.source.remote.Api
import com.su.service.model.pelanggan.PelangganResponse
import com.su.service.utils.Constants
import retrofit2.Call
import retrofit2.Response

class BerandaViewModel : ViewModel() {
    private var context: Context? = null
    val data = MutableLiveData<BerandaResponse>()
    fun getContext(context: Context){
        this.context = context
    }
    fun getDataBeranda(): LiveData<BerandaResponse> {
        Api.service.beranda(Constants.APIKEY).enqueue(object: retrofit2.Callback<BerandaResponse>{
            override fun onFailure(call: Call<BerandaResponse>, t: Throwable) {
                Log.d("request",call.request().toString())
                data.value = null
            }

            override fun onResponse(
                call: Call<BerandaResponse>,
                response: Response<BerandaResponse>
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

    fun getUser(apisess: String): LiveData<PelangganResponse> {
        val data = MutableLiveData<PelangganResponse>()
        Api.service.getUser(Constants.APIKEY, apisess).enqueue(object: retrofit2.Callback<PelangganResponse>{
            override fun onFailure(call: Call<PelangganResponse>, t: Throwable) {
                Log.d("request",call.request().toString())
                data.value = null
            }

            override fun onResponse(
                call: Call<PelangganResponse>,
                response: Response<PelangganResponse>
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
}