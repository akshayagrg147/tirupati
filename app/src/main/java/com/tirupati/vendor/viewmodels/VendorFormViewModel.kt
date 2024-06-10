package com.tirupati.vendor.viewmodels

import androidx.lifecycle.ViewModel
import com.tirupati.vendor.model.itemListResponse
import com.tirupati.vendor.model.uomDataResponse
import com.tirupati.vendor.network.NetworkState

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VendorFormViewModel  @Inject constructor(private val stateVMRepo:StateVMRepository) : ViewModel() {


    suspend fun getPaymentTermItemList(header: HashMap<String, String>):NetworkState<itemListResponse>{
        return stateVMRepo.getPaymentTermItemList(header)
    }


    suspend fun getUomList():NetworkState<uomDataResponse>{
        return stateVMRepo.getUomList()
    }

}

