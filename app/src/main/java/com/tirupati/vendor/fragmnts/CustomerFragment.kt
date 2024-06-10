package com.tirupati.vendor.fragmnts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.tirupati.vendor.R
import com.tirupati.vendor.adapters.SuperviserMenuAdapter
import com.tirupati.vendor.databinding.FragmentCustomerBinding
import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.helper.interfaces.OnItemClickListSupervisor
import com.tirupati.vendor.model.GateRESPONSEDATA
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.ui.LandingScreenCustomerActivity
import com.tirupati.vendor.ui.LandingVendorSActivity
import com.tirupati.vendor.viewmodels.GatekeeperListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CustomerFragment : Fragment() {

    private var binding: FragmentCustomerBinding? = null
    private val gateKeeperVm: GatekeeperListViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager
//    var listOfAllCompany: ArrayList<VendorRESPONSEDATAX>? = null

    companion object{
        private var bindingS: FragmentCustomerBinding? = null
        var listingAdapterS: SuperviserMenuAdapter?=null
        var contextS: Context? = null
        fun reloadUi(){
            if(listingAdapterS!=null){

                println(LandingScreenCustomerActivity.listOfAllCompanySuperviser.size)
                val listingAdapterCus = SuperviserMenuAdapter( contextS!!,
                    LandingScreenCustomerActivity.listOfAllCompanySuperviser!!,object :
                        OnItemClickListSupervisor {

                        override fun onItemClicked(pos: Int, data: GateRESPONSEDATA) {

                            val args = Bundle()
                            args.putParcelable("GateResponse",data)
                            Navigation.findNavController(bindingS!!.root).
                            navigate(R.id.action_customerFragment_to_editSupervisorFragment, args)


                        }
                    })
                bindingS?.listSuperOpts?.adapter=listingAdapterCus

            }
        }
    }


    override fun onResume() {
        super.onResume()
        LandingVendorSActivity.showIcon(true)
        LandingVendorSActivity.changeTitle("Checklist Against")


    }

    override fun onPause() {
        super.onPause()
        LandingVendorSActivity.showIcon(false)
        LandingVendorSActivity.changeTitle("Checklist Against")

    }

    /* override fun onDestroy() {
         super.onDestroy()
         LandingScreenGateKeeperActivity.showIcon(false)

     }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomerBinding.inflate(inflater, container, false)
        bindingS=binding
        contextS = requireContext()
//        titleChangeListener?.updateToolbarTitle("Your Fragment Title")

//        listingAdapterC=listingAdapter
        callTheListApiForSupervisor()

        return binding!!.root


    }
    /* override fun onAttach(context: Context) {
         super.onAttach(context)
         if (context is ToolbarTitleChangeListener) {
             titleChangeListener = context
         } else {
             throw RuntimeException("$context must implement ToolbarTitleChangeListener")
         }
     }*/


    fun callTheListApiForSupervisor() {
        lifecycleScope.launch {

            val header = HashMap<String, String>()
            header["Accept"] = "application/json"
            header["version"] = "1"
            header["Authorization"] = "${sessionManager.loginToken}"
            header["userID"]="${sessionManager.user?.RESPONSEDATA?.USER_ID}"

            var response = gateKeeperVm.getSuperViserData(header)

            when (response) {

                is NetworkState.Success -> {
                    LandingScreenCustomerActivity.listOfAllCompanySuperviser = response.body.RESPONSEDATA
                    println(LandingScreenCustomerActivity.listOfAllCompanySuperviser.toString())
                    val listingAdapter = SuperviserMenuAdapter( requireContext(),
                        LandingScreenCustomerActivity.listOfAllCompanySuperviser!!,object :
                            OnItemClickListSupervisor {

                            override fun onItemClicked(pos: Int, data: GateRESPONSEDATA) {
                                val args = Bundle()
                                args.putParcelable("Gate_RESPONSE_DATA",data)
                                Navigation.findNavController(binding!!.root).
                                navigate(R.id.action_customerFragment_to_editSupervisorFragment, args)


                            }
                        })
                    binding?.listSuperOpts?.adapter=listingAdapter
                    listingAdapterS= listingAdapter
//                    binding?.listOpts?.adapter!!.notifyDataSetChanged()



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