package com.su.service.model.quran

import com.google.gson.annotations.SerializedName

data class Surat(
	val number: String? = null,
	val name: String? = null,
    @SerializedName("number_of_ayah")
	val numberOfAyah: String? = null,
    @SerializedName("name_latin")
	val nameLatin: String? = null
)
