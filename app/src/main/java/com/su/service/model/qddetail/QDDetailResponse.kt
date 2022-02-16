package com.su.service.model.qddetail

import com.google.gson.annotations.SerializedName

data class QDDetailResponse(

	@field:SerializedName("result")
	val result: Result? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)