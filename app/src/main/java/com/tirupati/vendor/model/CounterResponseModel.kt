package com.tirupati.vendor.model


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class CounterResponseModel(
    @SerializedName("MESSAGE")
    var message: String? = null,
    @SerializedName("RESPONSEDATA")
    var responseData: RESPONSEDATA? = null,
    @SerializedName("STATUS")
    var status: Boolean? = null
) : Parcelable {
    @Parcelize
    data class RESPONSEDATA(
        @SerializedName("AMOUNT")
        var amount: String? = null,
        @SerializedName("ATTACHMENT_DOC")
        var attachmentDOC: String? = null,
        @SerializedName("COUNTER_REMARK")
        var counterRemark: String? = null,
        @SerializedName("COUNTS")
        var counts: String? = null,
        @SerializedName("DELIVERY_TERMS")
        var deliveryTerms: String? = null,
        @SerializedName("HSNCODE")
        var hsncCode: String? = null,
        @SerializedName("ICODE")
        var icode: String? = null,
        @SerializedName("ITEMID_REF")
        var itemIdRef: String? = null,
        @SerializedName("ITEM_NAME")
        var itemName : String? = null,
        @SerializedName("NAME_OF_ORGANIZATION")
        var nameOfOrganization: String? = null,
        @SerializedName("PAYMENT_TERMS")
        var paymentTerms: String? = null,
        @SerializedName("PO_DT")
        var podt: String? = null,
        @SerializedName("POID")
        var poid: String? = null,
        @SerializedName("PO_NO")
        var pono: String? = null,
        @SerializedName("PO_QTY")
        var poqty: String? = null,
        @SerializedName("RATEP_UOM")
        var ratepUom: String? = null,
        @SerializedName("REMARKS")
        var remarks: String? = null,
        @SerializedName("STATUS")
        var status: String? = null,
        @SerializedName("UOMCODE")
        var uomcode: String? = null,
        @SerializedName("UOMID_REF")
        var uomidRef: String? = null,
        @SerializedName("UOM_NAME")
        var uomName: String? = null,
        @SerializedName("VENDOR_NAME")
        var vendorName: String? = null,
        @SerializedName("VID")
        var vid: String? = null
    ) : Parcelable
}