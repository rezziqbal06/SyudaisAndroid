package com.su.service.model.detaildonasi

import com.google.gson.annotations.SerializedName

data class Result(
	val donasi: DonasiItem? = null,
	@SerializedName("donasi_count")
	val donasiCount: String? = null,
	val pagesize: Int? = null,
	val kategori: String? = null,
	val page: Int? = null,
	val keyword: String? = null
)
