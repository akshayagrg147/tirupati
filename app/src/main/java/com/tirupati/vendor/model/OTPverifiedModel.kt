package com.tirupati.vendor.model

data class OTPverifiedModel(
    val MESSAGE: String,
    val RESPONSEDATA: OTPRESPONSEDATA,
    val STATUS: Boolean,
    val USER_TYPE: String
)


data class OTPRESPONSEDATA(
    val LOG_TOKEN: String,
    val USER_ID: String,
    val VERSION: String,
    val VCODE: String,
    val NAME: String,
    val GSTIN: String,
    val VENDOR_LEGAL_NAME: String,
    val AADHAR_NO: String,
    val NAME_OF_ORGANIZATION: String,
    val NAME_OF_OWNER: String,
    val CONTACT_EMAIL: String,
    val OWNER_CONT_NO: String
)