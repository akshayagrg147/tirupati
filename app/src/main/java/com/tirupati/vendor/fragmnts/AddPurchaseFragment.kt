package com.tirupati.vendor.fragmnts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.tirupati.vendor.R
import com.tirupati.vendor.databinding.FragmentAddPurchaseBinding


class AddPurchaseFragment : Fragment() {
    private var binding: FragmentAddPurchaseBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPurchaseBinding.inflate(inflater, container, false)
        binding?.btnAddPurchase?.setOnClickListener {
                val args = Bundle()
                Navigation.findNavController(binding!!.root).
                navigate(R.id.action_addPurchaseFragment_to_purchaseOrderFragment, args)
            }
        return binding!!.root
    }

}