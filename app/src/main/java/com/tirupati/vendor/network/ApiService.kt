package com.tirupati.vendor.network


import com.tirupati.vendor.model.GateKeeperEntryModel
import com.tirupati.vendor.model.LogInResponse
import com.tirupati.vendor.model.OTPverifiedModel
import com.tirupati.vendor.model.POID_RESPONSE
import com.tirupati.vendor.model.PoDetailsResponse
import com.tirupati.vendor.model.PurchaseOrderResponse
import com.tirupati.vendor.model.StateResponse
import com.tirupati.vendor.model.UploadsDetailResponse
import com.tirupati.vendor.model.VendorListModel
import com.tirupati.vendor.model.getCitiesResponse
import com.tirupati.vendor.model.itemListResponse
import com.tirupati.vendor.model.uomDataResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


@JvmSuppressWildcards
interface ApiService {

    companion object {

        const val BASE_URL = "http://103.139.58.23:8095/api/"
            /*-------------------- DEVICE TYPE --------------------*/
        const val DEVICE_TYPE = "android"

    }


    /*=======================  APP API's  ========================*/

    @GET("V2/getState")
//    @Headers("access-key:${Constants.ACCESS_KEY}")
    suspend fun getStates(): Response<StateResponse>


//    "http://103.139.58.23:8095/api/V2/getCity?STID=19"
@FormUrlEncoded
@POST("V2/login")
@Headers("version:1")
suspend fun getLogIn(
    @Field("GST_NUMBER") gst_id: String
): Response<LogInResponse>

    @FormUrlEncoded
    @POST("V2/VerifyOTP")
    @Headers("version:1")
    suspend fun getVerifiedOTP(
        @Field("OTP") otp: String,
        @Field("MOBILE_NO") mobileNo: String,
        @Field("USER_TYPE") userType: String

    ): Response<OTPverifiedModel>

    @GET("V2/getCity")
    suspend fun getCitiesFromStates(
        @Query("STID") input: String
    ): Response<getCitiesResponse>



    @GET("V2/itemList")
    suspend fun getitemList(
        @HeaderMap headers: Map<String, String>,

    ): Response<itemListResponse>

    @GET("V2/uomList")
    suspend fun getuomList(
       
    ): Response<uomDataResponse>


    @GET("V2/vendorList")
    suspend fun getVandor(
    ): Response<VendorListModel>

    @GET("V2/gateentrychecklist")
    suspend fun getSuperVisor(
        @HeaderMap headers: Map<String, String>,
    ): Response<GateKeeperEntryModel>

    @GET("V2/getpoDetails")
    suspend fun getPoDetails(
        @HeaderMap headers: Map<String, String>,
        @Query("id") input: String,

    ): Response<PoDetailsResponse>
@Multipart
    @POST("V2/vendorQuotation")
    suspend fun saveVendorQuoation(
    @HeaderMap headers: Map<String, String>,


    @Part formaData: MultipartBody.Part?,
    @Part("UOM_ID") UOM_ID: RequestBody,
    @Part("TOTAL_AMOUNT") TOTAL_AMOUNT: RequestBody,
    @Part("DELIVERY_TERMS") DELIVERY_TERMS: RequestBody,
    @Part("REMARKS") REMARKS: RequestBody,
    @Part("QUANTITY") QUANTITY: RequestBody,
    @Part("PAYMENT_TERMS") PAYMENT_TERMS: RequestBody,
    @Part("ITEM_ID") ITEM_ID: RequestBody,
    @Part("RATE") RATE: RequestBody

): Response<UploadsDetailResponse>

    @GET("V2/getpoList")
    suspend fun getpoList(
        @HeaderMap headers: Map<String, String>,
    ): Response<PurchaseOrderResponse>


    @GET("V2/checklistAgainstGE")
    suspend fun getVandorTypeCompany(
        @Query("id") input: String
    ): Response<POID_RESPONSE>

    @Multipart
    @POST("V2/getEntry")

    suspend fun gateKeeperUploadsForImages(
        @HeaderMap headers: Map<String, String>,

        @Part("VENDOR_ID") vendor_id: RequestBody,
        @Part("PO_ID") po_id: RequestBody,
        @Part("BILL_NUMBER") billNumber: RequestBody,
        @Part("VEHICLE_NO") vehicleNo: RequestBody,
        @Part("VEDOR_BILL_DATE") billDate: RequestBody,
        @Part image_url1: ArrayList<MultipartBody.Part?>,
        @Part image_url2: ArrayList<MultipartBody.Part?>,
        @Part image_url3: ArrayList<MultipartBody.Part?>,
        @Part image_url4: ArrayList<MultipartBody.Part?>,
        @Part image_url5: ArrayList<MultipartBody.Part?>,
        @Part image_url6: ArrayList<MultipartBody.Part?>,
        @Part image_url7: ArrayList<MultipartBody.Part?>

        ): Response<UploadsDetailResponse>

    @Multipart
    @POST("V2/dispatchOrder")

    suspend fun dispatchOrder(
        @HeaderMap headers: Map<String, String>,

        @Part formdata: MultipartBody.Part,
        @Part("PODATE") podate:RequestBody,
        @Part("PO_NUMBER")  po_number: RequestBody,
        @Part("LOCATION_NAME")  location_name: RequestBody,
        @Part("LATITUDE")  lat: RequestBody,
        @Part("LONGITUDE") lng: RequestBody


    ): Response<UploadsDetailResponse>



    @Multipart
    @POST("V2/checklistAgainstGE")

    suspend fun superVisorUploadsForImages(
        @HeaderMap headers: Map<String, String>,

        @Part("GEID") GEID: RequestBody,
        @Part("VENDORID") VENDORID: RequestBody,
        @Part("VEHICLE_NO") VEHICLE_NO: RequestBody,
        @Part("GROSSWEIGHT") GROSSWEIGHT: RequestBody,
        @Part("TAREWEIGHT") TAREWEIGHT: RequestBody,
        @Part("NETWEIGHT") NETWEIGHT: RequestBody,
        @Part image_url1: ArrayList<MultipartBody.Part?>,
        @Part image_url2: ArrayList<MultipartBody.Part?>,
        @Part image_url3: ArrayList<MultipartBody.Part?>,
        @Part image_url4: ArrayList<MultipartBody.Part?>,
        @Part image_url5: ArrayList<MultipartBody.Part?> ):Response<UploadsDetailResponse>



    @Multipart
    @POST("V2/vendorRegister")

    suspend fun signUpCall(
        @HeaderMap headers: Map<String, String>,

        @Part("LATITUDE") LATITUDE: RequestBody,
        @Part("LONGITUDE") LONGITUDE: RequestBody,
        @Part("GODOWN_LOCATION") GODOWN_LOCATION: RequestBody,

        @Part("OTHERVENDORSAPPLICABLE") OTHERVENDORSAPPLICABLE: RequestBody,
        @Part("V1_NAME") V1_NAME: RequestBody,
        @Part("V1_EMAIL") V1_EMAIL: RequestBody,
        @Part("V1_CONTACT_NO") V1_CONTACT_NO: RequestBody,
        @Part("V1_AADHAR_NO") V1_AADHAR_NO: RequestBody,


        @Part("V2_NAME") V2_NAME: RequestBody,
        @Part("V2_EMAIL") V2_EMAIL: RequestBody,
        @Part("V2_CONTACT_NO") V2_CONTACT_NO: RequestBody,
        @Part("V2_AADHAR_NO") V2_AADHAR_NO: RequestBody,


        @Part("V3_NAME") V3_NAME: RequestBody,
        @Part("V3_EMAIL") V3_EMAIL: RequestBody,
        @Part("V3_CONTACT_NO") V3_CONTACT_NO: RequestBody,
        @Part("V3_AADHAR_NO") V3_AADHAR_NO: RequestBody,


        @Part("OWNER_NAME") OWNER_NAME: RequestBody,
        @Part("OWNER_CONTACT") OWNER_CONTACT: RequestBody,
        @Part("OWNER_EMAIL") OWNER_EMAIL: RequestBody,
        @Part("NAME_OF_ORGANISATION") NAME_OF_ORGANISATION: RequestBody,
        @Part("VENDOR_LEGAL_NAME") VENDOR_LEGAL_NAME: RequestBody,
        @Part("CONTACT_PERSON") CONTACT_PERSON: RequestBody,
        @Part("CONTACT_NUMBER") CONTACT_NUMBER: RequestBody,
        @Part("CONTACT_EMAIL") CONTACT_EMAIL: RequestBody,
        @Part("POC_NAME") POC_NAME: RequestBody,
        @Part("POC_NO") POC_NO: RequestBody,
        @Part("WHATSAPP_NO") WHATSAPP_NO: RequestBody,
        @Part("ADDRESS") ADDRESS: RequestBody,
        @Part("COUNTRY") COUNTRY: RequestBody,
        @Part("STATE") STATE: RequestBody,
        @Part("CITY") CITY: RequestBody,
        @Part("PIN_CODE") PIN_CODE: RequestBody,
        @Part("GSTIN") GSTIN: RequestBody,
        @Part("PANCARD_NO") PANCARD_NO: RequestBody,
        @Part("BANK_NAME") BANK_NAME: RequestBody,
        @Part("ACCOUNT_NUMBER") ACCOUNT_NUMBER: RequestBody,
        @Part("ACCOUNT_TYPE") ACCOUNT_TYPE: RequestBody,
        @Part("BANK_BRANCH") BANK_BRANCH: RequestBody,
        @Part("IFSC_CODE") IFSC_CODE: RequestBody,
        @Part("MSME_APPLICABLE") MSME_APPLICABLE: RequestBody?,
        @Part("E_INVOICE_APPLICABLE") E_INVOICE_APPLICABLE: RequestBody?,





        @Part image_url1: ArrayList<MultipartBody.Part?>,
        @Part image_url2: ArrayList<MultipartBody.Part?>,
        @Part image_url3: ArrayList<MultipartBody.Part?>,
        @Part image_url4: ArrayList<MultipartBody.Part?>,
        @Part image_url5: ArrayList<MultipartBody.Part?>,
        @Part image_url6: ArrayList<MultipartBody.Part?>,
        @Part image_url7: ArrayList<MultipartBody.Part?>,
        @Part image_url8: ArrayList<MultipartBody.Part?>,
        @Part image_url9: ArrayList<MultipartBody.Part?>,
        @Part image_url10: ArrayList<MultipartBody.Part?>,
        @Part image_url11: ArrayList<MultipartBody.Part?>,



    ):Response<UploadsDetailResponse>

}

