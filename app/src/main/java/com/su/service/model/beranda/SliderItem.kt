package com.su.service.model.beranda

import com.google.gson.annotations.SerializedName

data class SliderItem(
	val image: String? = null,
	@SerializedName("is_active")
	val isActive: String? = null,
	val jenis: String? = null,
	val utype: String? = null,
	val caption: String? = null,
	val id: String? = null,
	val title: String? = null,
	val priority: String? = null,
	val url: String? = null
)
