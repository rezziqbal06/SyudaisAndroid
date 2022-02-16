package com.su.service.model.youtube

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class YoutubeItem(
	val snippet: Snippet? = null,
	val kind: String? = null,
	val etag: String? = null,
	val id: Id? = null
) : Parcelable
