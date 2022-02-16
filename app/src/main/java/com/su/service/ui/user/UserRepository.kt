package com.su.service.ui.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.su.service.data.source.remote.Api
import com.su.service.model.artikel.ArtikelResponse
import com.su.service.ui.artikel.uploadartikel.UploadArtikelRepository
import com.su.service.utils.Constants
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    val TAG = UserRepository::class.java.simpleName
    val data = MutableLiveData<ArtikelResponse>()
    fun getByUser(apisess: String): LiveData<ArtikelResponse> {
        Api.service.getByUser(Constants.APIKEY,apisess).enqueue(object:
            Callback<ArtikelResponse> {
            override fun onFailure(call: Call<ArtikelResponse>, t: Throwable) {
                Log.d(TAG, "failure: ${call.request()}")
                data.value = null
            }

            override fun onResponse(
                call: Call<ArtikelResponse>,
                response: Response<ArtikelResponse>
            ) {
                Log.d(TAG, "success: ${call.request()}")
                if(response.isSuccessful){
                    data.value = response.body()
                }else{
                    data.value = null
                }
            }

        })
        return data
    }

    fun editArtikel(id:String, apisess: String, title:String, content: String, kategori: String, slug: String, mdescription: String): LiveData<ArtikelResponse> {
        Api.service.editArtikel(id,Constants.APIKEY,apisess,title, content, kategori, slug, mdescription).enqueue(object:
            Callback<ArtikelResponse> {
            override fun onFailure(call: Call<ArtikelResponse>, t: Throwable) {
                Log.d(TAG, "failure: ${call.request()}")
                data.value = null
            }

            override fun onResponse(
                call: Call<ArtikelResponse>,
                response: Response<ArtikelResponse>
            ) {
                Log.d(TAG, "success: ${call.request()}")
                if(response.isSuccessful){
                    data.value = response.body()
                }else{
                    data.value = null
                }
            }

        })
        return data
    }

    fun hapusArtikel(id:String, apisess: String): LiveData<ArtikelResponse> {
        Api.service.hapusArtikel(id,Constants.APIKEY,apisess).enqueue(object:
            Callback<ArtikelResponse> {
            override fun onFailure(call: Call<ArtikelResponse>, t: Throwable) {
                Log.d(TAG, "failure: ${call.request()}")
                data.value = null
            }

            override fun onResponse(
                call: Call<ArtikelResponse>,
                response: Response<ArtikelResponse>
            ) {
                Log.d(TAG, "success: ${call.request()}")
                if(response.isSuccessful){
                    data.value = response.body()
                }else{
                    data.value = null
                }
            }

        })
        return data
    }

    fun setStatus(id: Int, apisess: String?, status: String): LiveData<ArtikelResponse> {
        Api.service.approveStatus(id.toString(),Constants.APIKEY,apisess,status).enqueue(object:
            Callback<ArtikelResponse> {
            override fun onFailure(call: Call<ArtikelResponse>, t: Throwable) {
                Log.d(TAG, "failure: ${call.request()}")
                data.value = null
            }

            override fun onResponse(
                call: Call<ArtikelResponse>,
                response: Response<ArtikelResponse>
            ) {
                Log.d(TAG, "success: ${call.request()}")
                if(response.isSuccessful){
                    data.value = response.body()
                }else{
                    data.value = null
                }
            }

        })
        return data
    }

    fun updateGambarArtikel(id: Int?, apisess: String?, gambar: MultipartBody.Part?): LiveData<ArtikelResponse> {
        Api.service.updateGambarArtikel(id.toString(),Constants.APIKEY,apisess,gambar).enqueue(object:
            Callback<ArtikelResponse> {
            override fun onFailure(call: Call<ArtikelResponse>, t: Throwable) {
                Log.d(TAG, "failure: ${call.request()}")
                data.value = null
            }

            override fun onResponse(
                call: Call<ArtikelResponse>,
                response: Response<ArtikelResponse>
            ) {
                Log.d(TAG, "success: ${call.request()}")
                if(response.isSuccessful){
                    data.value = response.body()
                }else{
                    data.value = null
                }
            }

        })
        return data
    }

    fun bacaArtikel(id: Int?, apisess: String?): LiveData<ArtikelResponse> {
        Api.service.bacaArtikel(id.toString(),Constants.APIKEY,apisess).enqueue(object:
            Callback<ArtikelResponse> {
            override fun onFailure(call: Call<ArtikelResponse>, t: Throwable) {
                Log.d(TAG, "failure: ${call.request()}")
                data.value = null
            }

            override fun onResponse(
                call: Call<ArtikelResponse>,
                response: Response<ArtikelResponse>
            ) {
                Log.d(TAG, "success: ${call.request()}")
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