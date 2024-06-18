package com.tirupati.vendor.fragmnts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tirupati.vendor.R
import com.tirupati.vendor.databinding.FragmentPurchaseOrderClickBinding
import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.model.ResponseData
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.ui.LandingVendorSActivity
import com.tirupati.vendor.viewmodels.GatekeeperListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class PurchaseOrderClickFragment : Fragment() {
    private var mListener: OnItemSelectedListener? = null

    var objectResponseData:ResponseData?=null
    private var binding: FragmentPurchaseOrderClickBinding? = null
    private val gateKeeperVm: GatekeeperListViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<ResponseData>("myObjectKey")?.let { myObject ->
            objectResponseData=myObject
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        passingStatusData(objectResponseData)
        callingApiPoDetails(objectResponseData?.POID?:"")

    }

    private fun callingApiPoDetails(poNo: String) {
        val header = HashMap<String, String>()
        header["Accept"] = "application/json"
        header["version"] = "1"
        header["Authorization"] = "${sessionManager.loginToken}"
        header["userID"]="${sessionManager.user?.RESPONSEDATA?.USER_ID}"
        lifecycleScope.launch {
            var response = gateKeeperVm.getPoDetail(header,poNo)

            when (response) {

                is NetworkState.Success -> {
               if(!response.body.STATUS)
                   return@launch
                    binding?.apply {
                        statusText.text = response.body.RESPONSEDATA.STATUS
                        statusDate.text = response.body.RESPONSEDATA.PO_DT
                        poNumberValue.text = response.body.RESPONSEDATA.PO_NO
                        vendorValue.text = response.body.RESPONSEDATA.VENDOR_NAME
                        orgNameValue.text = response.body.RESPONSEDATA.NAME_OF_ORGANIZATION
                        itemValue.text = response.body.RESPONSEDATA.ITEM_NAME
                        uomValue.text = response.body.RESPONSEDATA.UOM_NAME
                        hsnSacCodeValue.text = response.body.RESPONSEDATA.HSNCODE
                        quantityValue.text = response.body.RESPONSEDATA.PO_QTY
                        rateValue.text = response.body.RESPONSEDATA.RATEP_UOM
                        amountValue.text = response.body.RESPONSEDATA.AMOUNT
                        paymentTermsValue.text = response.body.RESPONSEDATA.PAYMENT_TERMS
                        deliveryTermsValue.text = response.body.RESPONSEDATA.DELIVERY_TERMS
                        remarksValue.text = response.body.RESPONSEDATA.REMARKS
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

    fun passingStatusData(item: ResponseData?) {
        when (item?.STATUS) {
            "Pending" -> {
                val color = ContextCompat.getColor(
                    requireContext(),
                    com.tirupati.vendor.R.color.blue_00A0FA
                )
                binding?.statusIcon?.setBackgroundResource(R.drawable.pending)
                binding?.statusText?.setText("Pending")
                binding?.statusText?.setTextColor(color)
            }

            "Approved" -> {
                binding?.statusIcon?.setBackgroundResource(R.drawable.approved)
                binding?.statusText?.setText("Approved")
                val color = ContextCompat.getColor(
                    requireContext(),
                    com.tirupati.vendor.R.color.green_18BE46
                )
                binding?.statusText?.setTextColor(color)
            }

            "Dispatched" -> {
                binding?.statusIcon?.setBackgroundResource(R.drawable.dispatched)
                binding?.statusText?.setText("Dispatched")
                val color =
                    ContextCompat.getColor(requireContext(), com.tirupati.vendor.R.color.orange)
                binding?.statusText?.setTextColor(color)
            }

            "Rejected" -> {
                binding?.statusIcon?.setBackgroundResource(R.drawable.rejected)
                binding?.statusText?.setText("Rejected")
                val color =
                    ContextCompat.getColor(requireContext(), com.tirupati.vendor.R.color.red_DA3A36)
                binding?.statusText?.setTextColor(color)
            }

            "Collector" -> {
                binding?.statusIcon?.setBackgroundResource(R.drawable.collecter)
                binding?.statusText?.setText("Collector")
                val color = ContextCompat.getColor(
                    requireContext(),
                    com.tirupati.vendor.R.color.yellow_FFB800
                )
                binding?.statusText?.setTextColor(color)
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPurchaseOrderClickBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding?.root
    }
    override fun onResume() {
        super.onResume()
        LandingVendorSActivity.showIcon(false)
        LandingVendorSActivity.changeTitle("Purchase Order")


    }

    override fun onPause() {
        super.onPause()
        LandingVendorSActivity.showIcon(false)
        LandingVendorSActivity.changeTitle("Purchase Order")

    }


    interface OnItemSelectedListener {
        fun onItemSelected(item: String?)
    }


}