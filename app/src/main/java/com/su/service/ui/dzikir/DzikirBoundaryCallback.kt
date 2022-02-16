package com.su.service.ui.dzikir

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.su.service.data.source.local.SyudaisCache
import com.su.service.data.source.remote.DzikirService
import com.su.service.data.source.remote.queryDzikir
import com.su.service.model.dzikir.Dzikir

class DzikirBoundaryCallback(private val service: DzikirService,
                             private val query: String,
                             private val cache: SyudaisCache): PagedList.BoundaryCallback<Dzikir>() {
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

    override fun onItemAtEndLoaded(itemAtEnd: Dzikir) {
        requestAndSaveData(query)
    }

    private fun requestAndSaveData( query: String){
        if(isRequestInProgress) return

        isRequestInProgress = true
        queryDzikir(service,  query, lastRequestedPage.toString(), NETWORK_PAGE_SIZE.toString(), { dzikir ->
            cache.insertAllDzikir(dzikir){
                lastRequestedPage++
                isRequestInProgress = false
            }
        }, { error ->
            _networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }
}