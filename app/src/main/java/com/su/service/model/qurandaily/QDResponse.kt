package com.su.service.model.qurandaily

import com.google.gson.annotations.SerializedName

data class QDResponse(

	@field:SerializedName("result")
	val result: Result? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)