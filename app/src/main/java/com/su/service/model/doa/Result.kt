package com.su.service.model.doa

import com.google.gson.annotations.SerializedName

data class Result(
	@SerializedName("doa_count")
	val doaCount: String? = null,
	val pagesize: Int? = null,
	val kategori: String? = null,
	val page: Int? = null,
	val keyword: String? = null,
	val doa: List<Doa>? = null
)
