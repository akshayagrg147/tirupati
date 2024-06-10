package com.tirupati.vendor.fragmnts

import android.os.Bundle
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
import com.tirupati.vendor.adapters.CityAdapter
import com.tirupati.vendor.adapters.StateAdapter
import com.tirupati.vendor.databinding.FragmentSecondDetailPageBinding
import com.tirupati.vendor.helper.isValidGST
import com.tirupati.vendor.helper.isValidPINcode
import com.tirupati.vendor.helper.showCustomDialog
import com.tirupati.vendor.model.CityList
import com.tirupati.vendor.model.ResponseDataPo
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.utils.isValidPANNumber
import com.tirupati.vendor.viewmodels.statesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddressDetailFragment : Fragment() {


    private val statesviewModel: statesViewModel by viewModels()

    private var bindingSecondPage: FragmentSecondDetailPageBinding? = null
    var stateName: ArrayList<ResponseDataPo> = ArrayList()
    var cityList: ArrayList<CityList> = ArrayList()

    var org_name =""
    var org_contact = ""
    var org_email = ""
    var business_org_name = ""
    var ownerName = ""
    var legalName = ""
    var ownerContact = ""
    var ownerEmail =""
    var podName = ""
    var ownerPan = ""
    var pocWhatsapp = ""

    var stateCode = ""
    var cityCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args= arguments
        if (args!=null){
             org_name = args.getString("ORG_NAME","")
             org_contact = args.getString("ORG_CONTACT","")
             org_email = args.getString("ORG_EMAIL","")
             business_org_name = args.getString("NAME_OF_ORG","")
             legalName = args.getString("LEGAL_ENTITY","")
             ownerName = args.getString("OWNER_NAME","")
             ownerContact = args.getString("OWNER_CONTACT","")
             ownerEmail =args.getString("OWNER_EMAIL","")
             podName = args.getString("POD_NAME","")
             ownerPan = args.getString("OWNER_PAN","")
             pocWhatsapp = args.getString("POCWhatsAppNumber","")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        bindingSecondPage = FragmentSecondDetailPageBinding.inflate(inflater, container, false)

        bindingSecondPage?.btnSecondDone?.setOnClickListener {
            if(validateUI(bindingSecondPage!!)){
                val args = Bundle()
                args.putString("ORG_NAME",org_name)
                args.putString("ORG_CONTACT",org_contact)
                args.putString("ORG_EMAIL",org_email)
                args.putString("NAME_OF_ORG",business_org_name)
                args.putString("LEGAL_ENTITY",legalName)
                args.putString("OWNER_NAME",ownerName)
                args.putString("OWNER_CONTACT",ownerContact)
                args.putString("OWNER_EMAIL",ownerEmail)
                args.putString("POD_NAME",podName)
                args.putString("OWNER_PAN",ownerPan)
                args.putString("POCWhatsAppNumber",pocWhatsapp)
//              New Added

                args.putString("ADDRESS",bindingSecondPage!!.addressET.text.toString())
                args.putString("COUNTRY",bindingSecondPage!!.countrySpinner.text.toString())
//                args.putString("STATE",bindingSecondPage!!.statesList.text.toString())
//                args.putString("CITY",bindingSecondPage!!.citiesList.text.toString())
                args.putString("STATE",stateCode)
                args.putString("CITY",cityCode)
                args.putString("PIN",bindingSecondPage!!.etPinCode.text.toString())
                args.putString("ORG_GST",bindingSecondPage!!.GSTnumberEt.text.toString())
                args.putString("ORG_PAN",bindingSecondPage!!.PANEt.text.toString())


                Navigation.findNavController(bindingSecondPage!!.root)
                    .navigate(R.id.action_secondDetailPageFragment_to_thirdDetailPageFragment, args)
            }
        }
        bindingSecondPage!!.countrySpinner.setText("India")
        getStatesfromServer()
        return bindingSecondPage!!.root
    }


    /*fun getCountry() {
//        var accountType = ["Cash Credit","Over Draft","Current Account"]
        val countryList: ArrayList<String> = ArrayList()

        countryList.add("India")

        val spinnerAdapter = SpinnerAdapter(requireActivity(), R.layout.item_spinner_row, countryList)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_row)

        bindingSecondPage?.countrySpinner?.setAdapter(spinnerAdapter);
        bindingSecondPage?.countrySpinner?.setKeyListener(null);
        bindingSecondPage?.countrySpinner?.setOnTouchListener(View.OnTouchListener { v, event ->
            (v as AutoCompleteTextView).showDropDown()
            false
        })
*/

       /* bindingSecondPage!!.countrySpinner.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {


            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    when (position) {
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        })*/


    private fun getCities(Stateid: String) {
        stateCode = Stateid
        bindingSecondPage!!.citiesList.text.clear()
        lifecycleScope.launch {
            var response = statesviewModel.getCitiesList(Stateid)

            when (response) {

                is NetworkState.Success->{
                    cityList= response.body.RESPONSEDATA
//                    initializeAdapter(branchList)

                    cityAdapter(cityList)

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
//    addressET
//    countrySpinner
//    statesList
//    citiesList
//    etPinCode
//    GSTnumberEt
//    PANEt
//    adhaarET


    private fun validateUI(binding: FragmentSecondDetailPageBinding): Boolean {

        var status = false
        if (binding.addressET.text.isNullOrEmpty()) {
            showCustomDialog(requireContext(),"Address can't be empty","Error")
            status = false
        }
        else if(binding.countrySpinner.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"Country can't be empty","Error")
            status = false

        }

        else if(binding.statesList.text.toString().isNullOrEmpty()){
            showCustomDialog(requireContext(),"States can't be empty!!","Error")
            status = false

        }
        else if(binding.citiesList.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"City can't be empty","Error")
            status = false

        }
        else if(binding.etPinCode.text.toString().isNullOrEmpty()){
            showCustomDialog(requireContext(),"PIN Code can't be empty","Error")
            status = false

        }

        else if(!binding.etPinCode.text.toString().isValidPINcode()){
            showCustomDialog(requireContext(),"PIN code not Valid","Error")
            status = false

        }
        else if(binding.GSTnumberEt.text.toString().isNullOrEmpty()){
            showCustomDialog(requireContext(),"GST number can't be empty","Error")
            status = false

        }

        else if(!binding.GSTnumberEt.text.toString().isValidGST()){
            showCustomDialog(requireContext(),"GST is not valid","Error")
            status = false

        }
        else if(binding.PANEt.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"PAN number can't be Empty","Error")
            status = false

        }
        else if(!binding.PANEt.text.toString().isValidPANNumber()){
            showCustomDialog(requireContext(),"PAN number is not valid","Error")
            status = false

        }
       /* else if(binding.adhaarET.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"Adhaar Number can't be empty","Error")
            status = false
        }
           else if(!binding.adhaarET.text.toString().isValidAdharCard()){
                showCustomDialog(requireContext(),"Adhaar Number is not valid","Error")
                status = false

        }*/


        else {
            return true
        }
        return status
    }
    private fun cityAdapter(cityList: ArrayList<CityList>) {

        val spinnerAdapter =
            CityAdapter(requireActivity(), R.layout.item_spinner_row, cityList)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_row)

        bindingSecondPage?.citiesList?.setAdapter(spinnerAdapter);
        // Remove setting key listener to null
        // bindingSecondPage?.statesList?.setKeyListener(null);
        bindingSecondPage?.citiesList?.threshold=1
        bindingSecondPage?.citiesList?.setOnClickListener {
            (it as AutoCompleteTextView).showDropDown()
        }

        bindingSecondPage?.citiesList?.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedModel = parent.adapter.getItem(position) as CityList
            // Do whatever you want with the selected model object here
            bindingSecondPage?.citiesList?.setText(selectedModel.NAME)
            cityCode= selectedModel.CITYID
//            getCities(stateId)
        }

     /*   bindingSecondPage!!.citiesList.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    when (position) {
                        position ->{

                        }

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        })*/
    }

/*

    fun getStates(stateName: ArrayList<RESPONSEDATA>) {
        val spinnerAdapter =
            StateAdapter(requireActivity(), R.layout.item_spinner_row, stateName)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_row)

        bindingSecondPage?.statesList?.setAdapter(spinnerAdapter);
        bindingSecondPage?.statesList?.setKeyListener(null);
        bindingSecondPage?.statesList?.setOnTouchListener(View.OnTouchListener { v, event ->
            (v as AutoCompleteTextView).showDropDown()
            false
        })


        bindingSecondPage?.statesList?.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedModel = parent.adapter.getItem(position) as RESPONSEDATA
            // Do whatever you want with the selected model object here
            bindingSecondPage?.statesList?.setText(selectedModel.NAME)
            var Stateid = selectedModel.STID
            getCities(Stateid)
//            Toast.makeText(this, "Selected: ${selectedModel.name}", Toast.LENGTH_SHORT).show()
        }



    }
*/
fun getStates(stateName: ArrayList<ResponseDataPo>) {
    val spinnerAdapter =
        StateAdapter(requireActivity(), R.layout.item_spinner_row, stateName)
    spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_row)
        bindingSecondPage!!.spinner.adapter = spinnerAdapter
    bindingSecondPage!!.statesList.setOnClickListener {
//        bindingSecondPage!!.spinner.visibility = View.VISIBLE
        bindingSecondPage!!.spinner.visibility = View.VISIBLE
        bindingSecondPage!!.spinner.performClick()

    }
    bindingSecondPage!!.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view:View?, position: Int, id: Long) {
            // Handle spinner item selection here
//            val selectedItem = parent?.getItemAtPosition(position).toString()
            // Update EditText text
            bindingSecondPage!!.spinner.visibility=View.GONE

            bindingSecondPage!!.statesList.setText(spinnerAdapter.getItem(position).NAME)
            getCities(spinnerAdapter.getItem(position).STID)

//            bindingSecondPage!!.statesList.setText(spinnerAdapter.getItem(position).NAME)
//            bindingSecondPage!!.spinner.visibility = View.GONE
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            // Do nothing
        }
    }

}

    private fun getStatesfromServer() {
        lifecycleScope.launch {
            var response = statesviewModel.getStatesList()

            when (response) {

                is NetworkState.Success->{
                    stateName= response.body.RESPONSEDATA
//                    initializeAdapter(branchList)
                    getStates(stateName)
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