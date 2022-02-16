package com.su.service.ui.doa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import com.su.service.data.source.local.SyudaisCache
import com.su.service.data.source.remote.DoaService
import com.su.service.model.doa.DoaResult
import com.su.service.ui.dzikirdzikir.dzikir.DoaBoundaryCallback

class DoaRepository (
    private val service: DoaService,
    private val cache: SyudaisCache
){
    private var lastRequestPage = 1
    private val networkErrors = MutableLiveData<String>()
    private var isRequestInProgress = false

    fun query(query: String): DoaResult {
        val dataSourceFactory = cache.queryDoa(query)
        val boundaryCallback =  DoaBoundaryCallback(service, query, cache)
        val networkError = boundaryCallback.networkErrors
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback).build()
        return DoaResult(data, networkError)
    }

    fun refreshDoaData(): LiveData<Boolean> {
        val isFinished = MutableLiveData<Boolean>()
        cache.deleteDoaAll {
            isFinished.postValue(true)
        }
        return isFinished
    }

    companion object{
        private const val DATABASE_PAGE_SIZE = 12
    }


}