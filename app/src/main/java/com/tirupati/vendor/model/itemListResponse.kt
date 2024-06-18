package com.tirupati.vendor.model


data class ResponseDataItem(
    val ITEMID: String="",
    val NAME: String="",
    val ICODE: String="",
    val HSNID: String="",
    val HSNDESCRIPTION: String=""
)

data class itemListResponse(
    val STATUS: Boolean,
    val RESPONSEDATA: List<ResponseDataItem>
)
