package com.tirupati.vendor.model

data class getCitiesResponse(
    val MASSAGE: String,
    val RESPONSEDATA: ArrayList<CityList>,
    val STATUS: Boolean
)


data class CityList(
    val CITYID: String,
    val NAME: String
)