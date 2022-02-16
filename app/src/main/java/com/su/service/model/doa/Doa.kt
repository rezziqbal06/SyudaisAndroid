package com.su.service.model.doa

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "doa")
data class Doa(
    @PrimaryKey
    val id: Int? = null,
    val nama: String? = null,
    val doa: String? = null,
    val dalil: String? = null,
    val gambar: String? = null
)