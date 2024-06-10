package com.tirupati.vendor.model

data class OTPverifiedModel(
    val MESSAGE: String,
    val RESPONSEDATA: OTPRESPONSEDATA,
    val STATUS: Boolean,
    val USER_TYPE: String
)


data class OTPRESPONSEDATA(
    val EMAIL: String,
    val LOG_TOKEN: String,
    val MONO1: String,
    val USER_ID: String,
    val USER_NAME: String,
    val VERSION: String
)