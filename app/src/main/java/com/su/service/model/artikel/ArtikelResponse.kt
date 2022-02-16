package com.su.service.model.artikel

import com.google.gson.annotations.SerializedName

data class ArtikelResponse(
	@SerializedName("result")
	val result: Result? = null,
	val message: String? = null,
	val status: Int? = null
)
