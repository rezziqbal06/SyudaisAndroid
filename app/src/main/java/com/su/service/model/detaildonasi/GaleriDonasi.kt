package com.su.service.model.detaildonasi

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GaleriDonasi(
    val id: String? = null,
    val gambar: String? = null,
    val thumb: String? = null
) : Parcelable