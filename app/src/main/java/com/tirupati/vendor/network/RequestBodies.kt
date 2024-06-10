package com.tirupati.vendor.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


class RequestBodies {
//    Refresh Token
    @Parcelize
    class TokenRefresh (
        @SerializedName("token")
        val token: String
    ):Parcelable

//   Log In Auth
    @Parcelize
    class LogInReq (
    @SerializedName("userName")
    val username: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("companyCode")
    val company_code: String
): Parcelable

    class States


    @Parcelize
    class GetConsignee (
        @SerializedName("cityId")
        val cityId: Int

    ): Parcelable

    @Parcelize
    class validateLR (
        @SerializedName("docketNo")
        val docketNo: String,
        @SerializedName("branchId")
        val branchId: Int

    ): Parcelable



    @Parcelize
    class MenuWithRights (
        @SerializedName("roleId")
        val roleId: Int,

        @SerializedName("userId")
        val userId: Int,

        @SerializedName("moduleId")
        val moduleId: Int
    ): Parcelable
    @Parcelize
    class EmptyBody (
    ): Parcelable


    data class CustomerWithCity(
        @SerializedName("cityId")
        var cityId: Int,
        @SerializedName("custId")
        var custId:Int

    )

    data class CustomerCity(
        @SerializedName("cityId")
        var cityId: Int

    )
//TO SAVE A FULL RESPONSE OF DOCKET



    data class SaveDocketBody(
        val chargewt: Int,
        val consigneeAddressId: Int,
        val consigneeId: Int,
        val consignorAddressId: Int,
        val consignorId: Int,
        val createdBy: Int,
        val ctrNo: String,
        val custId: Int,
        val destBranchId: Int,
        val docketDate: String,
        val docketId: Int,
        val docketNo: String,
        val ewayBillMappinglist: List<EwayBillMappinglist>,
        val fromCityId: Int,
        val isActive: Boolean,
        val laneId: Int,
        val loadedById: Int,
        val loader: String,
        val mLiftedFrom: String,
        val mLiftedPhoneNo: String,
        val materialDeliveredTo: String,
        val mobileEntryYN: Boolean,
        val obdno: String,
        val originBranchId: Int,
        val pkgsNo: Int,
        val productId: Int,
        val sourceDocketId: Int,
        val splSvcReq: String,
        val toCityId: Int
    )
    data class EwayBillMappinglist(
        val ewaybillDate: String,
        val ewaybillNo: String,
        val expiryDate: String,
        val invoiceDocketMapping: List<InvoiceDocketMapping>
    )


    data class InvoiceDocketMapping(
        val actWeight: Int,
        val declVal: Int,
        val docketInvoiceNo: String,
        val elrProductId: Int,
        val hsnCode: String,
        val invoiceDate: String,
        val pkgsNo: Int,
        val productId: Int,
        val totCft: Int,
        val volB: Int,
        val volH: Int,
        val volL: Int
    )



    data class getValidatedLR(
        val branchId: Int,
        val custId: Int,
        val dlyBranchId: Int,
        val docketDate: String,
        val docketNo: String,
        val paybasisId: Int,
        val tamNo: String
    )
}
