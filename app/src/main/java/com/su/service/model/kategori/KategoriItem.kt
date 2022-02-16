package com.su.service.model.kategori

import com.google.gson.annotations.SerializedName

data class KategoriItem(
	@SerializedName("is_visible")
	var isVisible: String? = null,
	@SerializedName("is_active")
	var isActive: String? = null,
	var nama: String? = null,
	var utype: String? = null,
	var id: String? = null
)
