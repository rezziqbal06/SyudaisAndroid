package com.su.service.data.source.local


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.su.service.model.artikel.Artikel
import com.su.service.model.artikel.ArtikelLocal
import com.su.service.model.doa.Doa
import com.su.service.model.dzikir.Dzikir


@Database(entities = arrayOf(Artikel::class, ArtikelLocal::class, Doa::class, Dzikir::class), version = 9, exportSchema = false)
abstract class SyudaisDatabase : RoomDatabase() {
  abstract fun syudaisDao(): SyudaisDao
  companion object {

    @Volatile
    private var INSTANCE: SyudaisDatabase? = null

    fun getInstance(context: Context): SyudaisDatabase =
      INSTANCE ?: synchronized(this) {
        INSTANCE
          ?: buildDatabase(context).also { INSTANCE = it }
      }

    private fun buildDatabase(context: Context) = Room.databaseBuilder(context.applicationContext,SyudaisDatabase::class.java, "syudais_db")
      .fallbackToDestructiveMigration().build()
  }
}