package com.su.service.model.qddetail

import com.google.gson.annotations.SerializedName

data class Qurandaily(

	@field:SerializedName("no_surat")
	var noSurat: String? = null,

	@field:SerializedName("no_ayat")
	var noAyat: String? = null,

	@field:SerializedName("nama_surat")
	var namaSurat: String? = null,

	@field:SerializedName("id")
	var id: String? = null,

	@field:SerializedName("judul")
	var judul: String? = null,

	@field:SerializedName("is_pick")
	var isPick: String? = null
)