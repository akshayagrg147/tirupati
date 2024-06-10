package com.tirupati.vendor.fragmnts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tirupati.vendor.adapters.DeliveryTermsAdapter
import com.tirupati.vendor.adapters.PaymentTermsAdapter
import com.tirupati.vendor.databinding.VendorQuotationFormBinding
import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.ui.LandingVendorActivity
import com.tirupati.vendor.viewmodels.VendorFormViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VendorQotationFragment : Fragment() {
    private var binding: VendorQuotationFormBinding? = null

    private val vendorFormVM: VendorFormViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager
    lateinit var deliverTerm:String
    lateinit var paymentTerm:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VendorQuotationFormBinding.inflate(inflater, container, false)
        binding?.btnAddPurchase?.setOnClickListener { onSubmitClicked() }

        return binding!!.root
    }

    private fun onSubmitClicked() {
        val vendorName = binding?.vendorName?.text.toString().trim()
        val organisationName = binding?.organisationName?.text.toString().trim()
        val itemDetail = binding?.itemDetail?.text.toString().trim()
        val uom = binding?.uom?.text.toString().trim()
        val hsnSacCode = binding?.hsnSacCode?.text.toString().trim()
        val quantity = binding?.quantity?.text.toString().trim()
        val rate = binding?.rate?.text.toString().trim()
        val remarks = binding?.remarks?.text.toString().trim()




    }
    private fun getuomList() {

        lifecycleScope.launch {
            var response = vendorFormVM.getUomList()

            when (response) {

                is NetworkState.Success->{
                    deliverTerm=response.body.RESPONSEDATA[0].UOMCODE

                    val customDropDownAdapter3 =
                        DeliveryTermsAdapter(requireContext(),  response.body.RESPONSEDATA)
                    binding?.deliveryTerms?.adapter = customDropDownAdapter3
                    binding?.deliveryTerms?.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View,
                                position: Int,
                                id: Long
                            ) {
                                deliverTerm=response.body.RESPONSEDATA[position].UOMCODE




                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }

                }

                is NetworkState.Error<*>->{
                    // Toast.makeText(context,response.msg.toString(),Toast.LENGTH_SHORT).show()
                }

                is NetworkState.NetworkException->{
                }
                is NetworkState.HttpErrors.InternalServerError->{
                }
                is NetworkState.HttpErrors.ResourceNotFound->{
                }
                else->{
                }
            }


        }
    }
    override fun onResume() {
        super.onResume()
        LandingVendorActivity.showIcon(true)
        LandingVendorActivity.changeTitle("Vendor Quotation Form")


    }

    override fun onPause() {
        super.onPause()
        LandingVendorActivity.showIcon(false)
        LandingVendorActivity.changeTitle("Vendor Quotation Form")

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getuomList()
        getPaymentTerms()

    }

    private fun getPaymentTerms() {
        lifecycleScope.launch {
            val header = HashMap<String, String>()
            header["Accept"] = "application/json"
            header["version"] = "1"
            header["Authorization"] = "${sessionManager.loginToken}"
            header["userID"]="${sessionManager.user?.RESPONSEDATA?.USER_ID}"
            var response = vendorFormVM.getPaymentTermItemList(header)

            when (response) {

                is NetworkState.Success->{

                    val customDropDownAdapter3 =
                        PaymentTermsAdapter(requireContext(),  response.body.RESPONSEDATA)
                    paymentTerm= response.body.RESPONSEDATA[0].NAME
                    binding?.paymentTerms?.adapter = customDropDownAdapter3
                    binding?.paymentTerms?.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View,
                                position: Int,
                                id: Long
                            ) {

                                paymentTerm=response.body.RESPONSEDATA[position].NAME


                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }

                }

                is NetworkState.Error<*>->{
                    // Toast.makeText(context,response.msg.toString(),Toast.LENGTH_SHORT).show()
                }

                is NetworkState.NetworkException->{
                }
                is NetworkState.HttpErrors.InternalServerError->{
                }
                is NetworkState.HttpErrors.ResourceNotFound->{
                }
                else->{
                }
            }


        }

    }

}