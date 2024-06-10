package com.tirupati.vendor.model

data class POID_RESPONSE(
    val RESPONSEDATA: ArrayList<POIDRESPONSEDATA>,
    val STATUS: Boolean
)

data class POIDRESPONSEDATA(
    val CREATED_VID_REF: String,
    val DISPATCHED: String,
    val POID: String,
    val PO_NO: String,
    val SLID_REF: String
)