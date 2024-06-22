package com.tirupati.vendor.model


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class CounterResponseModel(
    @SerializedName("MESSAGE")
    var mESSAGE: String? = null,
    @SerializedName("RESPONSEDATA")
    var rESPONSEDATA: RESPONSEDATA? = null,
    @SerializedName("STATUS")
    var sTATUS: Boolean? = null
) : Parcelable {
    @Parcelize
    data class RESPONSEDATA(
        @SerializedName("AMOUNT")
        var aMOUNT: String? = null,
        @SerializedName("ATTACHMENT_DOC")
        var aTTACHMENTDOC: String? = null,
        @SerializedName("COUNTER_REMARK")
        var cOUNTERREMARK: String? = null,
        @SerializedName("COUNTS")
        var cOUNTS: String? = null,
        @SerializedName("DELIVERY_TERMS")
        var dELIVERYTERMS: String? = null,
        @SerializedName("HSNCODE")
        var hSNCODE: String? = null,
        @SerializedName("ICODE")
        var iCODE: String? = null,
        @SerializedName("ITEMID_REF")
        var iTEMIDREF: String? = null,
        @SerializedName("ITEM_NAME")
        var iTEMNAME: String? = null,
        @SerializedName("NAME_OF_ORGANIZATION")
        var nAMEOFORGANIZATION: String? = null,
        @SerializedName("PAYMENT_TERMS")
        var pAYMENTTERMS: String? = null,
        @SerializedName("PO_DT")
        var pODT: String? = null,
        @SerializedName("POID")
        var pOID: String? = null,
        @SerializedName("PO_NO")
        var pONO: String? = null,
        @SerializedName("PO_QTY")
        var pOQTY: String? = null,
        @SerializedName("RATEP_UOM")
        var rATEPUOM: String? = null,
        @SerializedName("REMARKS")
        var rEMARKS: String? = null,
        @SerializedName("STATUS")
        var sTATUS: String? = null,
        @SerializedName("UOMCODE")
        var uOMCODE: String? = null,
        @SerializedName("UOMID_REF")
        var uOMIDREF: String? = null,
        @SerializedName("UOM_NAME")
        var uOMNAME: String? = null,
        @SerializedName("VENDOR_NAME")
        var vENDORNAME: String? = null,
        @SerializedName("VID")
        var vID: String? = null
    ) : Parcelable
}