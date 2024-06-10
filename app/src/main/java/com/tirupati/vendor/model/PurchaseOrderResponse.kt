package com.tirupati.vendor.model

import java.util.ArrayList

data class PurchaseOrderResponse(
    val STATUS: Boolean,
    val RESPONSEDATA: ArrayList<ResponseData>
)

data class ResponseData(
    val POID: String,
    val PO_NO: String,
    val PO_DT: String,
    val VID: String,
    val VENDOR_NAME: String,
    val ITEMID_REF: String,
    val ICODE: String,
    val ITEM_NAME: String,
    val PO_QTY: String,
    val UOMID_REF: String,
    val UOMCODE: String,
    val UOM_NAME: String,
    val RATEP_UOM: String,
    val NET_TOTAL: String,
    val AMOUNT: String,
    val COUNTER_REMARK: String?,
    val STATUS: String,
    val COUNTS: String
)
