package com.su.service.ui.dzikir

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import com.su.service.data.source.local.SyudaisCache
import com.su.service.data.source.remote.DzikirService
import com.su.service.model.dzikir.DzikirResult

class DzikirRepository (
    private val service: DzikirService,
    private val cache: SyudaisCache
){
    private var lastRequestPage = 1
    private val networkErrors = MutableLiveData<String>()
    private var isRequestInProgress = false

    fun query(query: String): DzikirResult {
        val dataSourceFactory = cache.queryDzikir(query)
        val boundaryCallback =  DzikirBoundaryCallback(service, query, cache)
        val networkError = boundaryCallback.networkErrors
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback).build()
        return DzikirResult(data, networkError)
    }

    fun refreshData(): LiveData<Boolean> {
        val isFinished = MutableLiveData<Boolean>()
        cache.deleteDzikirAll {
            isFinished.postValue(true)
        }
        return isFinished
    }

    companion object{
        private const val DATABASE_PAGE_SIZE = 12
    }


}