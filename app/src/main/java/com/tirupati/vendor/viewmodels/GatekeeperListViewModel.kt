package com.tirupati.vendor.viewmodels

import androidx.lifecycle.ViewModel
import com.tirupati.vendor.model.GateKeeperEntryModel
import com.tirupati.vendor.model.OTPverifiedModel
import com.tirupati.vendor.model.POID_RESPONSE
import com.tirupati.vendor.model.PurchaseOrderResponse
import com.tirupati.vendor.model.VendorListModel
import com.tirupati.vendor.network.ApiService
import com.tirupati.vendor.network.NetworkCall
import com.tirupati.vendor.network.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GatekeeperListViewModel  @Inject constructor(private val logInVMRepo:GatekeeperListRepository) : ViewModel() {


    suspend fun getCompanyList():NetworkState<VendorListModel>{
        return logInVMRepo.getListOfroles()
    }


    suspend fun getCompanyPoId(otp:String):NetworkState<POID_RESPONSE>{
        return logInVMRepo.getPoId(otp)
    }

    suspend fun getSuperViserData(headers: HashMap<String, String>,):NetworkState<GateKeeperEntryModel>{
        return logInVMRepo.getListSuperviser(headers)
    }
    suspend fun getpoList(headers: HashMap<String, String>,):NetworkState<PurchaseOrderResponse>{
        return logInVMRepo.getpoList(headers)
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
    suspend fun getListSuperviser(headers: HashMap<String, String>,): NetworkState<GateKeeperEntryModel> {

        return safeApiCall {

            apiService.getSuperVisor(headers)
        }
    }
    suspend fun getpoList(headers: HashMap<String, String>,): NetworkState<PurchaseOrderResponse> {

        return safeApiCall {

            apiService.getpoList(headers)
        }
    }



}
