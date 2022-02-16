package com.su.service.ui.dzikir

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.su.service.data.source.local.SyudaisCache
import com.su.service.data.source.remote.DzikirService
import com.su.service.data.source.remote.queryDzikir
import com.su.service.model.dzikir.Dzikir
import com.su.service.model.dzikir.DzikirResult

class DzikirViewModel(private val repository: DzikirRepository) : ViewModel() {
    companion object{
        private var TAG = DzikirViewModel::class.java.simpleName
        var NETWORK_PAGE_SIZE = 12
    }
    private val queryLiveData = MutableLiveData<String>()

    fun getQuery(queryString: String){
        queryLiveData.postValue(queryString)
    }

    private val resultSearch: LiveData<DzikirResult> = Transformations.map(queryLiveData){
        repository.query(it)
    }

    fun refreshData(): LiveData<Boolean> = repository.refreshData()

    val getDzikir: LiveData<PagedList<Dzikir>> =  Transformations.switchMap(resultSearch){
        it.data
    }

    val getDzikirNetworkError: LiveData<String> = Transformations.switchMap(resultSearch){
        it.networkErrors
    }

    var lastRequestedPage = 1
    // LiveData of network errors.
    private val _networkErrors = MutableLiveData<String>()
    // LiveData of network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false
    fun requestAndSaveData(service: DzikirService, cache: SyudaisCache, query: String){
        queryDzikir(service, query, lastRequestedPage.toString(), NETWORK_PAGE_SIZE.toString(), { data ->
            cache.insertAllDzikir(data){
                lastRequestedPage++
                isRequestInProgress = false
            }
        }, { error ->
            _networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }




}