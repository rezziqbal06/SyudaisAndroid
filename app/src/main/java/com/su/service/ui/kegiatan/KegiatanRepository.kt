package com.su.service.ui.kegiatan

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.su.service.data.source.remote.Api
import com.su.service.model.kegiatan.KegiatanResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KegiatanRepository (
){
    val TAG = KegiatanRepository::class.java.simpleName
    fun getKegiatan(
        apikey: String?,
        apisess: String?
    ): MutableLiveData<KegiatanResponse>? {
        val data: MutableLiveData<KegiatanResponse> = MutableLiveData<KegiatanResponse>()
        Api.service.kegiatanGeneral(apikey, apisess)
            .enqueue(object : Callback<KegiatanResponse> {
                override fun onResponse(
                    call: Call<KegiatanResponse>,
                    response: Response<KegiatanResponse>
                ) {
                    Log.d(TAG, call.request().toString())
                    if (response.isSuccessful()) {
                        data.postValue(response.body())
                    } else {
                        data.postValue(null)
                    }
                }

                override fun onFailure(
                    call: Call<KegiatanResponse>,
                    t: Throwable
                ) {
                    Log.d(TAG, call.request().toString())
                    data.postValue(null)
                }
            })
        return data
    }

}