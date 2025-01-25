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
    @SerializedName("Customer_code") val customerCode: String ? = null,
    @SerializedName("Customer_name") val customerName: String ? = null,
    @SerializedName("Broker") val broker: String? = null,
    @SerializedName("Direct_booking") val directBooking: String ? = null,
    @SerializedName("Item_Name") val itemName: String ? = null,
    @SerializedName("Sale_Order_No") val saleOrderNo: String ? = null,
    @SerializedName("Sales_Order_Date") val salesOrderDate: String ? = null,
    @SerializedName("CUSTOMERPONO") val customerPONo: String ? = null,
    @SerializedName("PAYMENT_TERMS") val paymentTerms: String ? = null,
    @SerializedName("PAYMENT_CREDIT_DAYS") val paymentCreditDays: String ? = null,
    @SerializedName("OrderValidity") val orderValidity: String ? = null,
    @SerializedName("Name_Of_Organization") val nameOfOrganization: String ? = null,
    @SerializedName("UOM") val uom: String ? = null,
    @SerializedName("Quantity") val quantity: String ? = null,
    @SerializedName("Rate") val rate: String ? = null,
    @SerializedName("Width") val width: String ? = null,
    @SerializedName("Discount_Rate") val discountRate: String ? = null,
    @SerializedName("Discount_Amount") val discountAmount: String ? = null,
    @SerializedName("Taxable_Amount") val taxableAmount: String ? = null,
    @SerializedName("Total_Amount") val totalAmount: String ? = null,
    @SerializedName("Taxamount") val taxAmount: String ? = null,
    @SerializedName("Payment_terms") val paymentTermsDuplicate: String ? = null,
    @SerializedName("Bill_To") val billTo: String? = null,
    @SerializedName("Ship_To") val shipTo: String ? = null,
    @SerializedName("STATE") val state: String ? = null,
    @SerializedName("DRIVER_NAME") val driverName: String ? = null,
    @SerializedName("DRIVER_CONTACT_NO") val driverContactNo: String ? = null,
    @SerializedName("TRUCK_NO") val truckNo: String ? = null,
    @SerializedName("batchDetails") val batchDetails: List<BatchDetail> = emptyList(),
    @SerializedName("otherDetails") val otherDetails: List<OtherDetail> = emptyList()
) : Parcelable


@Parcelize
 data class BatchDetail(
    @SerializedName("BATCHID") val batchId: String? = null,
    @SerializedName("BATCH_CODE") val batchCode: String? = null,
    @SerializedName("SOID_REF") val soIdRef: String? = null,
    @SerializedName("SCID_REF") val scIdRef: String? = null,
    @SerializedName("PALLET_WEIGHT") val palletWeight: String? = null,
    @SerializedName("NET_WEIGHT") val netWeight: String? = null,
    @SerializedName("GROSS_WEIGHT") val grossWeight: String? = null,
    @SerializedName("COIL_NO") val coilNo: String? = null,
): Parcelable

@Parcelize
data class OtherDetail(
    @SerializedName("Quality_Document") val qualityDocument: String? = null,
    @SerializedName("Sales_Invoice") val salesInvoice: String? = null,
    @SerializedName("Proformal_invoice") val proformaInvoice: String? = null,
    @SerializedName("E_Way_Bill") val eWayBill: String? = null,
) : Parcelable

