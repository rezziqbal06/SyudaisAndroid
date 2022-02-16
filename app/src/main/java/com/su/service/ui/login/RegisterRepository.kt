package com.su.service.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.su.service.data.source.remote.Api
import com.su.service.model.pelanggan.PelangganResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterRepository {
    fun daftar(
        apikey: String?,
        nation_code: String?,
        fnama: String?,
        email: String?,
        password: String?,
        telp: String?,
        device: String?,
        fcm_token: String?
    ): MutableLiveData<PelangganResponse>? {
        val user: MutableLiveData<PelangganResponse> = MutableLiveData<PelangganResponse>()
        Api.service.daftar(apikey, nation_code, fnama, password, telp, email, device, fcm_token)
            .enqueue(object : Callback<PelangganResponse> {
                override fun onResponse(
                    call: Call<PelangganResponse>,
                    response: Response<PelangganResponse>
                ) {
                    if (response.isSuccessful()) {
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(
                    call: Call<PelangganResponse>,
                    t: Throwable
                ) {
                    user.postValue(null)
                }
            })
        return user
    }

    fun daftarBySosmed(
        apikey: String?,
        nama: String?,
        email: String?,
        google_id: String?,
        gambar: String?,
        device: String?,
        fcm_token: String?
    ): MutableLiveData<PelangganResponse>? {
        val user: MutableLiveData<PelangganResponse> = MutableLiveData<PelangganResponse>()
        Api.service.daftarBySosmed(apikey,nama, google_id, email, gambar, device, fcm_token)
            .enqueue(object : Callback<PelangganResponse> {
                override fun onResponse(
                    call: Call<PelangganResponse>,
                    response: Response<PelangganResponse>
                ) {
                    Log.d("DaftarRepository", call.request().toString())
                    if (response.isSuccessful()) {
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(
                    call: Call<PelangganResponse>,
                    t: Throwable
                ) {
                    Log.d("DaftarRepository", call.request().toString())
                    user.postValue(null)
                }
            })
        return user
    }
}