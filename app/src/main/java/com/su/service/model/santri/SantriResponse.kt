package com.su.service.model.santri

import com.google.gson.annotations.SerializedName


data class SantriResponse(

	@SerializedName("result")
	val result: Result? = null,

	@SerializedName("message")
	val message: String? = null,

	@SerializedName("status")
	val status: Int? = null
)