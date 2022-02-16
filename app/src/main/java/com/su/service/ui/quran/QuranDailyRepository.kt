package com.su.service.ui.quran

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.su.service.data.source.remote.Api
import com.su.service.model.qddetail.QDDetailResponse
import com.su.service.model.qurandaily.QDResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuranDailyRepository{
    val TAG = QuranDailyRepository::class.java.simpleName
    fun getAll(apikey: String?, apisess: String?): MutableLiveData<QDResponse>? {
        val data: MutableLiveData<QDResponse> = MutableLiveData<QDResponse>()
        Api.service.getQuranDaily(apikey, apisess)
            .enqueue(object : Callback<QDResponse> {
                override fun onResponse(
                    call: Call<QDResponse>,
                    response: Response<QDResponse>
                ) {
                    Log.d(TAG, call.request().toString())
                    if (response.isSuccessful()) {
                        data.postValue(response.body())
                    } else {
                        data.postValue(null)
                    }
                }

                override fun onFailure(
                    call: Call<QDResponse>,
                    t: Throwable
                ) {
                    Log.d(TAG, call.request().toString())
                    data.postValue(null)
                }
            })
        return data
    }

    fun getPicked(apikey: String?): MutableLiveData<QDDetailResponse>? {
        val data: MutableLiveData<QDDetailResponse> = MutableLiveData<QDDetailResponse>()
        Api.service.getQDPicked(apikey)
            .enqueue(object : Callback<QDDetailResponse> {
                override fun onResponse(
                    call: Call<QDDetailResponse>,
                    response: Response<QDDetailResponse>
                ) {
                    Log.d(TAG, call.request().toString())
                    if (response.isSuccessful()) {
                        data.postValue(response.body())
                    } else {
                        data.postValue(null)
                    }
                }

                override fun onFailure(
                    call: Call<QDDetailResponse>,
                    t: Throwable
                ) {
                    Log.d(TAG, call.request().toString())
                    data.postValue(null)
                }
            })
        return data
    }

    fun tambah(
        apikey: String?,
        apisess: String?,
        judul: String?,
        namaSurat: String?,
        noSurat: String?,
        noAyat: String?
    ): MutableLiveData<QDResponse>? {
        val data: MutableLiveData<QDResponse> = MutableLiveData<QDResponse>()
        Api.service.tambahQD(apikey, apisess, judul, namaSurat, noSurat, noAyat)
            .enqueue(object : Callback<QDResponse> {
                override fun onResponse(
                    call: Call<QDResponse>,
                    response: Response<QDResponse>
                ) {
                    Log.d(TAG, call.request().toString())
                    if (response.isSuccessful()) {
                        data.postValue(response.body())
                    } else {
                        data.postValue(null)
                    }
                }

                override fun onFailure(
                    call: Call<QDResponse>,
                    t: Throwable
                ) {
                    Log.d(TAG, call.request().toString())
                    data.postValue(null)
                }
            })
        return data
    }

    fun edit(
        id: String?,
        apikey: String?,
        apisess: String?,
        judul: String?,
        namaSurat: String?,
        noSurat: String?,
        noAyat: String?
    ): MutableLiveData<QDResponse>? {
        val data: MutableLiveData<QDResponse> = MutableLiveData<QDResponse>()
        Api.service.editQD(id, apikey, apisess, judul, namaSurat, noSurat, noAyat)
            .enqueue(object : Callback<QDResponse> {
                override fun onResponse(
                    call: Call<QDResponse>,
                    response: Response<QDResponse>
                ) {
                    Log.d(TAG, call.request().toString())
                    if (response.isSuccessful()) {
                        data.postValue(response.body())
                    } else {
                        data.postValue(null)
                    }
                }

                override fun onFailure(
                    call: Call<QDResponse>,
                    t: Throwable
                ) {
                    Log.d(TAG, call.request().toString())
                    Log.d(TAG, t.message)
                    data.postValue(null)
                }
            })
        return data
    }

    fun hapus(
        id: String?,
        apikey: String?,
        apisess: String?
    ): MutableLiveData<QDResponse>? {
        val data: MutableLiveData<QDResponse> = MutableLiveData<QDResponse>()
        Api.service.hapusQD(id,apikey, apisess)
            .enqueue(object : Callback<QDResponse> {
                override fun onResponse(
                    call: Call<QDResponse>,
                    response: Response<QDResponse>
                ) {
                    Log.d(TAG, call.request().toString())
                    if (response.isSuccessful()) {
                        data.postValue(response.body())
                    } else {
                        data.postValue(null)
                    }
                }

                override fun onFailure(
                    call: Call<QDResponse>,
                    t: Throwable
                ) {
                    Log.d(TAG, call.request().toString())
                    data.postValue(null)
                }
            })
        return data
    }

    fun pick(
        id: String?,
        apikey: String?,
        apisess: String?
    ): MutableLiveData<QDResponse>? {
        val data: MutableLiveData<QDResponse> = MutableLiveData<QDResponse>()
        Api.service.pickQD(id,apikey, apisess)
            .enqueue(object : Callback<QDResponse> {
                override fun onResponse(
                    call: Call<QDResponse>,
                    response: Response<QDResponse>
                ) {
                    Log.d(TAG, call.request().toString())
                    if (response.isSuccessful()) {
                        data.postValue(response.body())
                    } else {
                        data.postValue(null)
                    }
                }

                override fun onFailure(
                    call: Call<QDResponse>,
                    t: Throwable
                ) {
                    Log.d(TAG, call.request().toString())
                    data.postValue(null)
                }
            })
        return data
    }


}