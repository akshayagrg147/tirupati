package com.tirupati.vendor.fragmnts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tirupati.vendor.adapters.CustomerComplaintAdapter
import com.tirupati.vendor.databinding.FragmentCustomerComplaintTabBinding
import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.helper.hidden
import com.tirupati.vendor.helper.showCustomDialog
import com.tirupati.vendor.helper.shown
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.viewmodels.GatekeeperListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class CustomerComplaintTabFragment : Fragment() {

    private var _binding: FragmentCustomerComplaintTabBinding? = null
    private val binding get() = _binding!!
    private val gateKeeperVm: GatekeeperListViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerComplaintTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callTheListApiForSupervisor()
    }
    fun callTheListApiForSupervisor() {
        binding!!.loginProgressBar.progressBar.shown()
        lifecycleScope.launch {

            val header = HashMap<String, String>()
            header["Accept"] = "application/json"
            header["version"] = "1"
            header["Authorization"] = "${sessionManager.loginToken}"
            header["userID"]="${sessionManager.user?.RESPONSEDATA?.USER_ID}"

            var response = gateKeeperVm.getCustomerComplaintsList(header)

            when (response) {

                is NetworkState.Success -> {
                    binding!!.loginProgressBar.progressBar.hidden()
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    binding.recyclerView.adapter = CustomerComplaintAdapter( response.body.RESPONSEDATA)



                }

                is NetworkState.Error<*> -> {
                    binding!!.loginProgressBar.progressBar.hidden()
                    showCustomDialog(requireContext(), response.msg.toString(),"Error")

                    // Toast.makeText(context,response.msg.toString(),Toast.LENGTH_SHORT).show()
                }

                is NetworkState.NetworkException -> {
                    binding!!.loginProgressBar.progressBar.hidden()
                    showCustomDialog(requireContext(), response.msg.toString(),"Error")
                }

                is NetworkState.HttpErrors.InternalServerError -> {
                    binding!!.loginProgressBar.progressBar.hidden()
                    showCustomDialog(requireContext(), response.msg.toString(),"Error")
                }

                is NetworkState.HttpErrors.ResourceNotFound -> {
                    binding!!.loginProgressBar.progressBar.hidden()
                    showCustomDialog(requireContext(), response.msg.toString(),"Error")
                }

                else -> {
                    binding!!.loginProgressBar.progressBar.hidden()
                    showCustomDialog(requireContext(), "something went wrong","Error")
                }
            }


        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
