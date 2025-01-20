package com.tirupati.vendor.fragmnts

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController

import com.tirupati.vendor.R
import com.tirupati.vendor.databinding.FragmentMultipleAccountsBinding
import com.tirupati.vendor.utils.toast


class MultipleAccountsFragment : Fragment() {

    private var bindingUploads: FragmentMultipleAccountsBinding? = null
    var org_name =""
    var org_contact = ""
    var org_email = ""
    var business_org_name = ""
    private var ownerName = ""
    var legalName = ""
    var ownerContact = ""
    var ownerEmail =""
    var podName = ""
    var ownerPan = ""
    var podWhatsapp = ""
    var addressName = ""
    var country = ""
    var state = ""
    var city = ""
    var pinCode = ""
    var orgGst = ""
    var orgPAN = ""
    var orgBank = ""
    var accountNumber = ""
    var accountType = ""
    var ifsc = ""
    var branchName = ""
    var branchPin = ""


    var v1Name=""
    var v1Contact=""
    var v1Email=""
    var v1Adhar=""

    var v2Name=""
    var v2Contact=""
    var v2Email=""
    var v2Adhar=""

    var v3Name=""
    var v3Contact=""
    var v3Email=""
    var v3Adhar=""

    var MSME= ""
    var EINVOICE = ""
    var MULTIPLE_ACCOUNTS=""


    var POCEmailIdET=""
    var selectItem=""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args= arguments
        if(args!=null){
            org_name =  args.getString("ORG_NAME","")
            org_contact= args.getString("ORG_CONTACT","")
            org_email =args.getString("ORG_EMAIL","")
            business_org_name= args.getString("NAME_OF_ORG","")
            legalName = args.getString("LEGAL_ENTITY","")
            ownerName= args.getString("OWNER_NAME","")
            ownerContact = args.getString("OWNER_CONTACT","")
            ownerEmail =args.getString("OWNER_EMAIL","")
            podName= args.getString("POD_NAME","")
            ownerPan = args.getString("OWNER_PAN","")
            podWhatsapp =args.getString("POCWhatsAppNumber","")
            addressName =args.getString("ADDRESS","")
            country= args.getString("COUNTRY","")
            state = args.getString("STATE","")
            city = args.getString("CITY","")
            pinCode =args.getString("PIN","")
            orgGst =args.getString("ORG_GST","")
            orgPAN =args.getString("ORG_PAN","")
            //NEW ADDED
            orgBank= args.getString("BANK_NAME","")
            accountNumber =args.getString("ACCOUNT_NUMBER","")
            accountType = args.getString("ACCOUNT_TYPE","")
            ifsc= args.getString("IFSC","")
            branchName =args.getString("BRANCH_NAME","")
            branchPin =args.getString("PINCODE","")

            MSME = args.getString("MSME","")
            EINVOICE = args.getString("EINVOICE","")
            MULTIPLE_ACCOUNTS = args.getString("OTHERAPPLICABLE","")
            POCEmailIdET = args.getString("POCEmailIdET","")
            selectItem= args.getString("selectItem","")


        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        bindingUploads = FragmentMultipleAccountsBinding.inflate(inflater, container, false)
        bindingUploads?.btnBackForm?.setOnClickListener{
            findNavController(). popBackStack()

        }
        bindingUploads?.btnFinalNext?.setOnClickListener {
           if (validateUI(bindingUploads!!)) {
               val args = Bundle()
               args.putString("FROM_MULTI","MULTIPLE")
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
               args.putString("POCWhatsAppNumber",podWhatsapp)
               args.putString("ADDRESS",addressName)
               args.putString("COUNTRY",country)
               args.putString("STATE",state)
               args.putString("CITY",city)
               args.putString("PIN",pinCode)
               args.putString("ORG_GST",orgGst)
               args.putString("ORG_PAN",orgPAN)
               //NEW ADDED
               args.putString("BANK_NAME",orgBank)
               args.putString("ACCOUNT_NUMBER",accountNumber)
               args.putString("ACCOUNT_TYPE",accountType)
               args.putString("IFSC",ifsc)
               args.putString("BRANCH_NAME",branchName)
               args.putString("PINCODE",branchPin)

               args.putString("MSME",MSME)
               args.putString("EINVOICE",EINVOICE)
               args.putString("OTHERAPPLICABLE",MULTIPLE_ACCOUNTS)
               args.putString("POCEmailIdET",POCEmailIdET)
               args.putString("selectItem",selectItem)




               args.putString("V1_NAME",bindingUploads!!.EtVSecondName.text.toString()?:"")
               args.putString("V1_CONTACT",bindingUploads!!.secondVContact.text.toString())
               args.putString("V1_EMAIL",bindingUploads!!.inputSecondVendorEmail.text.toString())
               args.putString("V1_ADHAR",bindingUploads!!.secondVAdhar.text.toString())

               args.putString("V2_NAME",bindingUploads!!.EtVThirdName.text.toString()?:"")
               args.putString("V2_CONTACT",bindingUploads!!.thirdVContact.text.toString())
               args.putString("V2_EMAIL",bindingUploads!!.inputThirdVendorEmail.text.toString())
               args.putString("V2_ADHAR",bindingUploads!!.thirdVAdhar.text.toString())


               args.putString("V3_NAME",bindingUploads!!.EtVFourthName.text.toString()?:"")
               args.putString("V3_CONTACT",bindingUploads!!.fourthVContact.text.toString())
               args.putString("V3_EMAIL",bindingUploads!!.inputfourthVendorEmail.text.toString())
               args.putString("V3_ADHAR",bindingUploads!!.fourthVAdhar.text.toString())
               Navigation.findNavController(bindingUploads!!.root)
                   .navigate(R.id.action_multipleAccountsFragment_to_uploadsFragment, args)
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




    private fun validateUI(bindingUploads: FragmentMultipleAccountsBinding): Boolean {
        if (bindingUploads.secondAccountll.visibility == View.VISIBLE) {
            if (!isValidName(bindingUploads.EtVSecondName.text.toString())) {
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Owner's Full Name can't be empty",
                    "Error"
                )
                return false
            }

            if(bindingUploads.inputSecondVendorEmail.text.toString().isEmpty()){
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Owner's Email Id can't be empty",
                    "Error"
                )
                return false
            }
            if (bindingUploads.inputSecondVendorEmail.text.toString().isNotEmpty() && !isValidEmail(bindingUploads.inputSecondVendorEmail.text.toString())) {
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Invalid Email Id",
                    "Error"
                )
                return false
            }
            if(bindingUploads.secondVContact.text.toString().isEmpty()){
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Owner's Contact Number can't be empty",
                    "Error"
                )
                return false
            }
            if ( bindingUploads.secondVContact.text.toString().isNotEmpty() && !isValidContact(bindingUploads.secondVContact.text.toString())) {
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Invalid Owner Contact Number",
                    "Error"
                )
                return false
            }
            if(bindingUploads.secondVAdhar.text.toString().isEmpty()){
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Owner's PAN Number can't be empty",
                    "Error"
                )
                return false
            }
            if (bindingUploads.secondVAdhar.text.toString().isNotEmpty() && !isValidPan(bindingUploads.secondVAdhar.text.toString())) {
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
                    "Owner's Full Name can't be empty",
                    "Error"
                )
                return false
            }

            if(bindingUploads.inputThirdVendorEmail.text.toString().isEmpty()){
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Owner's Email Id can't be empty",
                    "Error"
                )
                return false
            }
            if (bindingUploads.inputThirdVendorEmail.text.toString().isNotEmpty() && !isValidEmail(bindingUploads.inputThirdVendorEmail.text.toString())) {
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Invalid Email Id",
                    "Error"
                )
                return false
            }
            if(bindingUploads.thirdVContact.text.toString().isEmpty()){
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Owner's Contact Number can't be empty",
                    "Error"
                )
                return false
            }
            if ( bindingUploads.thirdVContact.text.toString().isNotEmpty() && !isValidContact(bindingUploads.thirdVContact.text.toString())) {
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Invalid Owner Contact Number",
                    "Error"
                )
                return false
            }

            if(bindingUploads.thirdVAdhar.text.toString().isEmpty()){
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Owner's PAN Number can't be empty",
                    "Error"
                )
                return false
            }
            if (bindingUploads.thirdVAdhar.text.toString().isNotEmpty() && !isValidPan(bindingUploads.thirdVAdhar.text.toString())) {
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
                    "Owner's Full Name can't be empty",
                    "Error"
                )
                return false
            }

            if(bindingUploads.inputfourthVendorEmail.text.toString().isEmpty()){
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Owner's Email Id can't be empty",
                    "Error"
                )
                return false
            }
            if (bindingUploads.inputfourthVendorEmail.text.toString().isNotEmpty() && !isValidEmail(bindingUploads.inputfourthVendorEmail.text.toString())) {
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Invalid Email Id",
                    "Error"
                )
                return false
            }
            if(bindingUploads.fourthVContact.text.toString().isEmpty()){
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Owner's Contact Number can't be empty",
                    "Error"
                )
                return false
            }
            if ( bindingUploads.fourthVContact.text.toString().isNotEmpty() && !isValidContact(bindingUploads.fourthVContact.text.toString())) {
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Invalid Owner Contact Number",
                    "Error"
                )
                return false
            }
            if(bindingUploads.fourthVAdhar.text.toString().isEmpty()){
                com.tirupati.vendor.helper.showCustomDialog(
                    requireContext(),
                    "Owner's PAN Number can't be empty",
                    "Error"
                )
                return false
            }
            if (bindingUploads.fourthVAdhar.text.toString().isNotEmpty() && !isValidPan(bindingUploads.fourthVAdhar.text.toString())) {
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