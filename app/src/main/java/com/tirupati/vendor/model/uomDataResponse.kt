package com.tirupati.vendor.model


data class uomDataResponse(
    val STATUS: Boolean,
    val RESPONSEDATA: List<UOMData>
)

data class UOMData(
    val UOMID: String,
    val UOMCODE: String,
    val DESCRIPTIONS: String
)