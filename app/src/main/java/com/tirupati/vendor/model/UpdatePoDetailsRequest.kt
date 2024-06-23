package com.tirupati.vendor.model

data class UpdatePoDetailsRequest(
    val RATE: String?=null,
    var PO_ID: String?=null,
    val QUANTITY: String?=null,
    val REMARKS: String?=null
)