package com.tirupati.vendor.fragmnts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.tirupati.vendor.R
import com.tirupati.vendor.adapters.SpinnerAdapter
import com.tirupati.vendor.databinding.FragmentThirdDetailPageBinding
import com.tirupati.vendor.helper.isIFSC
import com.tirupati.vendor.helper.isValidPINcode
import com.tirupati.vendor.helper.showCustomDialog


class BankDetailFragment : Fragment() {
    private var bindingThirdPage: FragmentThirdDetailPageBinding? = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if(args!=null){
            org_name = args.getString("ORG_NAME","")
            org_contact= args.getString("ORG_CONTACT","")
            org_email= args.getString("ORG_EMAIL","")
            business_org_name = args.getString("NAME_OF_ORG","")
            legalName = args.getString("LEGAL_ENTITY","")
            ownerName = args.getString("OWNER_NAME","")
            ownerContact = args.getString("OWNER_CONTACT","")
            ownerEmail = args.getString("OWNER_EMAIL","")
            podName = args.getString("POD_NAME","")
            ownerPan = args.getString("OWNER_PAN","")
            podWhatsapp = args.getString("POCWhatsAppNumber","")

            addressName = args.getString("ADDRESS","")
            country= args.getString("COUNTRY","")
            state = args.getString("STATE","")
            city = args.getString("CITY","")
            pinCode = args.getString("PIN","")
            orgGst = args.getString("ORG_GST","")
            orgPAN =args.getString("ORG_PAN","")





        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindingThirdPage = FragmentThirdDetailPageBinding.inflate(inflater, container, false)
        bindingThirdPage?.btnBackForm?.setOnClickListener{
            findNavController(). popBackStack()

        }
        bindingThirdPage?.btnThirdDone?.setOnClickListener {
            if(validateUI(bindingThirdPage!!))
            {
                if (bindingThirdPage!!.multipleAccountswOnOff.isChecked) {
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
                    args.putString("POCWhatsAppNumber",podWhatsapp)
                    args.putString("ADDRESS",addressName)
                    args.putString("COUNTRY",country)
                    args.putString("STATE",state)
                    args.putString("CITY",city)
                    args.putString("PIN",pinCode)
                    args.putString("ORG_GST",orgGst)
                    args.putString("ORG_PAN",orgPAN)
                    //NEW ADDED
                    args.putString("BANK_NAME",bindingThirdPage!!.bankNameET.text.toString())
                    args.putString("ACCOUNT_NUMBER",bindingThirdPage!!.accountNumberET.text.toString())
                    args.putString("ACCOUNT_TYPE",bindingThirdPage!!.accountTypes.text.toString())
                    args.putString("IFSC",bindingThirdPage!!.ifscET.text.toString())
                    args.putString("BRANCH_NAME",bindingThirdPage!!.branchET.text.toString())
                    args.putString("PINCODE",bindingThirdPage!!.pinET.text.toString())
                   if(bindingThirdPage!!.MsmeswOnOff.isChecked){
                       args.putString("MSME","1")
                   }else{args.putString("MSME","0")}

                    if(bindingThirdPage!!.InvoiceswOnOff.isChecked){
                        args.putString("EINVOICE","1")
                    }else{args.putString("EINVOICE","0")
                    }

                    if(bindingThirdPage!!.multipleAccountswOnOff.isChecked){
                        args.putString("OTHERAPPLICABLE","1")
                    }else{args.putString("OTHERAPPLICABLE","0")
                    }



                    Navigation.findNavController(bindingThirdPage!!.root).navigate(
                        R.id.action_thirdDetailPageFragment_to_multipleAccountsFragment2,
                        args
                    )
                } else {
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
                    args.putString("POCWhatsAppNumber",podWhatsapp)
                    args.putString("ADDRESS",addressName)
                    args.putString("COUNTRY",country)
                    args.putString("STATE",state)
                    args.putString("CITY",city)
                    args.putString("PIN",pinCode)
                    args.putString("ORG_GST",orgGst)
                    args.putString("ORG_PAN",orgPAN)

                    //NEW ADDED
                    args.putString("BANK_NAME",bindingThirdPage!!.bankNameET.text.toString())
                    args.putString("ACCOUNT_NUMBER",bindingThirdPage!!.accountNumberET.text.toString())
                    args.putString("ACCOUNT_TYPE",bindingThirdPage!!.accountTypes.text.toString())
                    args.putString("IFSC",bindingThirdPage!!.ifscET.text.toString())
                    args.putString("BRANCH_NAME",bindingThirdPage!!.branchET.text.toString())
                    args.putString("PINCODE",bindingThirdPage!!.branchET.text.toString())
                    if(bindingThirdPage!!.MsmeswOnOff.isChecked){
                        args.putString("MSME","1")
                    }else{args.putString("MSME","0")}

                    if(bindingThirdPage!!.InvoiceswOnOff.isChecked){
                        args.putString("EINVOICE","1")
                    }else{args.putString("EINVOICE","0")
                    }

                    if(bindingThirdPage!!.multipleAccountswOnOff.isChecked){
                        args.putString("OTHERAPPLICABLE","1")
                    }else{args.putString("OTHERAPPLICABLE","0")
                    }

                    Navigation.findNavController(bindingThirdPage!!.root)
                        .navigate(R.id.action_thirdDetailPageFragment_to_uploadsFragment, args)

                }
            }



        }
        getAccountType()



        return bindingThirdPage!!.root

    }
//    bankNameET
//    accountNumberET
//    accountTypes
//    ifscET
//    pinET

/*
*
*
* */





    private fun validateUI(binding: FragmentThirdDetailPageBinding): Boolean {

        var status = false
        if (binding.bankNameET.text.isNullOrEmpty()) {
            showCustomDialog(requireContext(),"Organisation’s Bank name can't be empty!","Error")
            status = false
        }
        else if(binding.accountNumberET.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"Account number can't be empty!","Error")
            status = false

        }

        else if(binding.accountTypes.text.toString().isNullOrEmpty()){
            showCustomDialog(requireContext(),"Select Account Type!!","Error")
            status = false

        }
        else if(binding.ifscET.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"IFSC code can't be empty!","Error")
            status = false

        }
        else if(binding.branchET.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"Branch name can't be empty","Error")
            status = false

        }
        else if(!binding.ifscET.text.toString().isIFSC()){
            showCustomDialog(requireContext(),"IFSC code is incorrect!","Error")
            status = false

        }
        else if(binding.pinET.text.toString().isNullOrEmpty()){
            showCustomDialog(requireContext(),"PIN code can't be Empty","Error")
            status = false

        }

        else if(!binding.pinET.text.toString().isValidPINcode()){
            showCustomDialog(requireContext(),"PIN code not Valid","Error")
            status = false

        }


        else {
            return true
        }
        return status
    }


    fun getAccountType() {
        val accountType: ArrayList<String> = ArrayList()
//        var accountType = ["Cash Credit","Over Draft","Current Account"]
        accountType.add("Cash Credit")
        accountType.add("Over Draft")
        accountType.add("Current Account")
        val spinnerAdapter = SpinnerAdapter(requireActivity(), R.layout.item_spinner_row, accountType)
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_row)
        bindingThirdPage?.accountTypes?.setAdapter(spinnerAdapter);
        // Remove setting key listener to null
        // bindingSecondPage?.statesList?.setKeyListener(null);
        bindingThirdPage?.accountTypes?.threshold=1
        bindingThirdPage?.accountTypes?.setOnClickListener {
            (it as AutoCompleteTextView).showDropDown()
        }

        bindingThirdPage?.accountTypes?.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedModel = parent.adapter.getItem(position) as String
            // Do whatever you want with the selected model object here
            bindingThirdPage?.accountTypes?.setText(selectedModel)
        }
    }


}