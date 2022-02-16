package com.su.service.model.kategori

import com.google.gson.annotations.SerializedName

data class Result(
	@SerializedName("kategori_count")
	val kategoriCount: String? = null,
	val kategori: ArrayList<KategoriItem?>? = null
)
