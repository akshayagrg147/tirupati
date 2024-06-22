package com.tirupati.vendor.fragmnts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tirupati.vendor.R
import com.tirupati.vendor.databinding.FragmentCounterBinding
import com.tirupati.vendor.ui.LandingVendorSActivity
import com.tirupati.vendor.viewmodels.CounterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CounterFragment : Fragment() {

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
            val id = arguments?.getString("id")
            viewModel.callCounterApi(id?:"")
        }
    }



    override fun onResume() {
        super.onResume()
        LandingVendorSActivity.showIcon(false)
        LandingVendorSActivity.changeTitle("Vendor Quotation Form")
    }

}