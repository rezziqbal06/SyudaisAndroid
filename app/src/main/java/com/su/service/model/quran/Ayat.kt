package com.su.service.model.quran

data class Ayat(
    val nomor: String? = null,
    val arab: String? = null,
    val artinya: String? = null,
    var isBookmarked: Boolean? = false,
    var isSelected: Boolean = false
)