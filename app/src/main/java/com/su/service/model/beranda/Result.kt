package com.su.service.model.beranda

data class Result(
	val slider: List<SliderItem?>? = null,
	val kegiatan: List<KegiatanItem?>? = null,
	val kajian: ArrayList<KajianItem?>? = null
)
