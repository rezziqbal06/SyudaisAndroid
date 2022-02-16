package com.su.service.model.santri

import com.google.gson.annotations.SerializedName

data class Result(
	@SerializedName("santri")
	val santri: ArrayList<SantriItem>? = null,
	@SerializedName("hadir")
	val hadir: Int? = null,
	@SerializedName("berhalangan")
	val berhalangan: Int? = null
)