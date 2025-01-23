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
import androidx.navigation.fragment.findNavController
import com.tirupati.vendor.R
import com.tirupati.vendor.adapters.SpinnerAdapter
import com.tirupati.vendor.databinding.FragmentFirstDetailPageBinding
import com.tirupati.vendor.helper.isValidGST
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
    var selectItem=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args=arguments
        if(args!=null){
            org_name = args.getString("ORG_NAME","")
            org_contact = args.getString("ORG_CONTACT","")
            org_email = args.getString("ORG_EMAIL","")
            selectItem=args.getString("selectItem","")
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
        bindingFirstPage?.btnBackForm?.setOnClickListener{
            findNavController(). popBackStack()

        }
        bindingFirstPage?.inputUserFirstName?.setText(org_name)
        if(selectItem=="1"){
            bindingFirstPage?.ownerContactET1?.visibility=View.GONE
            bindingFirstPage?.ownerFullNameET1?.visibility=View.GONE
            bindingFirstPage?.ownerPanET1?.visibility=View.GONE
            bindingFirstPage?.emailOwnerET1?.visibility=View.GONE
        }
        else{
            bindingFirstPage?.POCEmailIdET1?.visibility=View.GONE
            bindingFirstPage?.PANEt1?.visibility=View.GONE
            bindingFirstPage?.GSTnumberEt1?.visibility=View.GONE

        }
        bindingFirstPage?.btnFirstDone?.setOnClickListener {
            if(validateUI(bindingFirstPage!!)){
                val args = Bundle()
                args.putString("ORG_NAME",org_name)
                args.putString("ORG_CONTACT",org_contact)
                args.putString("ORG_EMAIL",org_email)
                args.putString("selectItem",selectItem)
//                NEW ADDED

                args.putString("NAME_OF_ORG",bindingFirstPage!!.inputUserFirstName.text.toString())
                args.putString("LEGAL_ENTITY",bindingFirstPage!!.legalEntity.text.toString())
                if(selectItem=="1"){
                    args.putString("OWNER_NAME",bindingFirstPage?.ETpodName?.text.toString())
                    args.putString("OWNER_CONTACT",bindingFirstPage?.POCWhatsAppET?.text.toString())
                    args.putString("OWNER_EMAIL",bindingFirstPage?.POCEmailIdET?.text.toString())
                }
                else{
                    args.putString("OWNER_NAME",bindingFirstPage?.ownerFullNameET?.text.toString())
                    args.putString("OWNER_CONTACT",bindingFirstPage?.ownerContactET?.text.toString())
                    args.putString("OWNER_EMAIL",bindingFirstPage?.emailOwnerET?.text.toString())
                }

                args.putString("POD_NAME",bindingFirstPage!!.ETpodName.text.toString())
                args.putString("OWNER_PAN",bindingFirstPage?.ownerPanET?.text.toString())
                args.putString("POCEmailIdET",bindingFirstPage?.POCEmailIdET?.text.toString())
                args.putString("ORG_PAN",bindingFirstPage?.PANEt?.text.toString())
                args.putString("ORG_GST",bindingFirstPage?.GSTnumberEt?.text.toString())





                args.putString("POCWhatsAppNumber",bindingFirstPage!!.POCWhatsAppET.text.toString())
                for (key in args.keySet()) {
                    val value = args.get(key)
                    Log.d("BundleArgs", "Key: $key, Value: $value")
                }

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
            self.showAlertWithViewController(self, title: "Error", message: "Owner's Contact Number is Incorrect")
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
            showCustomDialog(requireContext(),"Name of the Organisation can't be empty!","Error")
            status = false
        }
        else if(binding.legalEntity.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"Legal Entity Type can't be empty!","Error")
            status = false

        }

        else if(binding.ownerFullNameET.text.toString().isNullOrEmpty() && selectItem!="1"){

                showCustomDialog(requireContext(), "Owner's Full Name can't be empty!", "Error")
            status = false



        }
        else if(binding.ownerContactET.text.isNullOrEmpty() && selectItem!="1"){

            showCustomDialog(
                requireContext(),
                "Owner's Contact Number can't be empty!",
                "Error"
            )
            status = false


        }
        else if(!binding.ownerContactET.text.toString().isValidPhoneNumber() && selectItem!="1"){

            showCustomDialog(requireContext(),"Owner's Contact Number is Incorrect","Error")
            status = false


        }
//        else if(binding.emailOwnerET.text.isNullOrEmpty() && selectItem!="1" ){
//            showCustomDialog(requireContext(), "Owner's Email ID is empty", "Error")
//            status = false
//
//
//        }
        else if(binding.emailOwnerET.text.length>0 && !binding.emailOwnerET.text.toString().isValidEmail() ){
            showCustomDialog(requireContext(), "Owner's Email ID is incorrect", "Error")
            status = false


        }
        else if(binding.ownerPanET.text.isNullOrEmpty() && selectItem!="1"){

            showCustomDialog(requireContext(), "Owner's PAN Number can't be empty!", "Error")
            status = false


        }
        else if(!binding.ownerPanET.text.toString().isValidPANNumber() && selectItem!="1"){

            showCustomDialog(requireContext(),"Owner's PAN Number is Incorrect","Error")
            status = false


        }


        else if(binding.ETpodName.text.isNullOrEmpty() ){

            showCustomDialog(requireContext(), "POC Name can't be empty!", "Error")
            status = false


        }

        else if(binding.POCWhatsAppET.text.isNullOrEmpty()){

            showCustomDialog(requireContext(), "POC WhatsApp Number can't be empty!", "Error")
            status = false


        }


           else if(binding.POCWhatsAppET.text.toString().isNotEmpty() && !binding.POCWhatsAppET.text.toString().isValidPhoneNumber()) {
            showCustomDialog(requireContext(), "POC WhatsApp Number is Incorrect ", "Error")
            status = false

        }
        else if((binding.POCEmailIdET.text.isEmpty() || binding.POCEmailIdET.text.isNotEmpty() && !binding.POCEmailIdET.text.toString().isValidEmail()) && selectItem=="1" ){

            showCustomDialog(requireContext(), "POC Email ID can't be empty!", "Error")
            status = false


        }
        else if(( binding.POCEmailIdET.text.isNotEmpty() && !binding.POCEmailIdET.text.toString().isValidEmail()) && selectItem=="1" ){

            showCustomDialog(requireContext(), "POC Email ID is Incorrect", "Error")
            status = false


        }
        else if(binding.GSTnumberEt.text.isNullOrEmpty() && selectItem=="1"){

            showCustomDialog(requireContext(), "Organisation's GST Number can't be empty!", "Error")
            status = false


        }
        else if(!binding.GSTnumberEt.text.toString().isValidGST() && selectItem=="1"){
            showCustomDialog(requireContext(), "GST is not Valid", "Error")
            status = false


        }

        else if(binding.PANEt.text.isNullOrEmpty() && selectItem=="1"){

            showCustomDialog(requireContext(), "Organisation's PAN Number can't be empty!", "Error")
            status = false


        }
        else if(!binding.PANEt.text.toString().isValidPANNumber() && selectItem=="1"){

            showCustomDialog(requireContext(),"Organisation's PAN Number is Incorrect","Error")
            status = false


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