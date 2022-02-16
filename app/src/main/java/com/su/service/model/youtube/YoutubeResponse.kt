package com.su.service.model.youtube

data class YoutubeResponse(
	val regionCode: String? = null,
	val kind: String? = null,
	val nextPageToken: String? = null,
	val pageInfo: PageInfo? = null,
	val etag: String? = null,
	val items: ArrayList<YoutubeItem>? = null
)
