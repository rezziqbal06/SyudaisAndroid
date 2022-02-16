package com.su.service.model.artikel

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "artikel")

@Parcelize
data class Artikel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "kategori")
    var kategori: String? = null,

    @ColumnInfo(name = "b_user_id")
    val b_user_id: String? = null,

    @ColumnInfo(name = "author")
    val author: String? = null,

    @ColumnInfo(name = "content")
    var content: String? = null,

    @ColumnInfo(name = "gambar")
    val gambar: String? = null,

    @ColumnInfo(name = "thumb")
    val thumb: String? =null,

    @ColumnInfo(name = "cdate")
    val cdate: String? =null,

    @ColumnInfo(name = "ldate")
    var ldate: String? = null,

    @ColumnInfo(name = "jml_baca")
    val jml_baca: String? = null,

    @ColumnInfo(name = "status")
    var status: String? = null,

    @ColumnInfo(name = "is_follow_up")
    val is_follow_up: Int? = null,

    @ColumnInfo(name = "is_read")
    val is_read: Int? = null,

    @ColumnInfo(name = "b_user_admin_id")
    val b_user_admin_id: String? = null

): Parcelable