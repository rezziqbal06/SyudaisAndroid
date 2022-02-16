package com.su.service.model.pelanggan

import com.google.gson.annotations.SerializedName

data class Data(
	val apisess: String? = null,
	@SerializedName("user")
	val pelanggan: Pelanggan? = null
)
