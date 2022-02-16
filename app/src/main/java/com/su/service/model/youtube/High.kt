package com.su.service.model.youtube

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class High(
	val width: Int? = null,
	val url: String? = null,
	val height: Int? = null
): Parcelable
