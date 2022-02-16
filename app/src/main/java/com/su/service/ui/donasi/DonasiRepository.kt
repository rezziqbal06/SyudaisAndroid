package com.su.service.ui.donasi

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.su.service.data.source.remote.Api
import com.su.service.model.detaildonasi.DetailDonasiResponse
import com.su.service.model.donasi.DonasiResponse
import com.su.service.model.update.UpdateResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DonasiRepository{
    val TAG = DonasiRepository::class.java.simpleName
    fun getDonasi(
        apikey: String?,
        page: String?,
        pagesize: String?,
        keyword: String?
    ): MutableLiveData<DonasiResponse>? {
        val data: MutableLiveData<DonasiResponse> = MutableLiveData<DonasiResponse>()
        Api.service.getDonasi(apikey, page, pagesize, keyword)
            .enqueue(object : Callback<DonasiResponse> {
                override fun onResponse(
                    call: Call<DonasiResponse>,
                    response: Response<DonasiResponse>
                ) {
                    Log.d(TAG, call.request().toString())
                    if (response.isSuccessful()) {
                        data.postValue(response.body())
                    } else {
                        data.postValue(null)
                    }
                }

                override fun onFailure(
                    call: Call<DonasiResponse>,
                    t: Throwable
                ) {
                    Log.d(TAG, call.request().toString())
                    data.postValue(null)
                }
            })
        return data
    }

    fun getDetailDonasi(
        id: String?,
        apikey: String?
    ): MutableLiveData<DetailDonasiResponse>? {
        val data: MutableLiveData<DetailDonasiResponse> = MutableLiveData<DetailDonasiResponse>()
        Api.service.detailDonasi(id,apikey)
            .enqueue(object : Callback<DetailDonasiResponse> {
                override fun onResponse(
                    call: Call<DetailDonasiResponse>,
                    response: Response<DetailDonasiResponse>
                ) {
                    Log.d(TAG, call.request().toString())
                    if (response.isSuccessful()) {
                        data.postValue(response.body())
                    } else {
                        data.postValue(null)
                    }
                }

                override fun onFailure(
                    call: Call<DetailDonasiResponse>,
                    t: Throwable
                ) {
                    Log.d(TAG, call.request().toString())
                    data.postValue(null)
                }
            })
        return data
    }

    fun postDonasi(
        apikey: String?,
        apisess: String?,
        gambar1: MultipartBody.Part?,
        gambar2: MultipartBody.Part?,
        gambar3: MultipartBody.Part?,
        isi: Map<String, RequestBody>?
    ): MutableLiveData<DonasiResponse>? {
        val data: MutableLiveData<DonasiResponse> = MutableLiveData<DonasiResponse>()
        Api.service.postDonasi(apikey, apisess, gambar1, isi)
            .enqueue(object : Callback<DonasiResponse> {
                override fun onResponse(
                    call: Call<DonasiResponse>,
                    response: Response<DonasiResponse>
                ) {
                    Log.d(TAG, call.request().toString())
                    if (response.isSuccessful()) {
                        data.postValue(response.body())
                    } else {
                        data.postValue(null)
                    }
                }

                override fun onFailure(
                    call: Call<DonasiResponse>,
                    t: Throwable
                ) {
                    Log.d(TAG, call.request().toString())
                    Log.d(TAG, t.message)
                    data.postValue(null)
                }
            })
        return data
    }

    fun editDonasi(
        id: String?,
        apikey: String?,
        apisess: String?,
        isi: Map<String, RequestBody>?
    ): MutableLiveData<DonasiResponse>? {
        val data: MutableLiveData<DonasiResponse> = MutableLiveData<DonasiResponse>()
        Api.service.editDonasi(id,apikey, apisess, isi)
            .enqueue(object : Callback<DonasiResponse> {
                override fun onResponse(
                    call: Call<DonasiResponse>,
                    response: Response<DonasiResponse>
                ) {
                    Log.d(TAG, call.request().toString())
                    if (response.isSuccessful()) {
                        data.postValue(response.body())
                    } else {
                        data.postValue(null)
                    }
                }

                override fun onFailure(
                    call: Call<DonasiResponse>,
                    t: Throwable
                ) {
                    Log.d(TAG, call.request().toString())
                    data.postValue(null)
                }
            })
        return data
    }

    fun hapusDonasi(
        id: String?,
        apikey: String?,
        apisess: String?
    ): MutableLiveData<DonasiResponse>? {
        val data: MutableLiveData<DonasiResponse> = MutableLiveData<DonasiResponse>()
        Api.service.hapusDonasi(id,apikey, apisess)
            .enqueue(object : Callback<DonasiResponse> {
                override fun onResponse(
                    call: Call<DonasiResponse>,
                    response: Response<DonasiResponse>
                ) {
                    Log.d(TAG, call.request().toString())
                    if (response.isSuccessful()) {
                        data.postValue(response.body())
                    } else {
                        data.postValue(null)
                    }
                }

                override fun onFailure(
                    call: Call<DonasiResponse>,
                    t: Throwable
                ) {
                    Log.d(TAG, call.request().toString())
                    data.postValue(null)
                }
            })
        return data
    }

    fun updateGambarDonasi(
        donasi_id: String?,
        id: String?,
        apikey: String?,
        apisess: String?,
        gambar: MultipartBody.Part?
    ): MutableLiveData<DonasiResponse>? {
        val data: MutableLiveData<DonasiResponse> = MutableLiveData<DonasiResponse>()
        Api.service.updateGambarDonasi(donasi_id,id,apikey, apisess, gambar)
            .enqueue(object : Callback<DonasiResponse> {
                override fun onResponse(
                    call: Call<DonasiResponse>,
                    response: Response<DonasiResponse>
                ) {
                    Log.d(TAG, call.request().toString())
                    if (response.isSuccessful()) {
                        data.postValue(response.body())
                    } else {
                        data.postValue(null)
                    }
                }

                override fun onFailure(
                    call: Call<DonasiResponse>,
                    t: Throwable
                ) {
                    Log.d(TAG, call.request().toString())
                    data.postValue(null)
                }
            })
        return data
    }
}