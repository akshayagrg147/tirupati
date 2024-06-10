package com.tirupati.vendor.fragmnts

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.tirupati.vendor.R
import com.tirupati.vendor.adapters.GateKeeperPoidAdapter
import com.tirupati.vendor.databinding.FragmentGateEntryDetailBinding
import com.tirupati.vendor.helper.showCustomDialog
import com.tirupati.vendor.model.POIDRESPONSEDATA
import com.tirupati.vendor.model.VendorRESPONSEDATAX
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.ui.LandingScreenGateKeeperActivity
import com.tirupati.vendor.viewmodels.GatekeeperListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class GateEntryDetailFragment : Fragment() {
    private var bindingGateEntryFragment: FragmentGateEntryDetailBinding? = null

    private val companyPoidVM: GatekeeperListViewModel by viewModels()

    lateinit var bundledData: Bundle
    var Alldata: VendorRESPONSEDATAX? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        if (args != null) {

            bundledData = args
            Log.d("bndldata", bundledData.toString())

            Alldata = args.getParcelable("RESPONSE_DATA")!!

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        bindingGateEntryFragment = FragmentGateEntryDetailBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        if (Alldata==null){

        }
        else{
            callListShowApi()
        }
        bindingGateEntryFragment!!.billDate.setOnClickListener {
            // on below line we are getting
            // the instance of our calendar.
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                requireContext(),R.style.CustomDatePickerDialog,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val dat = ("$year-${(monthOfYear + 1)}-${dayOfMonth.toString()}")
                    bindingGateEntryFragment!!.billDate.setText(dat)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.setOnShowListener {
                val positiveButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                val negativeButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                positiveButton?.setTextColor(Color.RED) // Change to your desired color
                negativeButton?.setTextColor(Color.BLUE) // Change to your desired color
            }
            datePickerDialog.show()

        }
        bindingGateEntryFragment!!.btnGateEntryDone.setOnClickListener {
           if (validateUI(bindingGateEntryFragment!!)) {

            val args = Bundle()
             args.putParcelable("user_selected",Alldata)
              args.putString("vendor_pod",bindingGateEntryFragment!!.purchaseNo.text.toString())
              args.putString("bill_no",bindingGateEntryFragment!!.billNumber.text.toString())
              args.putString("vehicle_no",bindingGateEntryFragment!!.vehicleNumber.text.toString())
              args.putString("dateOfEntry",bindingGateEntryFragment!!.billDate.text.toString())
            Navigation.findNavController(bindingGateEntryFragment!!.root).
            navigate(R.id.action_gateEntryDetailFragment_to_photoFragment, args)
            }
        }
        return bindingGateEntryFragment!!.root
    }

    fun getAccountType(responsedata: ArrayList<POIDRESPONSEDATA>) {


        val spinnerAdapter = GateKeeperPoidAdapter(requireActivity(), R.layout.item_spinner_row, responsedata)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_row)
        bindingGateEntryFragment?.purchaseNo?.setAdapter(spinnerAdapter);
        // Remove setting key listener to null
        // bindingSecondPage?.statesList?.setKeyListener(null);
        bindingGateEntryFragment?.purchaseNo?.threshold=1
        bindingGateEntryFragment?.purchaseNo?.setOnClickListener {
            (it as AutoCompleteTextView).showDropDown()
        }

        bindingGateEntryFragment?.purchaseNo?.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedModel = parent.adapter.getItem(position) as POIDRESPONSEDATA
            // Do whatever you want with the selected model object here
            bindingGateEntryFragment?.purchaseNo?.setText(selectedModel.PO_NO)

        }




//        bindingGateEntryFragment!!.btnGateEntryDone.setOnClickListener {
//            if (validateUI(bindingGateEntryFragment!!)) {

//                val args = Bundle()
              /*  args.putParcelable("user_selected",Alldata)
                args.putString("vendor_pod",bindingGateEntryFragment!!.purchaseNo.text.toString())
                args.putString("bill_no",bindingGateEntryFragment!!.billNumber.text.toString())
                args.putString("vehicle_no",bindingGateEntryFragment!!.vehicleNumber.text.toString())
                args.putString("dateOfEntry",bindingGateEntryFragment!!.billDate.text.toString())*/
//                Navigation.findNavController(bindingGateEntryFragment!!.root).
//                navigate(R.id.action_gateEntryDetailFragment_to_photoFragment, args)
////            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LandingScreenGateKeeperActivity.changeIcon()
    }

    private fun validateUI(binding: FragmentGateEntryDetailBinding): Boolean {

        var status = false
        if (binding.purchaseNo.text.isNullOrEmpty()) {
            showCustomDialog(requireContext(),"Purchase Number can't be empty","Error")
            status = false
        }else if(binding.billNumber.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"Bill Number can't be empty","Error")
            status = false
        }
        else if(binding.vehicleNumber.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"Vehicle Number can't be empty","Error")
            status = false
        }
        else if(binding.billDate.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"Bill Date can't be empty","Error")
            status = false
        }
        else {
            return true
        }
        return status
    }
    private fun callListShowApi() {
        lifecycleScope.launch {
            var response = companyPoidVM.getCompanyPoId(Alldata!!.VID)

            when (response) {

                is NetworkState.Success->{
                    getAccountType(response.body.RESPONSEDATA)
                }

                is NetworkState.Error<*>->{
                    // Toast.makeText(context,response.msg.toString(),Toast.LENGTH_SHORT).show()
                }

                is NetworkState.NetworkException->{
                }
                is NetworkState.HttpErrors.InternalServerError->{
                }
                is NetworkState.HttpErrors.ResourceNotFound->{
                }
                else->{
                }
            }


        }

    }

}