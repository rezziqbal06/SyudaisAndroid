package com.su.service.model.diskusi.Detail

import com.google.gson.annotations.SerializedName

data class KomenItem(
	var pengirim: String? = null,
	var cdate: String? = null,
	@SerializedName("b_user_pengirim_id")
	var bUserPengirimId: String? = null,
	var komen: String? = null,
	val id: String? = null,
	@SerializedName("d_blog_diskusi_id")
	val dBlogDiskusiId: String? = null,
	@SerializedName("title_diskusi")
	val titleDiskusi: String? = null,
	@SerializedName("title_blog")
	val titleBlog: String? = null,
	val gambar: String? = null,
	var status: String? = null
)
