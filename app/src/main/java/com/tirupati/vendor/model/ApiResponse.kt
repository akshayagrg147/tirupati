package com.tirupati.vendor.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize



@Parcelize
data class ApiResponse(
    @SerializedName("STATUS") val status: Boolean,
    @SerializedName("RESPONSEDATA") val responseData: List<ResponseDataCustomer>
) : Parcelable

@Parcelize
data class ResponseDataCustomer(
    @SerializedName("Customer_code") val customerCode: String,
    @SerializedName("Customer_name") val customerName: String,
    @SerializedName("Broker") val broker: String?,
    @SerializedName("Direct_booking") val directBooking: String,
    @SerializedName("Item_Name") val itemName: String,
    @SerializedName("Sale_Order_No") val saleOrderNo: String,
    @SerializedName("Sales_Order_Date") val salesOrderDate: String,
    @SerializedName("CUSTOMERPONO") val customerPONo: String,
    @SerializedName("PAYMENT_TERMS") val paymentTerms: String,
    @SerializedName("PAYMENT_CREDIT_DAYS") val paymentCreditDays: String?,
    @SerializedName("OrderValidity") val orderValidity: String,
    @SerializedName("Name_Of_Organization") val nameOfOrganization: String,
    @SerializedName("UOM") val uom: String,
    @SerializedName("Quantity") val quantity: String?,
    @SerializedName("Rate") val rate: String?,
    @SerializedName("Width") val width: String,
    @SerializedName("Discount_Rate") val discountRate: String?,
    @SerializedName("Discount_Amount") val discountAmount: String?,
    @SerializedName("Taxable_Amount") val taxableAmount: String?,
    @SerializedName("Total_Amount") val totalAmount: String?,
    @SerializedName("Taxamount") val taxAmount: String?,
    @SerializedName("Payment_terms") val paymentTermsDuplicate: String,
    @SerializedName("Bill_To") val billTo: String,
    @SerializedName("Ship_To") val shipTo: String,
    @SerializedName("STATE") val state: String,
    @SerializedName("DRIVER_NAME") val driverName: String?,
    @SerializedName("DRIVER_CONTACT_NO") val driverContactNo: String?,
    @SerializedName("TRUCK_NO") val truckNo: String?,
    @SerializedName("batchDetails") val batchDetails: List<BatchDetail>,
    @SerializedName("otherDetails") val otherDetails: List<OtherDetail>
) : Parcelable

@Parcelize
 data class BatchDetail(
    @SerializedName("BATCHID") val batchId: String,
    @SerializedName("BATCH_CODE") val batchCode: String,
    @SerializedName("SOID_REF") val soIdRef: String,
    @SerializedName("SCID_REF") val scIdRef: String,
    @SerializedName("PALLET_WEIGHT") val palletWeight: String,
    @SerializedName("NET_WEIGHT") val netWeight: String,
    @SerializedName("GROSS_WEIGHT") val grossWeight: String,
    @SerializedName("COIL_NO") val coilNo: String
): Parcelable

@Parcelize
data class OtherDetail(
    @SerializedName("Quality_Document") val qualityDocument: String,
    @SerializedName("Sales_Invoice") val salesInvoice: String?,
    @SerializedName("Proformal_invoice") val proformaInvoice: String,
    @SerializedName("E_Way_Bill") val eWayBill: String
) : Parcelable

