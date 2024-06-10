package com.tirupati.vendor.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VendorListModel(
    val RESPONSEDATA: ArrayList<VendorRESPONSEDATAX>,
    val STATUS: Boolean
):Parcelable
@Parcelize
data class VendorRESPONSEDATAX(
    val NAME: String,
    val VID: String
):Parcelable