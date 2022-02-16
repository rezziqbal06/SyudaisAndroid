package com.su.service.model.dzikir

import com.google.gson.annotations.SerializedName

data class Result(
	@SerializedName("dzikir_count")
	val dzikirCount: String? = null,
	val pagesize: Int? = null,
	val kategori: String? = null,
	val page: Int? = null,
	val keyword: String? = null,
	val dzikir: List<Dzikir>? = null
)
