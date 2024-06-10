package com.tirupati.vendor.viewmodels

import androidx.lifecycle.ViewModel
import com.tirupati.vendor.model.LogInResponse
import com.tirupati.vendor.model.OTPverifiedModel
import com.tirupati.vendor.model.StateResponse
import com.tirupati.vendor.model.getCitiesResponse
import com.tirupati.vendor.network.ApiService
import com.tirupati.vendor.network.NetworkCall
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.network.RequestBodies

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogInViewModel  @Inject constructor(private val logInVMRepo:LogInVMRepository) : ViewModel() {


    suspend fun getLogIn(gst_id:String):NetworkState<LogInResponse>{
        return logInVMRepo.getLogIn(gst_id)
    }


    suspend fun getOtpVerified(otp:String,mobile:String,typeUser:String):NetworkState<OTPverifiedModel>{
        return logInVMRepo.getOTPVerified(otp,mobile,typeUser)
    }



}
class LogInVMRepository @Inject constructor(private val apiService: ApiService) : NetworkCall() {



    suspend fun getLogIn(gst_id:String): NetworkState<LogInResponse> {

        return safeApiCall {

            apiService.getLogIn(gst_id)
        }
    }

    suspend fun getOTPVerified(otp:String,mobile:String,userType:String): NetworkState<OTPverifiedModel> {

        return safeApiCall {

            apiService.getVerifiedOTP( otp,mobile,userType)
        }
    }



}
