package com.tirupati.vendor.fragmnts

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController

import com.tirupati.vendor.R
import com.tirupati.vendor.databinding.FragmentMultipleAccountCustomerBinding
import com.tirupati.vendor.databinding.FragmentMultipleAccountsBinding
import com.tirupati.vendor.utils.toast


class MultipleAccountCustomerFragment : Fragment() {

    private var bindingUploads: FragmentMultipleAccountCustomerBinding? = null
    var orgName:String? = null
    var orgContact:String?= null
    var orgEmail:String? = null

    // Retrieve the new added values
    var nameOfOrg:String? = null
    var legalEntity:String? = null
    var pocName:String? = null
    var pocContact:String? = null
    var pocEmail:String? = null
    var orgGstNumber:String? = null
    var ownerPan:String? = null
    var address :String?= null
    var countrySpinner:String? = null
    var statesList:String? = null
    var citiesList:String? = null
    var pincode:String? = null
    var C1_NAME :String?= null
    var C1_EMAIL:String? = null
    var C1_CONTACT_NO:String? = null
    var C1_PAN_NO:String?= null
    var C2_NAME:String? = null
    var C2_EMAIL:String? = null
    var C2_CONTACT_NO:String? = null
    var C2_PAN_NO:String? = null
    var C3_NAME:String? = null
    var C3_EMAIL:String? = null
    var C3_CONTACT_NO:String? = null
    var C3_PAN_NO:String? = null

    var latititude:String? = null
    var longitude:String? = null
    var GODOWN_LOCATION:String? = null
    var MULTIPLE_ACCOUNTS:String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args= arguments
        if(args!=null){
             orgName = arguments?.getString("ORG_NAME")
             orgContact = arguments?.getString("ORG_CONTACT")
             orgEmail = arguments?.getString("ORG_EMAIL")

            // Retrieve the new added values
             nameOfOrg = arguments?.getString("NAME_OF_ORG")
             legalEntity = arguments?.getString("LEGAL_ENTITY")
             pocName = arguments?.getString("POC_NAME")
             pocContact = arguments?.getString("POC_CONTACT")
             pocEmail = arguments?.getString("POC_EMAIL")
             orgGstNumber = arguments?.getString("orgGstNumber")
             ownerPan = arguments?.getString("OWNER_PAN")
             address = arguments?.getString("addressET")
             countrySpinner = arguments?.getString("countrySpinner")
             statesList = arguments?.getString("statesList")
             citiesList = arguments?.getString("citiesList")
             pincode = arguments?.getString("pincode")
             C1_NAME = arguments?.getString("C1_NAME")
             C1_EMAIL = arguments?.getString("C1_EMAIL")
             C1_CONTACT_NO = arguments?.getString("C1_CONTACT_NO")
             C1_PAN_NO = arguments?.getString("C1_PAN_NO")
             C2_NAME = arguments?.getString("C2_NAME")
             C2_EMAIL = arguments?.getString("C2_EMAIL")
             C2_CONTACT_NO = arguments?.getString("C2_CONTACT_NO")
             C2_PAN_NO = arguments?.getString("C2_PAN_NO")
             C3_NAME = arguments?.getString("C3_NAME")
             C3_EMAIL = arguments?.getString("C3_EMAIL")
             C3_CONTACT_NO = arguments?.getString("C3_CONTACT_NO")
             C3_PAN_NO = arguments?.getString("C3_PAN_NO")

             latititude = arguments?.getString("latititude")
             longitude = arguments?.getString("longitude")
             GODOWN_LOCATION = arguments?.getString("GODOWN_LOCATION")
            MULTIPLE_ACCOUNTS = "1"
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        bindingUploads = FragmentMultipleAccountCustomerBinding.inflate(inflater, container, false)
        bindingUploads?.btnBackForm?.setOnClickListener{
            findNavController(). popBackStack()

        }
        bindingUploads?.btnFinalNext?.setOnClickListener {
            if (validateUI(bindingUploads!!)) {
                val args = Bundle()
                args.putString("ORG_NAME",orgName)
                args.putString("ORG_CONTACT",orgContact)
                args.putString("ORG_EMAIL",orgEmail)
//                NEW ADDED

                args.putString("NAME_OF_ORG",nameOfOrg)
                args.putString("LEGAL_ENTITY",legalEntity)
                args.putString("POC_NAME",pocName)
                args.putString("POC_CONTACT",pocContact)
                args.putString("POC_EMAIL",pocEmail)
                args.putString("orgGstNumber",orgGstNumber)
                args.putString("OWNER_PAN",ownerPan)
                args.putString("addressET",address)
                args.putString("countrySpinner",countrySpinner)
                args.putString("statesList",statesList)
                args.putString("citiesList",citiesList)
                args.putString("pincode",pincode)

                args.putString("latititude",latititude)
                args.putString("longitude",longitude.toString())
                args.putString("GODOWN_LOCATION",GODOWN_LOCATION)
                args.putString("otherApplicable","1")


                args.putString("C1_NAME",bindingUploads!!.EtVSecondName.text.toString()?:"")
                args.putString("C1_EMAIL",bindingUploads!!.secondVContact.text.toString())
                args.putString("C1_CONTACT_NO",bindingUploads!!.inputSecondVendorEmail.text.toString())
                args.putString("C1_PAN_NO",bindingUploads!!.secondVAdhar.text.toString())

                args.putString("C2_NAME",bindingUploads!!.EtVThirdName.text.toString()?:"")
                args.putString("C2_EMAIL",bindingUploads!!.thirdVContact.text.toString())
                args.putString("C2_CONTACT_NO",bindingUploads!!.inputThirdVendorEmail.text.toString())
                args.putString("C2_PAN_NO",bindingUploads!!.thirdVAdhar.text.toString())


                args.putString("C3_NAME",bindingUploads!!.EtVFourthName.text.toString()?:"")
                args.putString("C3_EMAIL",bindingUploads!!.fourthVContact.text.toString())
                args.putString("C3_CONTACT_NO",bindingUploads!!.inputfourthVendorEmail.text.toString())
                args.putString("C3_PAN_NO",bindingUploads!!.fourthVAdhar.text.toString())
                Navigation.findNavController(bindingUploads!!.root)
                    .navigate(R.id.action_multipleAccountsFragment_to_bankfragment, args)
            }

        }
        bindingUploads?.secondAdded?.setOnClickListener{
            bindingUploads?.firstLL?.visibility = View.GONE
            if (bindingUploads!!.thirdAccountll.visibility == View.VISIBLE){
                bindingUploads!!.fourthAccountll.visibility = View.VISIBLE
                bindingUploads?.secondAdded?.visibility = View.GONE
            }else{
                bindingUploads!!.thirdAccountll.visibility = View.VISIBLE
            }
        }
        bindingUploads?.thirdCancel?.setOnClickListener{
            bindingUploads?.secondAdded?.visibility = View.VISIBLE
            bindingUploads!!.thirdAccountll.visibility = View.GONE
        }
        bindingUploads?.fourthCancel?.setOnClickListener{
            bindingUploads?.secondAdded?.visibility = View.VISIBLE
            bindingUploads!!.fourthAccountll.visibility = View.GONE
        }


        return bindingUploads!!.root
    }



    private fun validateUI(bindingUploads: FragmentMultipleAccountCustomerBinding): Boolean {
        if (bindingUploads.secondAccountll.visibility == View.VISIBLE) {
            if (!isValidName(bindingUploads.EtVSecondName.text.toString())) {
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Invalid Owner Full Name",
                    "Error"
                )
                return false
            }
            if (!isValidContact(bindingUploads.secondVContact.text.toString())) {
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Invalid Owner Contact No",
                    "Error"
                )
                return false
            }
            if (!isValidEmail(bindingUploads.inputSecondVendorEmail.text.toString())) {
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Invalid Email Id",
                    "Error"
                )
                return false
            }
            if (!isValidPan(bindingUploads.secondVAdhar.text.toString())) {
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Invalid PAN Number",
                    "Error"
                )
                return false
            }
        }

        if (bindingUploads.thirdAccountll.visibility == View.VISIBLE) {
            if (!isValidName(bindingUploads.EtVThirdName.text.toString())) {
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Invalid Owner Full Name",
                    "Error"
                )
                return false
            }
            if (!isValidContact(bindingUploads.thirdVContact.text.toString())) {
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Invalid Owner Contact No",
                    "Error"
                )
                return false
            }
            if (!isValidEmail(bindingUploads.inputThirdVendorEmail.text.toString())) {
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Invalid Email Id",
                    "Error"
                )
                return false
            }
            if (!isValidPan(bindingUploads.thirdVAdhar.text.toString())) {
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Invalid PAN Number",
                    "Error"
                )
                return false
            }
        }

        if (bindingUploads.fourthAccountll.visibility == View.VISIBLE) {
            if (!isValidName(bindingUploads.EtVFourthName.text.toString())) {
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Invalid Owner Full Name",
                    "Error"
                )
                return false
            }
            if (!isValidContact(bindingUploads.fourthVContact.text.toString())) {
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Invalid Owner Contact No",
                    "Error"
                )
                return false
            }
            if (!isValidEmail(bindingUploads.inputfourthVendorEmail.text.toString())) {
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Invalid Email Id",
                    "Error"
                )
                return false
            }
            if (!isValidPan(bindingUploads.fourthVAdhar.text.toString())) {
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Invalid PAN Number",
                    "Error"
                )
                return false
            }
        }

        return true
    }


    fun isValidName(name: String): Boolean {
        return name.isNotEmpty()
    }

    fun isValidContact(contact: String): Boolean {
        val contactPattern = Regex("^\\d{10}$")
        return contactPattern.matches(contact)
    }

    fun isValidEmail(email: String): Boolean {
        val emailPattern = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        return emailPattern.matches(email)
    }

    fun isValidPan(pan: String): Boolean {
        val panPattern = Regex("^[A-Z]{5}[0-9]{4}[A-Z]\$")
        return panPattern.matches(pan)
    }




}