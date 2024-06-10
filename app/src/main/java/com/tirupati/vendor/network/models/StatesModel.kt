package com.tirupati.vendor.network.models

data class StatesModel(
    val MASSAGE: String,
    val RESPONSEDATA: ArrayList<StateData>,
    val STATUS: Boolean
)

data class StateData(
    val NAME: String,
    val STATUS: String,
    val STCODE: String,
    val STID: String
)