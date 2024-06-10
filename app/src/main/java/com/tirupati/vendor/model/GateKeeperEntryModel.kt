package com.tirupati.vendor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class GateKeeperEntryModel(
    val RESPONSEDATA: ArrayList<GateRESPONSEDATA>,
    val STATUS: Boolean
):Parcelable

@Parcelize
data class GateRESPONSEDATA(
    val GEID: String,
    val GE_NO: String,
    val GROSS_WEIGHT: String,
    val NAME: String,
    val NET_WEIGHT: String,
    val SLID_REF: String,
    val TARE_WEIGHT: String,
    val VEHICLE_NO: String
):Parcelable