package com.tirupati.vendor.model

data class PurchaseOrderRequest(
    val PODATE: String,
    val LOCATION_NAME: String,
    val PO_NUMBER: String,
    val LONGITUDE: String,
    val LATITUDE: String
)
