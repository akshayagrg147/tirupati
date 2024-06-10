package com.tirupati.vendor.fragmnts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

import com.tirupati.vendor.R
import com.tirupati.vendor.databinding.FragmentMultipleAccountsBinding


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
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        bindingUploads = FragmentMultipleAccountsBinding.inflate(inflater, container, false)

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


               args.putString("V1_NAME",bindingUploads!!.EtVFirstName.text.toString()?:"")
               args.putString("V1_CONTACT",bindingUploads!!.firstVContact.text.toString())
               args.putString("V1_EMAIL",bindingUploads!!.inputFirstVendorEmail.text.toString())
               args.putString("V1_ADHAR",bindingUploads!!.firstVAdhar.text.toString())

               args.putString("V2_NAME",bindingUploads!!.EtVSecondName.text.toString()?:"")
               args.putString("V2_CONTACT",bindingUploads!!.secondVContact.text.toString())
               args.putString("V2_EMAIL",bindingUploads!!.inputSecondVendorEmail.text.toString())
               args.putString("V2_ADHAR",bindingUploads!!.secondVAdhar.text.toString())


               args.putString("V3_NAME",bindingUploads!!.EtVThirdName.text.toString()?:"")
               args.putString("V3_CONTACT",bindingUploads!!.thirdVContact.text.toString())
               args.putString("V3_EMAIL",bindingUploads!!.inputThirdVendorEmail.text.toString())
               args.putString("V3_ADHAR",bindingUploads!!.thirdVAdhar.text.toString())
               Navigation.findNavController(bindingUploads!!.root)
                   .navigate(R.id.action_multipleAccountsFragment_to_uploadsFragment, args)
           }
        }
        bindingUploads?.firstAdded?.setOnClickListener{
            bindingUploads?.firstLL?.visibility = View.GONE
            bindingUploads!!.secondAccountll.visibility = View.VISIBLE

        }
        bindingUploads?.secondAdded?.setOnClickListener{
            bindingUploads?.firstLL?.visibility = View.GONE
            bindingUploads!!.secondAccountll.visibility = View.GONE
            bindingUploads!!.thirdAccountll.visibility = View.VISIBLE

        }


        return bindingUploads!!.root
    }

    private fun validateUI(bindingUploads: FragmentMultipleAccountsBinding): Boolean {

        return true
    }

}