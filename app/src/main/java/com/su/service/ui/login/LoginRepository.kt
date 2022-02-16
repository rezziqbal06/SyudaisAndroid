package com.su.service.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.su.service.data.source.remote.Api
import com.su.service.data.source.remote.ApiService
import com.su.service.model.NonData
import com.su.service.model.pelanggan.PelangganResponse
import com.su.service.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository{
    fun login(
        apikey: String?,
        email: String?,
        password: String?,
        device: String?,
        fcm_token: String?
    ): MutableLiveData<PelangganResponse>? {
        val user: MutableLiveData<PelangganResponse> = MutableLiveData<PelangganResponse>()
        Api.service.login(apikey, email, password, device, fcm_token)
            .enqueue(object : Callback<PelangganResponse> {
                override fun onResponse(
                    call: Call<PelangganResponse>,
                    response: Response<PelangganResponse>
                ) {
                    if (response.isSuccessful()) {
                        user.postValue(response.body())
                    } else {
                        user.postValue(null)
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

    fun loginByGoogle(
        email: String?, googleId: String?,
        fcm_token: String?, telp: String?
    ): MutableLiveData<PelangganResponse>? {
        val user: MutableLiveData<PelangganResponse> = MutableLiveData<PelangganResponse>()
        Api.service.loginByGoogle(Constants.APIKEY, googleId, telp, email, Constants.DEVICE, fcm_token)
            .enqueue(object : Callback<PelangganResponse> {
                override fun onResponse(
                    call: Call<PelangganResponse>,
                    response: Response<PelangganResponse>
                ) {
                    Log.d("LoginRepository", call.request().toString())
                    if (response.isSuccessful()) {
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(
                    call: Call<PelangganResponse>,
                    t: Throwable
                ) {
                    Log.d("LoginRepository", call.request().toString())
                    user.postValue(null)
                }
            })
        return user
    }

    fun edit_password(
        apikey: String?,
        apisess: String?,
        oldpassword: String?,
        newpassword: String?,
        cpassword: String?
    ): MutableLiveData<PelangganResponse>? {
        val user: MutableLiveData<PelangganResponse> = MutableLiveData<PelangganResponse>()
        Api.service.editPassword(apikey, apisess, oldpassword, newpassword, cpassword)
            .enqueue(object : Callback<PelangganResponse> {
                override fun onResponse(
                    call: Call<PelangganResponse>,
                    response: Response<PelangganResponse>
                ) {
                    if (response.isSuccessful()) {
                        user.postValue(response.body())
                    } else {
                        user.postValue(null)
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

    fun lupa(
        apikey: String?,
        email: String?
    ): MutableLiveData<PelangganResponse>? {
        val user: MutableLiveData<PelangganResponse> = MutableLiveData<PelangganResponse>()
        Api.service.lupa(apikey, email)
            .enqueue(object : Callback<PelangganResponse> {
                override fun onResponse(
                    call: Call<PelangganResponse>,
                    response: Response<PelangganResponse>
                ) {
                    if (response.isSuccessful()) {
                        user.postValue(response.body())
                    } else {
                        user.postValue(null)
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

    fun resetPassword(token: String?, newpassword: String?, cpassword: String?): MutableLiveData<NonData>? {
        val user: MutableLiveData<NonData> = MutableLiveData<NonData>()
        Api.service.resetPassword(token, newpassword, cpassword)
            .enqueue(object : Callback<NonData> {
                override fun onResponse(
                    call: Call<NonData>,
                    response: Response<NonData>
                ) {
                    if (response.isSuccessful()) {
                        user.postValue(response.body())
                    } else {
                        user.postValue(null)
                    }
                }

                override fun onFailure(
                    call: Call<NonData>,
                    t: Throwable
                ) {
                    user.postValue(null)
                }
            })
        return user
    }
}