package com.tirupati.vendor.model

data class ChangePassworModel(
    var result: Int? = null,
    var msg: String? = null,
    var `data`: PasswordResponseData? = null
)

data class PasswordResponseData(
    var user_id: String? = null,
    var email: String? = null,
    var otp: Int? = null
)