package com.su.service.ui.artikel.buateditartikel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.su.service.data.source.local.SyudaisDatabase
import com.su.service.model.artikel.ArtikelLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtikelLocalViewModel(application: Application) : AndroidViewModel(application){
    private val localRepository: ArtikelLocalRepository
    val artikelLocal: LiveData<List<ArtikelLocal>>
    init {
        val dao = SyudaisDatabase.getInstance(application).syudaisDao()
        localRepository = ArtikelLocalRepository(dao)
        artikelLocal = localRepository.getArtikel
    }

    fun insert(artikel: ArtikelLocal) = viewModelScope.launch(Dispatchers.IO) { localRepository.insert(artikel) }

    fun update(artikel: ArtikelLocal) = viewModelScope.launch(Dispatchers.IO) { localRepository.update(artikel) }

    fun delete(artikel: ArtikelLocal) = viewModelScope.launch(Dispatchers.IO) { localRepository.delete(artikel) }

}