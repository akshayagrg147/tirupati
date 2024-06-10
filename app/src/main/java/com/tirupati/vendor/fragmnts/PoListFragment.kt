package com.tirupati.vendor.fragmnts

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.tirupati.vendor.R
import com.tirupati.vendor.databinding.FragmentPoListBinding
import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.model.ResponseData
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.ui.LandingVendorActivity
import com.tirupati.vendor.ui.LandingVendorSActivity
import com.tirupati.vendor.viewmodels.GatekeeperListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PoListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class PoListFragment : Fragment() {
    private val gateKeeperVm: GatekeeperListViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager
    private var bindingS: FragmentPoListBinding? = null

    var listOfAllPo: ArrayList<ResponseData> = arrayListOf()


    companion object{

        var listingAdapterS: PoAdapter?=null
        var contextS: Context? = null

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingS = FragmentPoListBinding.inflate(inflater, container, false)

        callTheListPo()
        return bindingS?.root
    }

    override fun onResume() {
        super.onResume()
        LandingVendorSActivity.showIcon(true)
        LandingVendorSActivity.changeTitle("Purchase Order Approval")


    }

    override fun onPause() {
        super.onPause()
        LandingVendorSActivity.showIcon(false)
        LandingVendorSActivity.changeTitle("Purchase Order Approval")

    }


    fun callTheListPo() {
        val header = HashMap<String, String>()
        header["Accept"] = "application/json"
        header["version"] = "1"
        header["Authorization"] = "${sessionManager.loginToken}"
        header["userID"]="${sessionManager.user?.RESPONSEDATA?.USER_ID}"
        lifecycleScope.launch {
            var response = gateKeeperVm.getpoList(header)

            when (response) {

                is NetworkState.Success -> {

                   bindingS?.recyclerView?.adapter=PoAdapter(requireContext(),response.body.RESPONSEDATA)

                       bindingS?.recyclerView?.setHasFixedSize(true)

                    // Use a linear layout manager
                    val layoutManager = LinearLayoutManager(context)
                    bindingS?.recyclerView?.layoutManager = layoutManager

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LandingVendorActivity.showIcon(true)
        LandingVendorActivity.changeTitle("Purchase Order Approval")
        bindingS?.fab?.setOnClickListener{
            Navigation.findNavController(bindingS!!.root).
            navigate(R.id.currentt_to_vendorPageFragment)

        }
        callTheListPo()

    }
}
