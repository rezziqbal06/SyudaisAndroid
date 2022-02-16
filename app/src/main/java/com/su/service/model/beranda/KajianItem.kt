package com.su.service.model.beranda

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KajianItem(
	val sdate: String? = null,
	val deskripsi: String? = null,
	val tempat: String? = null,
	val alamat: String? = null,
	val latitude: String? = null,
	val longitude: String? = null,
	@SerializedName("is_active")
	val isActive: String? = null,
	val narasumber: String? = null,
	val gambar: String? = null,
	val id: String? = null,
	val judul: String? = null,
	val edate: String? = null
): Parcelable
