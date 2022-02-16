package com.su.service.model.absen

import com.google.gson.annotations.SerializedName

data class Absen(
	@SerializedName("jam_lembur_pulang")
	val jamLemburPulang: String? = null,
	@SerializedName("jam_masuk")
	val jamMasuk: String? = null,
	@SerializedName("jam_pulang")
	val jamPulang: String? = null,
	@SerializedName("b_jadwal_id")
	val bJadwalId: String? = null,
	val tgl: String? = null,
	val catatan: String? = null,
	val sia: String? = null,
	@SerializedName("b_user_pengabsen_id")
	val bUserPengabsenId: String? = null,
	@SerializedName("is_kesiangan")
	val isKesiangan: String? = null,
	@SerializedName("b_user_id")
	val bUserId: String? = null,
	@SerializedName("jam_lembur_masuk")
	val jamLemburMasuk: String? = null
)
