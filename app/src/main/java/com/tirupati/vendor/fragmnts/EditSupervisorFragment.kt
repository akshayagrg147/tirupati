package com.tirupati.vendor.fragmnts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.tirupati.vendor.R
import com.tirupati.vendor.databinding.FragmentEditSupervisorBinding
import com.tirupati.vendor.model.GateRESPONSEDATA
import com.tirupati.vendor.ui.LandingScreenCustomerActivity
import com.tirupati.vendor.utils.toast

class EditSupervisorFragment : Fragment() {
    lateinit var bundledData: Bundle
    var supervisorData: GateRESPONSEDATA? =null
    private var binding: FragmentEditSupervisorBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null) {

            bundledData = args
            Log.d("bndldata", bundledData.toString())

            supervisorData = args.getParcelable("Gate_RESPONSE_DATA")!!

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditSupervisorBinding.inflate(inflater, container, false)
        binding!!.etVendor.setText( supervisorData?.NAME ?:"")
        binding!!.etGateNumber.setText(supervisorData?.GE_NO ?:"")
        binding!!.etGross.setText(supervisorData?.GROSS_WEIGHT ?:"")
        binding!!.etNet.setText(supervisorData?.NET_WEIGHT ?:"")
        binding!!.etTare.setText(supervisorData?.TARE_WEIGHT ?:"")
        binding!!.btnContinueEdit.setOnClickListener {
            if (validateUI(binding!!)) {
                val args = Bundle()
                args.putParcelable("SUPERVISOR_DATA",supervisorData);
                args.putString("SUPERVISORE_VENDOR",binding!!.etVendor.text.toString().trim())
                args.putString("SUPERVISORE_GATE_NO",binding!!.etGateNumber.text.toString().trim())
                args.putString("SUPERVISORE_GROSS",binding!!.etGross.text.toString().trim())
                args.putString("SUPERVISORE_TARE",binding!!.etTare.text.toString().trim())
                args.putString("SUPERVISORE_NET",binding!!.etNet.text.toString().trim())
                args.putString("VEHICLE_NO",supervisorData!!.VEHICLE_NO)
                Navigation.findNavController(binding!!.root).
                navigate(R.id.action_editSupervisorFragment_to_uploadSupervisorPicturesFragment, args)
            }
        }

        return binding!!.root
    }
    override fun onResume() {
        super.onResume()
        LandingScreenCustomerActivity.changeTitle("Checklist Against GE")


    }

    override fun onPause() {
        super.onPause()
        LandingScreenCustomerActivity.changeTitle("Checklist Against GE")

    }

    private fun validateUI(binding: FragmentEditSupervisorBinding): Boolean {

        var status = false
        if (binding.etVendor.text.isNullOrEmpty()) {
            toast("Vendor can't be empty")
            status = false
        }else if(binding.etGateNumber.text.isNullOrEmpty()){
            toast("Gate Entry Number can't be empty")
            status = false
        }
        else if(binding.etGross.text.isNullOrEmpty()){
            toast("Gross Weight can't be empty")
            status = false
        }
        else if(binding.etTare.text.isNullOrEmpty()){
            toast("Tare Weight can't be empty")
            status = false
        }
        else if(binding.etNet.text.isNullOrEmpty()){
            toast("Net Weight can't be empty")
            status = false
        }
        else {
            return true
        }
        return status
    }

    override fun onDestroy() {
        super.onDestroy()
        LandingScreenCustomerActivity.changeIcon()
    }

}