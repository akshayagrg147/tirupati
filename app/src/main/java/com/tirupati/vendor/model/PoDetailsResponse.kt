package com.tirupati.vendor.model

data class PoDetailsResponse(
    val STATUS: Boolean,
    val MESSAGE: String,
    val RESPONSEDATA: ResponseDataPoDetail
)

data class ResponseDataPoDetail(
    val POID: String,
    val PO_NO: String,
    val PO_DT: String,
    val VID: String,
    val NAME_OF_ORGANIZATION: String,
    val VENDOR_NAME: String,
    val ATTACHMENT_DOC: String,
    val ITEMID_REF: String,
    val ICODE: String,
    val ITEM_NAME: String,
    val PO_QTY: String,
    val UOMID_REF: String,
    val UOMCODE: String,
    val UOM_NAME: String,
    val RATEP_UOM: String,
    val AMOUNT: String,
    val COUNTER_REMARK: String?,
    val HSNCODE: String,
    val PAYMENT_TERMS: String,
    val REMARKS: String,
    val DELIVERY_TERMS: String,
    val STATUS: String,
    val COUNTS: String
)
