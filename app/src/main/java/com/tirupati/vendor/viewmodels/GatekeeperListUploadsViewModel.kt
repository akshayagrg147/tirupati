package com.tirupati.vendor.viewmodels

import androidx.lifecycle.ViewModel
import com.google.gson.JsonParser
import com.tirupati.vendor.helper.toRequestBody
import com.tirupati.vendor.model.UploadsDetailResponse
import com.tirupati.vendor.network.ApiService
import com.tirupati.vendor.network.NetworkCall
import com.tirupati.vendor.network.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class GatekeeperListUploadsViewModel  @Inject constructor(private val gateUploadVMRepo:GatekeeperUploadsRepository) : ViewModel() {


    suspend fun postUploadsGatekeeper(header: HashMap<String, String>,vendor_id: String,
                                      po_id: String,
                                      billNumber: String,
                                      vehicleNo: String,
                                      billDate: String,
                                      invoiceVendor:ArrayList<MultipartBody.Part?>,
                                      eWayBill:ArrayList<MultipartBody.Part?>,
                                      eInvoice: ArrayList<MultipartBody.Part?>,
                                      vendorWeightingSlips: ArrayList<MultipartBody.Part?>,
                                      uploadWeightSlipBhiwadi: ArrayList<MultipartBody.Part?>,
                                      transportbilty: ArrayList<MultipartBody.Part?>,
                                      tollReceipt: ArrayList<MultipartBody.Part?>
    ): NetworkState<UploadsDetailResponse>  {
        return gateUploadVMRepo.gateUploads(
            header,
            vendor_id,
            po_id,
            billNumber,
            vehicleNo,
            billDate,
            invoiceVendor,
            eWayBill,
            eInvoice,
            vendorWeightingSlips,
            uploadWeightSlipBhiwadi,
            transportbilty,
            tollReceipt
        )
    }

    suspend fun postUploadsSupervisor(headers: HashMap<String, String>,
                                      GEID: String,
                                      VENDORID: String,
                                      VEHICLE_NO: String,
                                      GROSSWEIGHT: String,
                                      TAREWEIGHT: String,
                                      NETWEIGHT: String,
                                      loadVehicle:ArrayList<MultipartBody.Part?>,
                                      tollReceipt: ArrayList<MultipartBody.Part?>,
                                      vehicleRc: ArrayList<MultipartBody.Part?>,
                                      driverLic: ArrayList<MultipartBody.Part?>,
                                      frontBack: ArrayList<MultipartBody.Part?>,
                                      emptyVechile: ArrayList<MultipartBody.Part?>
    ): NetworkState<UploadsDetailResponse>  {
        return gateUploadVMRepo.superVisorUploads(
            headers,
            GEID
            ,VENDORID,
            VEHICLE_NO,
            GROSSWEIGHT,
            TAREWEIGHT,
            NETWEIGHT,
            loadVehicle,tollReceipt,vehicleRc,driverLic,frontBack,emptyVechile
        )
    }

}
class GatekeeperUploadsRepository @Inject constructor(private val apiService: ApiService) : NetworkCall() {



    suspend fun gateUploads(
        headers: HashMap<String, String>,vendor_id_name: String,
                            po_id: String,
                            billNumber: String,
                            vehicleNo: String,
                            billDate: String,
                            invoiceVendor:ArrayList<MultipartBody.Part?>,
                            eWayBill:ArrayList<MultipartBody.Part?>,
                            eInvoice: ArrayList<MultipartBody.Part?>,
                            vendorWeightingSlips: ArrayList<MultipartBody.Part?>,
                            uploadWeightSlipBhiwadi: ArrayList<MultipartBody.Part?>,
                            transportbilty: ArrayList<MultipartBody.Part?>,
                            tollReceipt: ArrayList<MultipartBody.Part?>
    ): NetworkState<UploadsDetailResponse> {

        return safeApiCall {

            apiService.gateKeeperUploadsForImages(
                headers,
                vendor_id_name.toRequestBody()
                ,po_id.toRequestBody(),
                billNumber.toRequestBody(),
                vehicleNo.toRequestBody(),
                billDate.toRequestBody(),invoiceVendor,
                eWayBill,eInvoice,vendorWeightingSlips,uploadWeightSlipBhiwadi,transportbilty,tollReceipt)
        }
    }


    suspend fun superVisorUploads(
        headers: HashMap<String, String>,
        GEID: String,
        VENDORID: String,
        VEHICLE_NO: String,
        GROSSWEIGHT: String,
        TAREWEIGHT: String,
        NETWEIGHT: String,
        loadVehicle:ArrayList<MultipartBody.Part?>,
        tollReceipt: ArrayList<MultipartBody.Part?>,
        vehicleRc: ArrayList<MultipartBody.Part?>,
        driverLic: ArrayList<MultipartBody.Part?>,
        frontBack: ArrayList<MultipartBody.Part?>,
        emptyVechile: ArrayList<MultipartBody.Part?>,): NetworkState<UploadsDetailResponse> {

        return try {
            // Make API call using Retrofit suspend function
            val response =  apiService.superVisorUploadsForImages(
                headers,
                GEID.toRequestBody()
                ,VENDORID.toRequestBody(),
                VEHICLE_NO.toRequestBody(),
                GROSSWEIGHT.toRequestBody(),
                TAREWEIGHT.toRequestBody(),
                NETWEIGHT.toRequestBody(),
                loadVehicle,tollReceipt,vehicleRc,driverLic,frontBack,emptyVechile)
            NetworkState.Success(response)
        } catch (e: Exception) {
            // Handle errors and exceptions
            when (e) {
                is retrofit2.HttpException -> {
                    val jsonObject = JsonParser.parseString(e.response()?.errorBody()?.string()).asJsonObject

                    val errorMsg =  jsonObject.get("MESSAGE")?.asString ?: "Unknown error"

                    when (e.code()) {
                        400 -> NetworkState.HttpErrors.BadRequest(errorMsg)
                        401 -> NetworkState.HttpErrors.Unauthorized(errorMsg)
                        403 -> NetworkState.HttpErrors.ResourceForbidden(errorMsg)
                        404 -> NetworkState.HttpErrors.ResourceNotFound(errorMsg)
                        500 -> NetworkState.HttpErrors.InternalServerError(errorMsg)
                        502 -> NetworkState.HttpErrors.BadGateWay(errorMsg)
                        503 -> NetworkState.HttpErrors.ServiceUnavailable(errorMsg)
                        504 -> NetworkState.HttpErrors.ResourceRemoved(errorMsg)
                        else -> NetworkState.HttpErrors.WrongData(e.response()?.errorBody())
                    }
                }

                else -> NetworkState.NetworkException(e.localizedMessage ?: "Unknown network error")
            }


        }}

}
