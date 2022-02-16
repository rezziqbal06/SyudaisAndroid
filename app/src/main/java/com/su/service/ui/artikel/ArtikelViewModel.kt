package com.su.service.ui.artikel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.su.service.data.ArtikelRepository
import com.su.service.data.source.local.SyudaisCache
import com.su.service.data.source.remote.ArtikelService
import com.su.service.data.source.remote.queryArtikel
import com.su.service.model.artikel.Artikel
import com.su.service.model.artikel.ArtikelResult

class ArtikelViewModel(private val repository: ArtikelRepository) : ViewModel() {
    companion object{
        private var TAG = ArtikelViewModel::class.java.simpleName
    }
    private val queryLiveData = MutableLiveData<String>()
    private val kategoriLiveData = MutableLiveData<String>()
    private val popularLiveData = MutableLiveData<String>()

    fun getQuery(queryString: String){
        queryLiveData.postValue(queryString)
    }

    fun getPopular(popularString: String){
        popularLiveData.postValue(popularString)
    }

    fun getKategori(kategoriString: String){
        kategoriLiveData.postValue(kategoriString)
    }

    private val resultSearch: LiveData<ArtikelResult> = Transformations.map(queryLiveData){
        repository.query(it)
    }

    private val resultKategori : LiveData<ArtikelResult> = Transformations.map(kategoriLiveData){
        Log.d(TAG, "ngambil data ke repository")
        repository.queryByKategori(it)
    }

    private val resultPopular : LiveData<ArtikelResult> = Transformations.map(popularLiveData){
        repository.queryPopular(it)
    }


    val artikelSearch: LiveData<PagedList<Artikel>> =  Transformations.switchMap(resultSearch){
        it.data
    }

    val searchNetworkError: LiveData<String> = Transformations.switchMap(resultSearch){
        it.networkErrors
    }

    val artikelPopular: LiveData<PagedList<Artikel>> =  Transformations.switchMap(resultPopular){
        it.data
    }

    val popularNetwork: LiveData<String> = Transformations.switchMap(resultPopular){
        it.networkErrors
    }

    val artikelKategori: LiveData<PagedList<Artikel>> =  Transformations.switchMap(resultKategori){
        Log.d(TAG, "simpan di livedata")
        it.data
    }

    val kategoriNetworkError: LiveData<String> = Transformations.switchMap(resultKategori){
        it.networkErrors
    }

}