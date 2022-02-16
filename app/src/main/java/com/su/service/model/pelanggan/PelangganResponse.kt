package com.su.service.model.pelanggan

import com.google.gson.annotations.SerializedName

data class PelangganResponse(
	@SerializedName("result")
	val data: Data? = null,
	val message: String? = null,
	val status: Int? = null
)
