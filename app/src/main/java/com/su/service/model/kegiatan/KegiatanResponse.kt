package com.su.service.model.kegiatan

import com.google.gson.annotations.SerializedName

data class KegiatanResponse(

	@field:SerializedName("result")
	val result: Result? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class KegiatanItem(

	@field:SerializedName("sdate")
	val sdate: String? = null,

	@field:SerializedName("is_active")
	val isActive: String? = null,

	@field:SerializedName("narasumber")
	val narasumber: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("utype")
	val utype: String? = null,

	@field:SerializedName("edate")
	val edate: String? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("tempat")
	val tempat: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("judul")
	val judul: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null
)

data class Result(

	@field:SerializedName("kegiatan")
	val kegiatan: List<KegiatanItem?>? = null,

	@field:SerializedName("pagesize")
	val pagesize: Int? = null,

	@field:SerializedName("kegiatan_count")
	val kegiatanCount: Int? = null,

	@field:SerializedName("dckegiatan")
	val dckegiatan: Int? = null,

	@field:SerializedName("kategori")
	val kategori: String? = null,

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("keyword")
	val keyword: String? = null,

	@field:SerializedName("dckajian")
	val dckajian: Int? = null
)
