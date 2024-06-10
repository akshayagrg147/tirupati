package com.tirupati.vendor.fragmnts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tirupati.vendor.databinding.FragmentVendorListingBinding
import com.tirupati.vendor.ui.LandingVendorActivity
import com.tirupati.vendor.viewmodels.GatekeeperListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VendorListingFragment : Fragment() {

    private var binding: FragmentVendorListingBinding? = null
    private val gateKeeperVm: GatekeeperListViewModel by viewModels()
//    var listOfAllCompany: ArrayList<VendorRESPONSEDATAX>? = null

   companion object{
        private var bindingC: FragmentVendorListingBinding? = null

    }


    override fun onResume() {
        super.onResume()
        LandingVendorActivity.showIcon(true)
        LandingVendorActivity.changeTitle("Purchase Order Approval")


    }

    override fun onPause() {
        super.onPause()
        LandingVendorActivity.showIcon(false)
        LandingVendorActivity.changeTitle("Purchase Order Approval")

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
        binding = FragmentVendorListingBinding.inflate(inflater, container, false)
//        bindingC=binding
//        contextC = requireContext()
//        titleChangeListener?.updateToolbarTitle("Your Fragment Title")

//        listingAdapterC=listingAdapter
//        callTheListApi()

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

/*

   fun callTheListApi() {
        lifecycleScope.launch {
            var response = gateKeeperVm.getCompanyList()

            when (response) {

                is NetworkState.Success -> {
                   LandingScreenGateKeeperActivity.listOfAllCompany = response.body.RESPONSEDATA
                    println(LandingScreenGateKeeperActivity.listOfAllCompany.toString())
                    val listingAdapter = MenuAdapter( requireContext(),
                        LandingScreenGateKeeperActivity.listOfAllCompany!!,object :
                            OnItemClickListGateKeeper {

                            override fun onItemClicked(pos: Int, data: VendorRESPONSEDATAX) {

                                val args = Bundle()
                                args.putParcelable("RESPONSE_DATA",data)
                                Navigation.findNavController(binding!!.root).
                                navigate(R.id.action_gateEntryFragment_to_gateEntryDetailFragment, args)


                            }
                        })
                    binding?.listVendor?.adapter=listingAdapter
                    listingAdapterC= listingAdapter
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
*/


}