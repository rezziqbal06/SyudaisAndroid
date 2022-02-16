package com.su.service.ui.doa

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.su.service.data.source.local.SyudaisCache
import com.su.service.data.source.remote.DoaService
import com.su.service.data.source.remote.DzikirService
import com.su.service.data.source.remote.queryDoa
import com.su.service.data.source.remote.queryDzikir
import com.su.service.model.doa.Doa
import com.su.service.model.doa.DoaResult
import com.su.service.model.dzikir.Dzikir
import com.su.service.model.dzikir.DzikirResult

class DoaViewModel(private val repository: DoaRepository) : ViewModel() {
    companion object{
        private var TAG = DoaViewModel::class.java.simpleName
        var NETWORK_PAGE_SIZE = 12
    }
    private val queryLiveData = MutableLiveData<String>()

    fun getQuery(queryString: String){
        queryLiveData.postValue(queryString)
    }

    private val resultSearch: LiveData<DoaResult> = Transformations.map(queryLiveData){
        repository.query(it)
    }

    val getDoa: LiveData<PagedList<Doa>> =  Transformations.switchMap(resultSearch){
        it.data
    }

    val getDoaNetwork: LiveData<String> = Transformations.switchMap(resultSearch){
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

  fun refreshData() : LiveData<Boolean>{
      return repository.refreshDoaData()
  }

}