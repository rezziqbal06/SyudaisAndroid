package com.su.service.ui.artikel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.su.service.data.source.remote.Api
import com.su.service.model.beranda.BerandaResponse
import com.su.service.model.kategori.KategoriResponse
import com.su.service.utils.Constants
import retrofit2.Call
import retrofit2.Response

class KategoriViewModel : ViewModel(){
    val data = MutableLiveData<KategoriResponse>()

    fun getDataKategori(page: String, pagesize: String, kategori: String, keyword:String): LiveData<KategoriResponse> {
        Api.service.getKategoriArtikel(Constants.APIKEY,kategori, page, pagesize, keyword).enqueue(object: retrofit2.Callback<KategoriResponse>{
            override fun onFailure(call: Call<KategoriResponse>, t: Throwable) {
                Log.d("request",call.request().toString())
                data.value = null
            }

            override fun onResponse(
                call: Call<KategoriResponse>,
                response: Response<KategoriResponse>
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