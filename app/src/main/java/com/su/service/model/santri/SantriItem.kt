package com.su.service.model.santri

import com.google.gson.annotations.SerializedName

data class SantriItem(

	@SerializedName("keterangan")
    var keterangan: String? = null,

	@SerializedName("nama")
	val nama: String? = null,

	@SerializedName("id")
	val id: String? = null,

	@SerializedName("email")
	val email: String? = null,

	@SerializedName("jam")
	var jam: String? = null
)