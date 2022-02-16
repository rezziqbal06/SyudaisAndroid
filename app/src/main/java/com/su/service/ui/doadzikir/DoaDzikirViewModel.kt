package com.su.service.ui.doadzikir

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.su.service.model.doa.DoaResponse
import com.su.service.model.dzikir.DzikirResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DoaDzikirViewModel :ViewModel(){
    val doa = MutableLiveData<DoaResponse>()
    val dzikir = MutableLiveData<DzikirResponse>()
    val repository = DoaDzikirRepository()

    fun uploadDoa(apisess:String,  text: Map<String,RequestBody>, gambar: MultipartBody.Part): LiveData<DoaResponse>{
        return repository.uploadDoa(apisess, text, gambar)
    }

    fun editDoa(id:String?, apisess:String?,  text: Map<String,RequestBody>, gambar: MultipartBody.Part?): LiveData<DoaResponse>{
        return repository.editDoa(id, apisess, text, gambar)
    }

    fun hapusDoa(id: String?,apisess:String): LiveData<DoaResponse>{
        return id?.let { repository.hapusDoa(it, apisess) }!!
    }

    fun uploadDzikir(apisess:String,  text: Map<String,RequestBody>, gambar: MultipartBody.Part): LiveData<DzikirResponse>{
        return repository.uploadDzikir(apisess, text, gambar)
    }

    fun editDzikir(id:String, apisess:String,  text: Map<String,RequestBody>, gambar: MultipartBody.Part): LiveData<DzikirResponse>{
        return repository.editDzikir(id, apisess, text, gambar)
    }

    fun hapusDzikir(id: String?,apisess:String): LiveData<DzikirResponse>{
        return id?.let { repository.hapusDzikir(it, apisess) }!!
    }
}