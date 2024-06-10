package com.tirupati.vendor.helper.interfaces

import com.tirupati.vendor.model.GateRESPONSEDATA
import com.tirupati.vendor.model.VendorRESPONSEDATAX

fun interface OnItemClickListGateKeeper{
    fun onItemClicked(pos: Int,data: VendorRESPONSEDATAX)
}
fun interface OnItemClickListSupervisor{
    fun onItemClicked(pos: Int,data: GateRESPONSEDATA)
}

fun interface OnItemClickWithData {
    fun onItemClicked(pos: Int, data: String)
}

