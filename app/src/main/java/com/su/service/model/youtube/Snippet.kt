package com.su.service.model.youtube

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Snippet(
	val publishedAt: String? = null,
	val description: String? = null,
	val title: String? = null,
	val thumbnails: Thumbnails? = null,
	val channelId: String? = null,
	val channelTitle: String? = null,
	val liveBroadcastContent: String? = null
) : Parcelable
