package com.tirupati.vendor.fragmnts

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.tirupati.vendor.ProgressDialogHelper
import com.tirupati.vendor.R
import com.tirupati.vendor.databinding.FragmentChooseZoneBinding
import com.tirupati.vendor.databinding.FragmentLogInBinding
import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.helper.hidden
import com.tirupati.vendor.helper.showCustomDialog
import com.tirupati.vendor.helper.shown
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.viewmodels.LogInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject






@AndroidEntryPoint
class ChooseZoneFragment : Fragment() {

    private val logInVm: LogInViewModel by viewModels()
    private var binding: FragmentChooseZoneBinding? = null

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChooseZoneBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            val customerLayout = linearCustomer
            val vendorLayout = linearVendor
            val continueButton: TextView = btnContinue

            // Initial state
            customerLayout.isSelected = false
            vendorLayout.isSelected = true

            // Set click listeners for layouts
            customerLayout.setOnClickListener {
                customerLayout.isSelected = true
                vendorLayout.isSelected = false
            }

            vendorLayout.setOnClickListener {
                customerLayout.isSelected = false
                vendorLayout.isSelected = true
            }

            // Handle continue button click
            continueButton.setOnClickListener {
                when {
                    customerLayout.isSelected -> {
                        navigateWithSelection("1") // "1" for Customer
                    }
                    vendorLayout.isSelected -> {
                        navigateWithSelection("2") // "2" for Vendor
                    }
                    else -> {
                        Toast.makeText(context, "Please select Customer or Vendor", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun navigateWithSelection(selection: String) {
        val args = Bundle()
        args.putString("zoneselect", selection)

        Navigation.findNavController(binding!!.root)
            .navigate(R.id.action_choosezone_loginfragment, args)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

