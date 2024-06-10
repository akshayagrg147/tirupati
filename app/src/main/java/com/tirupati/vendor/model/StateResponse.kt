package com.tirupati.vendor.model

data class StateResponse(
    val MASSAGE: String,
    val RESPONSEDATA: ArrayList<ResponseDataPo>,
    val STATUS: Boolean
)

data class ResponseDataPo(
    val NAME: String,
    val STATUS: String,
    val STCODE: String,
    val STID: String
)