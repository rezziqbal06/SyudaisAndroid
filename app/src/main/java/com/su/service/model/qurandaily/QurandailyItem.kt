package com.su.service.model.qurandaily

import com.google.gson.annotations.SerializedName

data class QurandailyItem(

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