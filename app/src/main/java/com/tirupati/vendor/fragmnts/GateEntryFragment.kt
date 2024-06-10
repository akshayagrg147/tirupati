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
import com.tirupati.vendor.adapters.MenuAdapter
import com.tirupati.vendor.databinding.FragmentGateEntryBinding
import com.tirupati.vendor.helper.interfaces.OnItemClickListGateKeeper
import com.tirupati.vendor.model.VendorRESPONSEDATAX
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.ui.LandingScreenGateKeeperActivity
import com.tirupati.vendor.viewmodels.GatekeeperListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class GateEntryFragment : Fragment() {

    private var binding: FragmentGateEntryBinding? = null
    private val gateKeeperVm: GatekeeperListViewModel by viewModels()
//    var listOfAllCompany: ArrayList<VendorRESPONSEDATAX>? = null

    companion object{
        private var bindingC: FragmentGateEntryBinding? = null
        var listingAdapterC:MenuAdapter?=null
        var contextC:Context? = null
        fun reloadUi(){
            if(listingAdapterC!=null){

                println(LandingScreenGateKeeperActivity.listOfAllCompany.size)
                val listingAdapter = MenuAdapter( contextC!!,
                    LandingScreenGateKeeperActivity.listOfAllCompany!!,object :
                        OnItemClickListGateKeeper {

                        override fun onItemClicked(pos: Int, data: VendorRESPONSEDATAX) {

                            val args = Bundle()
                            args.putParcelable("RESPONSE_DATA",data)
                            Navigation.findNavController(bindingC!!.root).
                            navigate(R.id.action_gateEntryFragment_to_gateEntryDetailFragment, args)


                        }
                    })
                bindingC?.listOpts?.adapter=listingAdapter

            }
        }
    }


    override fun onResume() {
        super.onResume()
        LandingScreenGateKeeperActivity.showIcon(true)

    }

    override fun onPause() {
        super.onPause()
        LandingScreenGateKeeperActivity.showIcon(false)

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
        binding = FragmentGateEntryBinding.inflate(inflater, container, false)
        bindingC=binding
        contextC = requireContext()
//        titleChangeListener?.updateToolbarTitle("Your Fragment Title")

//        listingAdapterC=listingAdapter
        callTheListApi()

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
                    binding?.listOpts?.adapter=listingAdapter
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


}