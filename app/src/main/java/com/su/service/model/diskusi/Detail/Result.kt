package com.su.service.model.diskusi.Detail

import com.google.gson.annotations.SerializedName

data class Result(
	@SerializedName("melihat_sebagai")
	val melihatSebagai: String? = null,
	val pagesize: Int? = null,
	val kategori: String? = null,
	val page: Int? = null,
	val keyword: String? = null,
	val diskusi: Diskusi? = null
)
