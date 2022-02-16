package com.su.service.model.qurandaily

import com.google.gson.annotations.SerializedName

data class Result(

	@field:SerializedName("qurandaily")
	val qurandaily: ArrayList<QurandailyItem>? = null
)