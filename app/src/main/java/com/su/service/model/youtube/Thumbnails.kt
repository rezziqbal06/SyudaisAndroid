package com.su.service.model.youtube

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Thumbnails(
	val jsonMemberDefault: JsonMemberDefault? = null,
	val high: High? = null,
	val medium: Medium? = null
): Parcelable
