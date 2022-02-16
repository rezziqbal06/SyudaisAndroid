package com.su.service.data.source.local

import android.util.Log
import androidx.paging.DataSource
import com.su.service.model.artikel.Artikel
import com.su.service.model.artikel.ArtikelLocal
import com.su.service.model.doa.Doa
import com.su.service.model.dzikir.Dzikir
import retrofit2.http.DELETE
import retrofit2.http.Query
import java.util.concurrent.Executor

class SyudaisCache (private val syudaisDao: SyudaisDao, private val ioExecutor: Executor){
    fun insertAll(artikel: List<Artikel>, insertFinished: () -> Unit){
        ioExecutor.execute{
            Log.d("ArtikelCache", "inserting ${artikel.size} artikel")
            syudaisDao.insertAllArtikel(artikel)
            insertFinished()
        }
    }

    fun updateArtikel(artikel: Artikel, updateFinished: () -> Unit){
        ioExecutor.execute{
            Log.d("ArtikelCache", "updating artikel: ${artikel.title}")
            syudaisDao.updateArtikel(artikel)
            updateFinished()
        }
    }

    fun queryArtikel(stringQuery: String) : DataSource.Factory<Int, Artikel>{
        val query = "%${stringQuery.replace(' ','%')}%"
        return syudaisDao.queryArtikel(query)
    }

    fun queryPopular() : DataSource.Factory<Int, Artikel> {
        return syudaisDao.queryPopularArtikel()
    }

    fun queryArtikelByKategori(stringKategori: String): DataSource.Factory<Int, Artikel> {
        val kategori = "%${stringKategori.replace(' ','%')}%"
        return syudaisDao.queryArtikelByKategori(kategori)
    }

    //Doa
    fun queryDoa(stringQuery: String) : DataSource.Factory<Int, Doa>{
        val query = "%${stringQuery.replace(' ','%')}%"
        return syudaisDao.queryDoa(query)
    }

    fun insertAllDoa(doa: List<Doa>, insertFinished: () -> Unit){
        ioExecutor.execute{
            Log.d("DoaCache", "inserting ${doa.size} doa")
            syudaisDao.insertAllDoa(doa)
            insertFinished()
        }
    }

    fun updateDoa(doa: Doa, updateFinished: () -> Unit){
        ioExecutor.execute{
            Log.d("DoaCache", "updating doa: ${doa.nama}")
            syudaisDao.updateDoa(doa)
            updateFinished()
        }
    }

    fun deleteDoa(doa: Doa, deleteFinished: () -> Unit){
        ioExecutor.execute{
            Log.d("DoaCache", "deleting doa: ${doa.nama}")
            syudaisDao.deleteDoa(doa)
            deleteFinished()
        }
    }

    fun deleteDoaAll(deleteFinished: () -> Unit){
        ioExecutor.execute{
            syudaisDao.deleteDoaAll()
            deleteFinished()
        }
    }



    //Dzikir
    fun queryDzikir(stringQuery: String) : DataSource.Factory<Int, Dzikir>{
        val query = "%${stringQuery.replace(' ','%')}%"
        return syudaisDao.queryDzikir(query)
    }

    fun insertAllDzikir(dzikir: List<Dzikir>, insertFinished: () -> Unit){
        ioExecutor.execute{
            Log.d("DzikirCache", "inserting ${dzikir.size} dzikir")
            syudaisDao.insertAllDzikir(dzikir)
            insertFinished()
        }
    }

    fun updateDzikir(dzikir: Dzikir, updateFinished: () -> Unit){
        ioExecutor.execute{
            Log.d("DzikirCache", "updating dzikir: ${dzikir.nama}")
            syudaisDao.updateDzikir(dzikir)
            updateFinished()
        }
    }

    fun deleteDzikir(dzikir: Dzikir, deleteFinished: () -> Unit){
        ioExecutor.execute{
            Log.d("DzikirCache", "deleting dzikir: ${dzikir.nama}")
            syudaisDao.deleteDzikir(dzikir)
            deleteFinished()
        }
    }

    fun deleteDzikirAll(deleteFinished: () -> Unit){
        ioExecutor.execute{
            syudaisDao.deleteDzikirAll()
            deleteFinished()
        }
    }

}