package com.su.service.model.donasi

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.su.service.model.detaildonasi.GaleriDonasi
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DonasiItem(
	val cdate: String? = null,
	@SerializedName("is_active")
	val isActive: String? = null,
	val nama: String? = null,
	val dana: String? = null,
	val thumb: String? = null,
	val galeri: List<GaleriDonasi>? = null,
	val latitude: String? = null,
	val wa: String? = null,
	val id: Int? = null,
	val deskripsi: String? = null,
	val gambar: String? = null,
	val nomor: String? = null,
	val longitude: String? = null
) : Parcelable
