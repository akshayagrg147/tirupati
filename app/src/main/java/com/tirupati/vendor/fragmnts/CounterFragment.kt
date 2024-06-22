package com.tirupati.vendor.fragmnts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tirupati.vendor.databinding.FragmentCounterBinding
import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.ui.LandingVendorSActivity
import com.tirupati.vendor.viewmodels.CounterViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CounterFragment : Fragment() {
    @Inject
    lateinit var sessionManager: SessionManager

    private val viewModel: CounterViewModel by viewModels()
    private val binding by lazy { FragmentCounterBinding.inflate(layoutInflater).apply {
        model = viewModel
    } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments!=null){
            val header = HashMap<String, String>()
            header["Accept"] = "application/json"
            header["version"] = "1"
            header["Authorization"] = "${sessionManager.loginToken}"
            header["userID"]="${sessionManager.user?.RESPONSEDATA?.USER_ID}"
            val id = arguments?.getString("id")
            viewModel.callCounterApi(header,id?:"")
        }

        binding.btnAddPurchase.setOnClickListener {
            findNavController().navigateUp()
        }
    }



    override fun onResume() {
        super.onResume()
        LandingVendorSActivity.showIcon(false)
        LandingVendorSActivity.changeTitle("Vendor Quotation Form")
    }

}