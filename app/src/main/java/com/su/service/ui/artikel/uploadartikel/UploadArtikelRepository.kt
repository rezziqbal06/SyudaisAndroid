package com.su.service.ui.artikel.uploadartikel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.su.service.data.source.remote.Api
import com.su.service.model.artikel.ArtikelResponse
import com.su.service.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadArtikelRepository {
    val TAG = UploadArtikelRepository::class.java.simpleName
    val data = MutableLiveData<ArtikelResponse>()
    fun uploadArtikel(apisess: String, title: String, content: String, kategori: String, slug: String, mdescription: String): LiveData<ArtikelResponse>{
        Api.service.uploadArtikel(Constants.APIKEY,apisess,title, content, kategori,slug,mdescription).enqueue(object:
            Callback<ArtikelResponse>{
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