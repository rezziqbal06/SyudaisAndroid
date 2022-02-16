package com.su.service.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.su.service.model.artikel.ArtikelResponse
import okhttp3.MultipartBody

class UserViewModel : ViewModel() {
    val repository = UserRepository()
    fun getByUser(apisess: String):LiveData<ArtikelResponse>{
        return repository.getByUser(apisess)
    }

    fun editArtikel(id:String, apisess: String, title:String, content: String, kategori: String, slug: String, mdescription: String):LiveData<ArtikelResponse>{
        return repository.editArtikel(id, apisess, title, content, kategori, slug, mdescription)
    }

    fun hapusArtikel(id: String, apisess: String):LiveData<ArtikelResponse>{
        return repository.hapusArtikel(id,apisess)
    }

    fun setStatus(id: Int, apisess: String?, status: String):LiveData<ArtikelResponse> {
        return repository.setStatus(id, apisess, status)
    }

    fun updateGambarArtikel(id: Int?, apisess: String?, gambar: MultipartBody.Part?):LiveData<ArtikelResponse> {
        return repository.updateGambarArtikel(id, apisess, gambar)
    }

    fun bacaArtikel(id: Int?, apisess: String?):LiveData<ArtikelResponse> {
        return repository.bacaArtikel(id, apisess)
    }
}