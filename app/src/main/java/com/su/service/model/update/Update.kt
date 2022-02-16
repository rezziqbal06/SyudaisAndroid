package com.su.service.model.update

import com.google.gson.annotations.SerializedName

data class Update(
	@SerializedName("version_name")
	val versionName: String? = null,
	val isActive: String? = null,
	val id: String? = null,
	val store: String? = null
)
