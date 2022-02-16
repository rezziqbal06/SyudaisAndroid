package com.su.service.ui.doadzikir

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.su.service.data.source.remote.Api
import com.su.service.model.doa.DoaResponse
import com.su.service.model.dzikir.DzikirResponse
import com.su.service.utils.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoaDzikirRepository {
    val TAG = DoaDzikirRepository::class.java.simpleName
    val doa = MutableLiveData<DoaResponse>()
    val dzikir = MutableLiveData<DzikirResponse>()
    fun uploadDoa(apisess:String,  text: Map<String,RequestBody>, gambar: MultipartBody.Part ) : LiveData<DoaResponse>{
        Api.service.uploadDoa(Constants.APIKEY, apisess,gambar, text).enqueue(object:
            Callback<DoaResponse> {
            override fun onFailure(call: Call<DoaResponse>, t: Throwable) {
                Log.d(TAG, call.request().toString())
                doa.value = null
            }

            override fun onResponse(call: Call<DoaResponse>, response: Response<DoaResponse>) {
                Log.d(TAG, call.request().toString())
                if(response.isSuccessful){
                    doa.value = response.body()
                }else{
                    doa.value = null
                }
            }

        })
        return doa
    }

    fun editDoa(id: String?, apisess:String?,  text: Map<String,RequestBody>, gambar: MultipartBody.Part? ) : LiveData<DoaResponse>{
        Api.service.editDoa(id, Constants.APIKEY, apisess,gambar, text).enqueue(object:
            Callback<DoaResponse> {
            override fun onFailure(call: Call<DoaResponse>, t: Throwable) {
                Log.d(TAG, call.request().toString())
                doa.value = null
            }

            override fun onResponse(call: Call<DoaResponse>, response: Response<DoaResponse>) {
                Log.d(TAG, call.request().toString())
                if(response.isSuccessful){
                    doa.value = response.body()
                }else{
                    doa.value = null
                }
            }

        })
        return doa
    }

    fun hapusDoa(id: String, apisess:String) : LiveData<DoaResponse>{
        Api.service.hapusDoa(id, Constants.APIKEY, apisess).enqueue(object:
            Callback<DoaResponse> {
            override fun onFailure(call: Call<DoaResponse>, t: Throwable) {
                Log.d(TAG, call.request().toString())
                doa.value = null
            }

            override fun onResponse(call: Call<DoaResponse>, response: Response<DoaResponse>) {
                Log.d(TAG, call.request().toString())
                if(response.isSuccessful){
                    doa.value = response.body()
                }else{
                    doa.value = null
                }
            }

        })
        return doa
    }
    fun uploadDzikir(apisess:String,  text: Map<String,RequestBody>, gambar: MultipartBody.Part ) : LiveData<DzikirResponse>{
        Api.service.uploadDzikir(Constants.APIKEY, apisess,gambar, text).enqueue(object:
            Callback<DzikirResponse> {
            override fun onFailure(call: Call<DzikirResponse>, t: Throwable) {
                Log.d(TAG, call.request().toString())
                dzikir.value = null
            }

            override fun onResponse(call: Call<DzikirResponse>, response: Response<DzikirResponse>) {
                Log.d(TAG, call.request().toString())
                if(response.isSuccessful){
                    dzikir.value = response.body()
                }else{
                    dzikir.value = null
                }
            }

        })
        return dzikir
    }

    fun editDzikir(id: String, apisess:String,  text: Map<String,RequestBody>, gambar: MultipartBody.Part ) : LiveData<DzikirResponse>{
        Api.service.editDzikir(id, Constants.APIKEY, apisess,gambar, text).enqueue(object:
            Callback<DzikirResponse> {
            override fun onFailure(call: Call<DzikirResponse>, t: Throwable) {
                Log.d(TAG, call.request().toString())
                dzikir.value = null
            }

            override fun onResponse(call: Call<DzikirResponse>, response: Response<DzikirResponse>) {
                Log.d(TAG, call.request().toString())
                if(response.isSuccessful){
                    dzikir.value = response.body()
                }else{
                    dzikir.value = null
                }
            }

        })
        return dzikir
    }

    fun hapusDzikir(id: String, apisess:String) : LiveData<DzikirResponse>{
        Api.service.hapusDzikir(id, Constants.APIKEY, apisess).enqueue(object:
            Callback<DzikirResponse> {
            override fun onFailure(call: Call<DzikirResponse>, t: Throwable) {
                Log.d(TAG, call.request().toString())
                dzikir.value = null
            }

            override fun onResponse(call: Call<DzikirResponse>, response: Response<DzikirResponse>) {
                Log.d(TAG, call.request().toString())
                if(response.isSuccessful){
                    dzikir.value = response.body()
                }else{
                    dzikir.value = null
                }
            }

        })
        return dzikir
    }
}