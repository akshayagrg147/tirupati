package com.tirupati.vendor.model


data class vendorQuoationRequest(
    val UOM_ID: String,
    val TOTAL_AMOUNT: String,
    val DELIVERY_TERMS: String,
    val REMARKS: String,
    val QUANTITY: String,
    val PAYMENT_TERMS: String,
    val ITEM_ID: String,
    val RATE: String
)