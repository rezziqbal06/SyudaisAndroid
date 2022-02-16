package com.su.service.data.source.local


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.su.service.model.artikel.Artikel
import com.su.service.model.artikel.ArtikelLocal
import com.su.service.model.doa.Doa
import com.su.service.model.dzikir.Dzikir

@Dao
interface SyudaisDao {
  //Artikel
  @Query("SELECT * FROM artikel WHERE (title LIKE :stringQuery) OR (content LIKE :stringQuery) ORDER BY id ")
  fun queryArtikel(stringQuery: String): DataSource.Factory<Int, Artikel>

  @Query("SELECT * FROM artikel ORDER BY jml_baca DESC")
  fun queryPopularArtikel(): DataSource.Factory<Int, Artikel>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertArtikel(artikel: Artikel)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAllArtikel(artikel: List<Artikel>)

  @Update(onConflict = OnConflictStrategy.IGNORE)
  fun updateArtikel(artikel: Artikel)

  @Query("SELECT * FROM artikel WHERE (kategori LIKE :stringKategori) ORDER BY id ")
  fun queryArtikelByKategori(stringKategori: String): DataSource.Factory<Int, Artikel>

  @Query("DELETE FROM artikel")
  fun deleteArtikelAll()

  //ArtikelLocal
  @Query("SELECT * FROM artikel_local ORDER BY id")
  fun getAllArtikelLocal(): LiveData<List<ArtikelLocal>>
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertArtikelLocal(artikel: ArtikelLocal): Long

  @Update(onConflict = OnConflictStrategy.IGNORE)
  fun updateArtikelLocal(artikel: ArtikelLocal)

  @Delete
  fun deleteArtikelLocal(artikel: ArtikelLocal)

  //Doa
  @Query("SELECT * FROM doa WHERE (nama LIKE :stringQuery) OR (dalil LIKE :stringQuery) OR (doa LIKE :stringQuery) ORDER BY id ")
  fun queryDoa(stringQuery: String): DataSource.Factory<Int, Doa>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertDoa(doa: Doa)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAllDoa(doa: List<Doa>)

  @Update(onConflict = OnConflictStrategy.IGNORE)
  fun updateDoa(doa: Doa)

  @Delete
  fun deleteDoa(doa: Doa)

  @Delete
  fun deleteAllDoa(doa: List<Doa>)

  @Query("DELETE FROM doa")
  fun deleteDoaAll()

  //Dzikir
  @Query("SELECT * FROM dzikir WHERE (nama LIKE :stringQuery) OR (dalil LIKE :stringQuery) OR (dzikir LIKE :stringQuery) ORDER BY id ")
  fun queryDzikir(stringQuery: String): DataSource.Factory<Int, Dzikir>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertDzikir(dzikir: Dzikir)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAllDzikir(dzikir: List<Dzikir>)

  @Update(onConflict = OnConflictStrategy.IGNORE)
  fun updateDzikir(dzikir: Dzikir)

  @Delete
  fun deleteDzikir(dzikir: Dzikir)

  @Delete
  fun deleteAllDzikir(dzikir: List<Dzikir>)

  @Query("DELETE FROM dzikir")
  fun deleteDzikirAll()

}