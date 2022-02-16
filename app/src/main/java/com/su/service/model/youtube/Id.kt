package com.su.service.model.youtube

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Id(
	val kind: String? = null,
	val videoId: String? = null
): Parcelable
