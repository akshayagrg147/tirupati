package com.tirupati.vendor.model

data class LogInResponse(
    val MESSAGE: String,
    val MOBILE_NO: String,
    val OTP: String,
    val STATUS: Boolean,
    val USER_TYPE: String
)