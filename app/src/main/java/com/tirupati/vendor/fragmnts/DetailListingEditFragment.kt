package com.tirupati.vendor.fragmnts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tirupati.vendor.databinding.FragmentDetailListingEditBinding

class DetailListingEditFragment : Fragment() {
    private var binding: FragmentDetailListingEditBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailListingEditBinding.inflate(inflater, container, false)


        // Inflate the layout for this fragment
        return binding!!.root
    }

}