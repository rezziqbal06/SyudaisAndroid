package com.su.service.model.diskusi.Detail

import com.google.gson.annotations.SerializedName

data class Diskusi(
	val cdate: String? = null,
	val komen: ArrayList<KomenItem>? = null,
	@SerializedName("d_blog_id")
	val dBlogId: String? = null,
	val id: String? = null,
	@SerializedName("komen_count")
	val komenCount: String? = null,
	val title: String? = null,
	@SerializedName("b_user_admin_id")
	val bUserAdminId: String? = null,
	@SerializedName("b_user_id")
	val bUserId: String? = null,
	val status: String? = null
)
