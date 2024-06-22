package com.tirupati.vendor.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tirupati.vendor.model.CounterResponseModel
import com.tirupati.vendor.network.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor(private val stateVMRepo: StateVMRepository) :
    ViewModel() {

        var counterDetails = ObservableField<CounterResponseModel.RESPONSEDATA>()

    fun callCounterApi(header: HashMap<String, String>, id: String) {

        viewModelScope.launch {
            var response = stateVMRepo.getCounter(header,id)

            when (response) {

                is NetworkState.Success -> {
                    withContext(Dispatchers.Main){
                        counterDetails.set(response.body.rESPONSEDATA)
                    }
                }

                is NetworkState.Error<*> -> {
                    // Toast.makeText(context,response.msg.toString(),Toast.LENGTH_SHORT).show()
                }

                is NetworkState.NetworkException -> {
                }

                is NetworkState.HttpErrors.InternalServerError -> {
                }

                is NetworkState.HttpErrors.ResourceNotFound -> {
                }

                else -> {
                }
            }


        }

    }
}