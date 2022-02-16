package com.su.service.ui.absen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.su.service.data.source.remote.Api
import com.su.service.model.absen.AbsenResponse
import com.su.service.model.santri.SantriResponse
import com.su.service.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AbsenRepository {
    val TAG = AbsenRepository::class.java.simpleName
    fun ngabsen(
        email: String?,
        apisess: String?
    ): MutableLiveData<AbsenResponse>? {
        val data: MutableLiveData<AbsenResponse> = MutableLiveData<AbsenResponse>()
        Api.service.ngabsen(email,Constants.APIKEY, apisess)
            .enqueue(object : Callback<AbsenResponse> {
                override fun onResponse(
                    call: Call<AbsenResponse>,
                    response: Response<AbsenResponse>
                ) {
                    Log.d(TAG, call.request().toString())
                    if (response.isSuccessful()) {
                        data.postValue(response.body())
                    } else {
                        data.postValue(null)
                    }
                }

                override fun onFailure(
                    call: Call<AbsenResponse>,
                    t: Throwable
                ) {
                    Log.d(TAG, call.request().toString())
                    data.postValue(null)
                }
            })
        return data
    }

    fun setAbsen(
        email: String?,
        apisess: String?,
        status: String?,
        idKegiatan: Int?,
        utypeKegiatan: String?
    ): MutableLiveData<AbsenResponse>? {
        val data: MutableLiveData<AbsenResponse> = MutableLiveData<AbsenResponse>()
        Api.service.setAbsen(email,Constants.APIKEY, apisess, status, idKegiatan, utypeKegiatan)
            .enqueue(object : Callback<AbsenResponse> {
                override fun onResponse(
                    call: Call<AbsenResponse>,
                    response: Response<AbsenResponse>
                ) {
                    Log.d(TAG, call.request().toString())
                    if (response.isSuccessful()) {
                        data.postValue(response.body())
                    } else {
                        data.postValue(null)
                    }
                }

                override fun onFailure(
                    call: Call<AbsenResponse>,
                    t: Throwable
                ) {
                    Log.d(TAG, call.request().toString())
                    data.postValue(null)
                }
            })
        return data
    }

    fun list(
        apisess: String?,
        keyword: String?,
        id: Int,
        utype: String
    ): MutableLiveData<SantriResponse>? {
        val data: MutableLiveData<SantriResponse> = MutableLiveData<SantriResponse>()
        Api.service.absensi(Constants.APIKEY, apisess, keyword, id, utype)
            .enqueue(object : Callback<SantriResponse> {
                override fun onResponse(
                    call: Call<SantriResponse>,
                    response: Response<SantriResponse>
                ) {
                    Log.d(TAG, call.request().toString())
                    Log.d(TAG, response.toString())
                    if (response.isSuccessful()) {
                        data.postValue(response.body())
                    } else {
                        data.postValue(null)
                    }
                }

                override fun onFailure(
                    call: Call<SantriResponse>,
                    t: Throwable
                ) {
                    Log.d(TAG, call.request().toString())
                    Log.d(TAG, t.message.toString())
                    data.postValue(null)
                }
            })
        return data
    }


}