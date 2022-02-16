package com.su.service.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.su.service.model.NonData
import com.su.service.model.pelanggan.PelangganResponse
import com.su.service.model.update.UpdateResponse
import com.su.service.ui.login.LoginRepository
import com.su.service.ui.login.RegisterRepository
import com.su.service.utils.Constants

class MainViewModel : ViewModel(){
    private lateinit var data: MutableLiveData<PelangganResponse>
    private lateinit var dataUpdate: MutableLiveData<UpdateResponse>

    fun login(email: String?, password: String, fcmToken: String): LiveData<PelangganResponse>{
        val loginRepository = LoginRepository()
        data = loginRepository.login(Constants.APIKEY,email, password,Constants.DEVICE, fcmToken)!!
        return data
    }

    fun lupa(email: String?): LiveData<PelangganResponse>{
        val loginRepository = LoginRepository()
        data = loginRepository.lupa(Constants.APIKEY,email)!!
        return data
    }

    fun loginByGoogle(email: String?, googleId: String, fcmToken: String, telepon: String?): LiveData<PelangganResponse>{
        val loginRepository = LoginRepository()
        data = loginRepository.loginByGoogle(email, googleId, fcmToken, telepon)!!
        return data
    }

    fun updatePassword(apisess: String?,oldpassword: String?, newpassword: String?, cpassword: String?): LiveData<PelangganResponse>{
        val loginRepository = LoginRepository()
        data = loginRepository.edit_password(Constants.APIKEY,apisess,oldpassword, newpassword, cpassword)!!
        return data
    }

    fun resetPassword(token: String?, newpassword: String?, cpassword: String?): LiveData<NonData>{
        val loginRepository = LoginRepository()
        return loginRepository.resetPassword(token, newpassword, cpassword)!!
    }

    fun daftarByGoogle(nama: String, email: String, googleId: String, gambar: String, fcmToken: String): LiveData<PelangganResponse>{
        val registerRepository = RegisterRepository()
        data = registerRepository.daftarBySosmed(Constants.APIKEY,nama, email,googleId,gambar,Constants.DEVICE,fcmToken)!!
        return data
    }

    fun getUser(): LiveData<PelangganResponse>{
        return data
    }

    fun checkUpdate(versionName: String): LiveData<UpdateResponse>{
        val repository =  MainRepository()
        dataUpdate = repository.checkUpdate(Constants.APIKEY, versionName)!!
        return dataUpdate
    }

}