package com.su.service.model.artikel

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("artikel_count")
    val artikelCount: String? = null,
    @SerializedName("artikel")
    val artikel: List<Artikel>? = emptyList(),
    val nextPage: Int? = null
)