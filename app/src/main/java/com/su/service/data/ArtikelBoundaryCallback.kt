package com.su.service.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.su.service.data.source.local.SyudaisCache
import com.su.service.data.source.remote.ArtikelService
import com.su.service.data.source.remote.queryArtikel
import com.su.service.model.artikel.Artikel

class ArtikelBoundaryCallback(private val service: ArtikelService,
                              private val kategori: String,
                              private val query: String,
                              private val cache: SyudaisCache): PagedList.BoundaryCallback<Artikel>() {
    private var lastRequestedPage = 0
    private val TAG = ArtikelBoundaryCallback::class.java.simpleName

    // LiveData of network errors.
    private val _networkErrors = MutableLiveData<String>()
    // LiveData of network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    companion object{
        private const val NETWORK_PAGE_SIZE = 12
    }

    override fun onZeroItemsLoaded() {
        Log.d(TAG, "Item kosong")
        requestAndSaveData(kategori, query)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Artikel) {
        Log.d(TAG, "Item diakhir")
        requestAndSaveData(kategori, query)
    }

    private fun requestAndSaveData(kategori: String, query: String){
        if(isRequestInProgress) return

        isRequestInProgress = true
        queryArtikel(service, kategori, query, lastRequestedPage.toString(), NETWORK_PAGE_SIZE.toString(), { artikel ->
            cache.insertAll(artikel){
                Log.d(TAG,"Berhasil insert")
                lastRequestedPage++
                isRequestInProgress = false
            }
        }, { error ->
            _networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }
}