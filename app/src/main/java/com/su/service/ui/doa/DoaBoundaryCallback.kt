package com.su.service.ui.dzikirdzikir.dzikir

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.su.service.data.source.local.SyudaisCache
import com.su.service.data.source.remote.DoaService
import com.su.service.data.source.remote.queryDoa
import com.su.service.model.doa.Doa

class DoaBoundaryCallback(private val service: DoaService,
                          private val query: String,
                          private val cache: SyudaisCache): PagedList.BoundaryCallback<Doa>() {
    private var lastRequestedPage = 0

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
        requestAndSaveData(query)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Doa) {
        requestAndSaveData(query)
    }

    private fun requestAndSaveData( query: String){
        if(isRequestInProgress) return

        isRequestInProgress = true
        queryDoa(service,  query, lastRequestedPage.toString(), NETWORK_PAGE_SIZE.toString(), { doa ->
            cache.insertAllDoa(doa){
                lastRequestedPage++
                isRequestInProgress = false
            }
        }, { error ->
            _networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }
}