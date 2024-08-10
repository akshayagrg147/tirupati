package com.tirupati.vendor.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.tirupati.vendor.helper.toRequestBody
import com.tirupati.vendor.model.GateKeeperEntryModel
import com.tirupati.vendor.model.OTPverifiedModel
import com.tirupati.vendor.model.POID_RESPONSE
import com.tirupati.vendor.model.PoDetailsResponse
import com.tirupati.vendor.model.PurchaseOrderRequest
import com.tirupati.vendor.model.PurchaseOrderResponse
import com.tirupati.vendor.model.UploadsDetailResponse
import com.tirupati.vendor.model.VendorListModel
import com.tirupati.vendor.model.vendorQuoationRequest
import com.tirupati.vendor.network.ApiService
import com.tirupati.vendor.network.NetworkCall
import com.tirupati.vendor.network.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class GatekeeperListViewModel  @Inject constructor(private val logInVMRepo:GatekeeperListRepository) : ViewModel() {


    suspend fun getCompanyList():NetworkState<VendorListModel>{
        return logInVMRepo.getListOfroles()
    }


    suspend fun getCompanyPoId(otp:String):NetworkState<POID_RESPONSE>{
        return logInVMRepo.getPoId(otp)
    }

    suspend fun getSuperViserData(headers: HashMap<String, String>):NetworkState<GateKeeperEntryModel>{
        return logInVMRepo.getListSuperviser(headers)
    }
    suspend fun getpoList(headers: HashMap<String, String>):NetworkState<PurchaseOrderResponse>{
        return logInVMRepo.getpoList(headers)
    }
    suspend fun getPoDetail(headers: HashMap<String, String>,id:String):NetworkState<PoDetailsResponse>{
        return logInVMRepo.getPoDetail(headers,id)
    }
    suspend fun saveVendorQuationR(
        headers: HashMap<String, String>,
        formaData: MultipartBody.Part?,
        body: vendorQuoationRequest
    ):NetworkState<UploadsDetailResponse>{
        return logInVMRepo.saveVendorQuoation(headers,formaData,body)
    }

    suspend fun passDispatchOrder(headers: HashMap<String, String>, formaData: MultipartBody.Part?, request:PurchaseOrderRequest):NetworkState<UploadsDetailResponse>{
        return logInVMRepo.passDispatchOrder(headers,formaData,request)
    }



}
class GatekeeperListRepository @Inject constructor(private val apiService: ApiService) : NetworkCall() {



    suspend fun getListOfroles(): NetworkState<VendorListModel> {

        return safeApiCall {

            apiService.getVandor()
        }
    }

    suspend fun getOTPVerified(otp:String,mobile:String,userType:String): NetworkState<OTPverifiedModel> {

        return safeApiCall {

            apiService.getVerifiedOTP( otp,mobile,userType)
        }
    }

    suspend fun getPoId(id_company:String): NetworkState<POID_RESPONSE> {

        return safeApiCall {

            apiService.getVandorTypeCompany(id_company)
        }
    }
    suspend fun getListSuperviser(headers: HashMap<String, String>): NetworkState<GateKeeperEntryModel> {

        return safeApiCall {

            apiService.getSuperVisor(headers)
        }
    }
    suspend fun getpoList(headers: HashMap<String, String>): NetworkState<PurchaseOrderResponse> {

        return safeApiCall {

            apiService.getpoList(headers)
        }
    }
    suspend fun getPoDetail(headers: HashMap<String, String>,id:String): NetworkState<PoDetailsResponse> {

        return safeApiCall {

            apiService.getPoDetails(headers,id)
        }
    }

    suspend fun saveVendorQuoation(
        headers: HashMap<String, String>,
        formaData: MultipartBody.Part?,
        body: vendorQuoationRequest,
    ): NetworkState<UploadsDetailResponse> {
        val uomId = body.UOM_ID.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val totalAmount = body.TOTAL_AMOUNT.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val deliveryTerms = body.DELIVERY_TERMS.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val remarks = body.REMARKS.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val quantity = body.QUANTITY.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val paymentTerms = body.PAYMENT_TERMS.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val itemId = body.ITEM_ID.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val rate = body.RATE.toRequestBody("multipart/form-data".toMediaTypeOrNull())


//        return safeApiCall {
//
//            apiService.saveVendorQuoation(headers,formaData,UOM_ID,TOTAL_AMOUNT,DELIVERY_TERMS,REMARKS,QUANTITY,PAYMENT_TERMS,ITEM_ID,RATE)
//        }

        //formData.put("USERLOG_FILE", file.get(0).getName()); // Remove this line
        return try {
            // Make API call using Retrofit suspend function
            val response = apiService.saveVendorQuoation(headers, formaData, uomId, totalAmount, deliveryTerms, remarks, quantity, paymentTerms, itemId, rate)
            NetworkState.Success(response)
        } catch (e: Exception) {
            // Handle errors and exceptions
            when (e) {
                is retrofit2.HttpException -> {
                    val errorMsg = e.response()?.errorBody()?.string() ?: "Unknown error"
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


        }
    }
    suspend fun passDispatchOrder(headers: HashMap<String, String>,formaData:MultipartBody.Part?,request:PurchaseOrderRequest): NetworkState<UploadsDetailResponse> {
        val podate = request.PODATE.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val po_number = request.PO_NUMBER.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val location_name = request.LOCATION_NAME.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val lat = request.LATITUDE.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val lng = request.LONGITUDE.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        return try {
            // Make API call using Retrofit suspend function
            val response =  apiService.dispatchOrder(headers,formaData!!,podate,po_number,location_name,lat,lng)
            NetworkState.Success(response)
        } catch (e: Exception) {
            // Handle errors and exceptions
            when (e) {
                is retrofit2.HttpException -> {
                    val errorMsg = e.response()?.errorBody()?.string() ?: "Unknown error"
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


        }


    }



}
