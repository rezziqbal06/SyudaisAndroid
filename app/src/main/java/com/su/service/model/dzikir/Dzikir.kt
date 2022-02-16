package com.su.service.model.dzikir

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "dzikir")
data class Dzikir(
    @PrimaryKey
    val id: Int? = null,
    val nama: String? = null,
    val dzikir: String? = null,
    val dalil: String? = null,
    val gambar: String? = null
)