package com.tirupati.vendor.viewmodels

import androidx.lifecycle.ViewModel
import com.tirupati.vendor.model.CounterResponseModel
import com.tirupati.vendor.model.StateResponse
import com.tirupati.vendor.model.getCitiesResponse
import com.tirupati.vendor.model.itemListResponse
import com.tirupati.vendor.model.uomDataResponse
import com.tirupati.vendor.network.ApiService
import com.tirupati.vendor.network.NetworkCall
import com.tirupati.vendor.network.NetworkState

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class statesViewModel  @Inject constructor(private val stateVMRepo:StateVMRepository) : ViewModel() {


    suspend fun getStatesList():NetworkState<StateResponse>{
        return stateVMRepo.getStateLists()
    }


    suspend fun getCitiesList(stateId: String):NetworkState<getCitiesResponse>{
        return stateVMRepo.getCitiesLists(stateId)
    }

}
class StateVMRepository @Inject constructor(private val apiService: ApiService) : NetworkCall() {



    suspend fun getStateLists(): NetworkState<StateResponse> {

        return safeApiCall {

            apiService.getStates()
        }
    }

    suspend fun getCitiesLists(stateId: String): NetworkState<getCitiesResponse> {

        return safeApiCall {

            apiService.getCitiesFromStates(stateId)
        }
    }
    suspend fun getPaymentTermItemList(
        header: HashMap<String, String>)
        : NetworkState<itemListResponse> {

        return safeApiCall {

            apiService.getitemList(header)
        }
    }
    suspend fun getUomList(): NetworkState<uomDataResponse> {

        return safeApiCall {

            apiService.getuomList()
        }
    }

    suspend fun getCounter(header: HashMap<String, String>,id:String):NetworkState<CounterResponseModel>{
        return safeApiCall {
            apiService.getCounter(header,id)
        }
    }


}
