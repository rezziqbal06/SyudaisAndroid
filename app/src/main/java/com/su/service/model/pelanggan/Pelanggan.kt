package com.su.service.model.pelanggan

import com.google.gson.annotations.SerializedName

data class Pelanggan(
	var provinsi: String? = null,
	@SerializedName("api_web_token")
	var apiWebToken: String? = null,
	@SerializedName("google_id")
	var googleId: String? = null,
	var bdate: String? = null,
	var telp: String? = null,
	var umur: String? = null,
	var kabkota: String? = null,
	@SerializedName("is_guest")
	var isGuest: Int? = null,
	var adate: String? = null,
	var kelamin: String? = null,
	var poin: Int? = null,
	var password: String? = null,
	var lnama: String? = null,
	@SerializedName("fcm_token")
	var fcmToken: String? = null,
	var tlahir: String? = null,
	var kodepos: String? = null,
	var id: String? = null,
	var email: String? = null,
	var utype: String? = null,
	var ig: String? = null,
	var image: String? = null,
	@SerializedName("is_active")
	var isActive: String? = null,
	@SerializedName("is_confirmed")
	var isConfirmed: String? = null,
	@SerializedName("ig_id")
	var igId: String? = null,
	var edate: String? = null,
	var kelurahan: String? = null,
	var alamat: String? = null,
	var cdate: String? = null,
	@SerializedName("is_premium")
	var isPremium: String? = null,
	var negara: String? = null,
	var pekerjaan: String? = null,
	@SerializedName("fb_id")
	var fbId: String? = null,
	var kecamatan: String? = null,
	@SerializedName("api_reg_token")
	var apiRegToken: String? = null,
	var fb: String? = null,
	@SerializedName("apisess")
	var apiMobileToken: String? = null,
	@SerializedName("is_agree")
	var isAgree: String? = null,
	var fnama: String? = null,
	var device: String? = null
)

