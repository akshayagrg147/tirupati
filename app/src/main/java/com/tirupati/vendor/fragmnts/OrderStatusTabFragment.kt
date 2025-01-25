package com.tirupati.vendor.fragmnts

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.tirupati.vendor.R
import com.tirupati.vendor.databinding.FragmentOrderStatusTabBinding
import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.helper.hidden
import com.tirupati.vendor.helper.showCustomDialog
import com.tirupati.vendor.helper.shown
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.viewmodels.GatekeeperListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class OrderStatusTabFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private val gateKeeperVm: GatekeeperListViewModel by viewModels()
    private var onNavigate: ((Int, Bundle?) -> Unit)? = null

    @Inject
    lateinit var sessionManager: SessionManager

    // Add the ViewBinding property
    private var _binding: FragmentOrderStatusTabBinding? = null
    private val binding get() = _binding!!

    fun setOnNavigateListener(listener: (Int, Bundle?) -> Unit) {
        onNavigate = listener
    }
    private fun navigateToDetailFragment(args: Bundle) {
        Log.d("argsrecieved",Gson().toJson(args))

        onNavigate?.invoke(R.id.orderStatusTabFragment_to_orderStatusDetailFragment, args)
            ?: Log.e("NavigationError", "No navigation listener set!")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize ViewBinding
        _binding = FragmentOrderStatusTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Use binding to access views
        binding.btnSubmit.setOnClickListener {
            if(_binding?.inputUserFirstName?.text.toString().isEmpty())
            {
                showCustomDialog(requireContext(), "Unique Sales Order Number can't be empty!","Error")
                return@setOnClickListener
            }
            callCustomerStatus(_binding?.inputUserFirstName?.text.toString())
        }
    }

    private fun callCustomerStatus(orderNumber: String) {
        binding.loginProgressBar.progressBar.shown()
        lifecycleScope.launch {
            val header = HashMap<String, String>().apply {
                put("Accept", "application/json")
                put("version", "1")
                put("Authorization", sessionManager.loginToken ?: "")
                put("userID", sessionManager.user?.RESPONSEDATA?.USER_ID ?: "")
            }

            when (val response = gateKeeperVm.setOrderNumber(orderNumber,header)) {
                is NetworkState.Success -> {
                    val responseData = response.body.responseData
                    Log.d("bundleadded",Gson().toJson(responseData))
                    // Create a bundle and put the Parcelable list
                    val args = Bundle().apply {
                        putParcelable("responseData", responseData.get(0)) // Pass the first item
                    }

                    binding.loginProgressBar.progressBar.hidden()
                    navigateToDetailFragment(args)

                }

                is NetworkState.Error<*> -> {

                    binding.loginProgressBar.progressBar.hidden()
                    showCustomDialog(requireContext(), response.msg.toString(),"Error")


                }

                is NetworkState.HttpErrors.ResourceNotFound -> {

                    val jsonObject = JSONObject(response.msg.toString())
                    val message = jsonObject.optString("MESSAGE", "Unknown error")
                    showCustomDialog(requireContext(),message, "Error")
                    binding.loginProgressBar.progressBar.hidden()
                }
                else -> {
                    binding.loginProgressBar.progressBar.hidden()
                    showCustomDialog(requireContext(), "something went wrong", "Error")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding to prevent memory leaks
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderStatusTabFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
