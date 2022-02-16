package com.su.service.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import com.su.service.model.artikel.ArtikelResult
import com.su.service.data.source.local.SyudaisCache
import com.su.service.data.source.remote.ArtikelService

class ArtikelRepository (
    private val service: ArtikelService,
    private val cache: SyudaisCache
){
    private var lastRequestPage = 1
    private val networkErrors = MutableLiveData<String>()
    private var isRequestInProgress = false

    fun query(query: String): ArtikelResult{
        val dataSourceFactory = cache.queryArtikel(query)
        val boundaryCallback =  ArtikelBoundaryCallback(service,"", query, cache)
        val networkError = boundaryCallback.networkErrors
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback).build()
        return ArtikelResult(data, networkError)
    }

    fun queryByKategori(kategori: String): ArtikelResult {
        val dataSourceFactory = cache.queryArtikelByKategori(kategori)
        val boundaryCallback =  ArtikelBoundaryCallback(service, kategori,"", cache)
        val networkError = boundaryCallback.networkErrors
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback).build()
        return ArtikelResult(data, networkError)
    }

    fun queryPopular(query: String): ArtikelResult{
        val dataSourceFactory = cache.queryPopular()
        val boundaryCallback =  ArtikelBoundaryCallback(service,"", query, cache)
        val networkError = boundaryCallback.networkErrors
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback).build()
        return ArtikelResult(data, networkError)
    }


    companion object{
        private const val DATABASE_PAGE_SIZE = 12
    }


}