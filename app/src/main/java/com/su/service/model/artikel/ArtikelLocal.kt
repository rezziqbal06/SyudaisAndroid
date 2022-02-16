package com.su.service.model.artikel

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "artikel_local")
@Parcelize
data class ArtikelLocal(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "kategori")
    var kategori: String? = null,

    @ColumnInfo(name = "b_user_id")
    var b_user_id: String? = null,

    @ColumnInfo(name = "author")
    var author: String? = null,

    @ColumnInfo(name = "content")
    var content: String? = null,

    @ColumnInfo(name = "gambar")
    var gambar: String? = null,

    @ColumnInfo(name = "thumb")
    var thumb: String? =null,

    @ColumnInfo(name = "cdate")
    var cdate: String? = null,

    @ColumnInfo(name = "ldate")
    var ldate: String? = null,

    @ColumnInfo(name = "jml_baca")
    var jml_baca: String? = null,

    @ColumnInfo(name = "status")
    var status: String? = null
): Parcelable