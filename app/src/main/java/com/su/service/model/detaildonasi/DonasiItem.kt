package com.su.service.model.detaildonasi

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DonasiItem(
	val cdate: String? = null,
	@SerializedName("is_active")
	val isActive: String? = null,
	val nama: String? = null,
	val thumb: String? = null,
	val dana: String? = null,
	val atas_nama: String? = null,
	val rekening: String? = null,
	val bank: String? = null,
	val galeri: ArrayList<GaleriDonasi>? = null,
	val latitude: String? = null,
	val wa: String? = null,
	val id: Int? = null,
	val deskripsi: String? = null,
	val gambar: String? = null,
	val nomor: String? = null,
	val longitude: String? = null
) : Parcelable
