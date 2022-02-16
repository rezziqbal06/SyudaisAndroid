package com.su.service.ui.artikel.buateditartikel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.su.service.data.source.local.SyudaisDao
import com.su.service.data.source.local.SyudaisDatabase
import com.su.service.model.artikel.ArtikelLocal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class ArtikelLocalRepository (private val dao: SyudaisDao
) {


    private val TAG = ArtikelLocalRepository::class.java.simpleName

    val getArtikel = dao.getAllArtikelLocal()

    suspend fun insert(artikel: ArtikelLocal): Long {
           return dao.insertArtikelLocal(artikel)
    }

    suspend fun update(artikel: ArtikelLocal){
            dao.updateArtikelLocal(artikel)
    }

    suspend fun delete(artikel: ArtikelLocal){
            dao.deleteArtikelLocal(artikel)
    }
    companion object{
        private const val DATABASE_PAGE_SIZE = 12
    }
}