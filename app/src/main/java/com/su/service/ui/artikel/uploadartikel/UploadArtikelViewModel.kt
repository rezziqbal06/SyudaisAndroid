package com.su.service.ui.artikel.uploadartikel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.su.service.model.artikel.ArtikelResponse

class UploadArtikelViewModel :ViewModel(){
    val repository = UploadArtikelRepository()
    fun uploadArtikel(apisess: String, title: String, content: String, kategori: String, slug: String, mdescription: String):LiveData<ArtikelResponse>{
        return repository.uploadArtikel(apisess, title, content, kategori, slug, mdescription)
    }
}