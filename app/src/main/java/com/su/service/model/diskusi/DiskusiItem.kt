package com.su.service.model.diskusi

import com.google.gson.annotations.SerializedName

data class DiskusiItem(
	val cdate: String? = null,
	val author: String? = null,
	val id: String? = null,
	val title: String? = null,
	@SerializedName("title_blog")
	val titleBlog: String? = null,
	@SerializedName("b_user_admin_id")
	val bUserAdminId: String? = null,
	@SerializedName("b_user_id")
	val bUserId: String? = null,
	val status: String? = null
)
