package com.tirupati.vendor.fragmnts

import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.tirupati.vendor.R
import com.tirupati.vendor.adapters.SpinnerAdapter
import com.tirupati.vendor.databinding.FragmentFirstDetailPageBinding
import com.tirupati.vendor.helper.isValidPhoneNumber
import com.tirupati.vendor.helper.showCustomDialog
import com.tirupati.vendor.utils.isValidEmail
import com.tirupati.vendor.utils.isValidPANNumber
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BusinessDetailFragment : Fragment() {


    private var bindingFirstPage: FragmentFirstDetailPageBinding? = null
    var org_name = ""
    var org_contact = ""
    var org_email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args=arguments
        if(args!=null){
            org_name = args.getString("ORG_NAME","")
            org_contact = args.getString("ORG_CONTACT","")
            org_email = args.getString("ORG_EMAIL","")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindingFirstPage = FragmentFirstDetailPageBinding.inflate(inflater, container, false)
        bindingFirstPage!!.ownerPanET.setFilters(bindingFirstPage!!.ownerPanET.getFilters() + InputFilter.AllCaps())
        legalEntity()
        bindingFirstPage?.btnFirstDone?.setOnClickListener {
            if(validateUI(bindingFirstPage!!)){
                val args = Bundle()
                args.putString("ORG_NAME",org_name)
                args.putString("ORG_CONTACT",org_contact)
                args.putString("ORG_EMAIL",org_email)
//                NEW ADDED

                args.putString("NAME_OF_ORG",bindingFirstPage!!.inputUserFirstName.text.toString())
                args.putString("LEGAL_ENTITY",bindingFirstPage!!.legalEntity.text.toString())
                args.putString("OWNER_NAME",bindingFirstPage!!.ownerFullNameET.text.toString())
                args.putString("OWNER_CONTACT",bindingFirstPage!!.ownerContactET.text.toString())
                args.putString("OWNER_EMAIL",bindingFirstPage!!.emailOwnerET.text.toString())
                args.putString("POD_NAME",bindingFirstPage!!.ETpodName.text.toString())
                args.putString("OWNER_PAN",bindingFirstPage!!.ownerPanET.text.toString())
                args.putString("POCWhatsAppNumber",bindingFirstPage!!.POCWhatsAppET.text.toString())


                Navigation.findNavController(bindingFirstPage!!.root)
                    .navigate(R.id.action_firstDetailPageFragment_to_secondDetailPageFragment, args)
            }
        }





        return bindingFirstPage!!.root
    }

//    inputUserFirstName
//    legalEntity
//    ownerFullNameET
//    ownerContactET
//    emailOwnerET
//    ownerPanET
//    POCWhatsAppET
    /*
    * if self.organizationNameTextField.text == ""{
            self.showAlertWithViewController(self, title: "Error", message: "Name of the organisation can't be empty!")
        }else if self.legalEntityTextField.text == ""{
            self.showAlertWithViewController(self, title: "Error", message: "Legal entity type can't be empty!")
        }else if self.ownerNameTextField.text == ""{
            self.showAlertWithViewController(self, title: "Error", message: "Owner's Full Name can't be empty!")
        }else if self.legalEntityTextField.text == ""{
            self.showAlertWithViewController(self, title: "Error", message: "Legal entity type can't be empty!")
        }else if self.contactNumberTextField.text == ""{
            self.showAlertWithViewController(self, title: "Error", message: "Owner's Contact Number can't be empty!")
        }else if self.contactNumberTextField.text?.isValidPhone == false{
            self.showAlertWithViewController(self, title: "Error", message: "Owner's Contact Number is incorrect")
        }
        else if ((self.emailIDTextField.text?.count ?? 0) > 0) && (self.emailIDTextField.text?.isValidEmail == false){
            self.showAlertWithViewController(self, title: "Error", message: "Owner's Email ID is incorrect")
        }

        else if ((self.whatappNumberTextField.text?.count ?? 0) > 0) && (self.whatappNumberTextField.text?.isValidPhone == false){
            self.showAlertWithViewController(self, title: "Error", message: "POC Whatsapp Number is incorrect")
        }
        else if self.pocNumberTextField.text == ""{
            self.showAlertWithViewController(self, title: "Error", message: "Owner's PAN Card can't be empty!")
        }
        else if self.pocNumberTextField.text?.isValidPanNumber == false{
            self.showAlertWithViewController(self, title: "Error", message: "Owner's PAN Card is incorrect")
        }
    *
    * */



    private fun validateUI(binding: FragmentFirstDetailPageBinding): Boolean {

        var status = false
        if (binding.inputUserFirstName.text.isNullOrEmpty()) {
            showCustomDialog(requireContext(),"Name of the organisation can't be empty!","Error")
            status = false
        }
        else if(binding.legalEntity.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"Legal entity type can't be empty!","Error")
            status = false

        }

        else if(binding.ownerFullNameET.text.toString().isNullOrEmpty()){
            showCustomDialog(requireContext(),"Owner's Full Name can't be empty!","Error")
            status = false

        }
        else if(binding.ownerContactET.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"Owner's Contact Number can't be empty!","Error")
            status = false

        }
        else if(!binding.ownerContactET.text.toString().isValidPhoneNumber()){
            showCustomDialog(requireContext(),"Owner's Contact Number is incorrect","Error")
            status = false

        }

        else if(binding.emailOwnerET.text.length>0 && !binding.emailOwnerET.text.toString().isValidEmail()){

                showCustomDialog(requireContext(),"Owner's Email ID is incorrect","Error")
                status = false

        }

        else if(binding.ownerPanET.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"Owner's PAN number can't be empty","Error")
            status = false

        }
        else if(!binding.ownerPanET.text.toString().isValidPANNumber()){
            showCustomDialog(requireContext(),"Owner's PAN Card is incorrect","Error")
            status = false

        }
        else if(!binding.POCWhatsAppET.text.isNullOrEmpty()){
            if(!binding.POCWhatsAppET.text.toString().isValidPhoneNumber()){
                showCustomDialog(requireContext(),"POC Whatsapp Number is incorrect","Error")
                status = false

            }else{
            status=true
            }
        }


        else {
            return true
        }
        return status
    }

    fun UpdateData(){


        Log.d("TV",bindingFirstPage?.legalEntity?.text.toString())
    }

    fun legalEntity() {
        val legalEntityArray: ArrayList<String> = ArrayList()
//        "Proprietorship (Individual/HUF)","Firm/LLP","Company (OPC/Pvt Ltd/Ltd)"]
        legalEntityArray.add("Proprietorship (Individual/HUF)")
        legalEntityArray.add("Firm/LLP")
        legalEntityArray.add("Company (OPC/Pvt Ltd/Ltd)")
        var spinnerAdapter = SpinnerAdapter(requireActivity(), R.layout.item_spinner_row, legalEntityArray)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_row)
        /*bindingFirstPage!!.spinner.adapter = spinnerAdapter
        bindingFirstPage!!.spinner.setOnItemSelectedListener(object :
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
        bindingFirstPage?.legalEntity?.setAdapter(spinnerAdapter);
        bindingFirstPage?.legalEntity?.setKeyListener(null);
        bindingFirstPage?.legalEntity?.setOnTouchListener(OnTouchListener { v, event ->
            (v as AutoCompleteTextView).showDropDown()
            false
        })
        UpdateData()
    }


}