package com.tirupati.vendor.fragmnts

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tirupati.vendor.R
import com.tirupati.vendor.adapters.SpinnerAdapter
import com.tirupati.vendor.databinding.FragmentBankAccountBinding
import com.tirupati.vendor.databinding.FragmentFirstDetailPageBinding
import com.tirupati.vendor.databinding.FragmentThirdDetailPageBinding
import com.tirupati.vendor.helper.hidden
import com.tirupati.vendor.helper.isIFSC
import com.tirupati.vendor.helper.showCustomDialog
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.utils.first
import com.tirupati.vendor.utils.second
import com.tirupati.vendor.utils.third
import com.tirupati.vendor.viewmodels.SignUpUploadsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BankAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class BankAccountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val signUpVM: SignUpUploadsViewModel by viewModels()
    private var bindingThirdPage: FragmentBankAccountBinding? = null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingThirdPage = FragmentBankAccountBinding.inflate(inflater, container, false)

        // Retrieve the arguments
        val orgName = arguments?.getString("ORG_NAME")
        val orgContact = arguments?.getString("ORG_CONTACT")
        val orgEmail = arguments?.getString("ORG_EMAIL")

        // Retrieve the new added values
        val nameOfOrg = arguments?.getString("NAME_OF_ORG")
        val legalEntity = arguments?.getString("LEGAL_ENTITY")
        val pocName = arguments?.getString("POC_NAME")
        val pocContact = arguments?.getString("POC_CONTACT")
        val pocEmail = arguments?.getString("POC_EMAIL")
        val orgGstNumber = arguments?.getString("orgGstNumber")
        val ownerPan = arguments?.getString("OWNER_PAN")
        val address = arguments?.getString("addressET")
        val countrySpinner = arguments?.getString("countrySpinner")
        val statesList = arguments?.getString("statesList")
        val citiesList = arguments?.getString("citiesList")
        val pincode = arguments?.getString("pincode")
        var C1_NAME = arguments?.getString("C1_NAME")
        var C1_EMAIL = arguments?.getString("C1_EMAIL")
        var C1_CONTACT_NO = arguments?.getString("C1_CONTACT_NO")
        var C1_PAN_NO = arguments?.getString("C1_PAN_NO")
        var C2_NAME = arguments?.getString("C2_NAME")
        var C2_EMAIL = arguments?.getString("C2_EMAIL")
        var C2_CONTACT_NO = arguments?.getString("C2_CONTACT_NO")
        var C2_PAN_NO = arguments?.getString("C2_PAN_NO")
        var C3_NAME = arguments?.getString("C3_NAME")
        var C3_EMAIL = arguments?.getString("C3_EMAIL")
        var C3_CONTACT_NO = arguments?.getString("C3_CONTACT_NO")
        var C3_PAN_NO = arguments?.getString("C3_PAN_NO")
        var otherApplicable = arguments?.getString("otherApplicable")

        var latititude = arguments?.getString("latititude")
        var longitude = arguments?.getString("longitude")
        var GODOWN_LOCATION = arguments?.getString("GODOWN_LOCATION")
        bindingThirdPage?.btnThirdDone?.setOnClickListener {
            if(validateUI(bindingThirdPage!!)){



                lifecycleScope.launch(Dispatchers.IO) {
                    val header = HashMap<String, String>()
                    header["Accept"] = "application/json"
                    header["version"] = "1"

                    val response = signUpVM.postCustomerRegistration(header,latititude.toString(),
                        longitude.toString(),GODOWN_LOCATION.toString(),otherApplicable.toString(),
                        C1_NAME.toString(), C1_EMAIL.toString(), C1_CONTACT_NO.toString(), C1_PAN_NO.toString(),
                        C2_NAME.toString(), C2_EMAIL.toString(), C2_CONTACT_NO.toString(), C2_PAN_NO.toString(),
                          C3_NAME.toString(), C3_EMAIL.toString(), C3_CONTACT_NO.toString(), C3_PAN_NO.toString(),
                        orgName.toString(),orgContact.toString(),orgEmail.toString(),nameOfOrg.toString()
                        ,pocName.toString(),pocContact.toString(),pocEmail.toString(),pocContact.toString(),address.toString(),countrySpinner.toString(),statesList.toString(),citiesList.toString(),pincode.toString(),orgGstNumber.toString(),ownerPan.toString(),bindingThirdPage!!.bankNameET.text.toString(),
                        bindingThirdPage?.accountNumberET?.text.toString(),
                        bindingThirdPage?.accountTypes?.text.toString(), bindingThirdPage?.branchET?.text.toString(),
                        bindingThirdPage!!.ifscET.text.toString(),
                       first!!,
                        second!!,
                        third!!



                    )

                    when (response) {

                        is NetworkState.Success -> {
                            withContext(Dispatchers.Main){
                                bindingThirdPage!!.loginProgressBar.progressBar.hidden()

                              //  showCustomDialog()
                            }



//                    binding?.listOpts?.adapter!!.notifyDataSetChanged()



                        }
                        is NetworkState.HttpErrors.ResourceForbidden -> {
                            withContext(Dispatchers.Main) {
                                bindingThirdPage!!.loginProgressBar.progressBar.hidden()
                                Toast.makeText(
                                    context,
                                    "Access forbidden: ${response.msg}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        is NetworkState.Error<*> -> {
                            withContext(Dispatchers.Main) {
                                bindingThirdPage!!.loginProgressBar.progressBar.hidden()

                                Toast.makeText(context, response.msg.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        is NetworkState.NetworkException -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, response.msg.toString(), Toast.LENGTH_SHORT)
                                    .show()

                                bindingThirdPage!!.loginProgressBar.progressBar.hidden()
                            }

                        }

                        is NetworkState.HttpErrors.InternalServerError -> {
                            withContext(Dispatchers.Main){
                                Toast.makeText(context,response.msg.toString(), Toast.LENGTH_SHORT).show()
                                bindingThirdPage!!.loginProgressBar.progressBar.hidden()}

                        }

                        is NetworkState.HttpErrors.ResourceNotFound -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, response.msg.toString(), Toast.LENGTH_SHORT)
                                    .show()
                                bindingThirdPage!!.loginProgressBar.progressBar.hidden()
                            }

                        }

                        else -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT)
                                    .show()
                                bindingThirdPage!!.loginProgressBar.progressBar.hidden()
                            }

                        }
                    }


                }


            }


            }
        getAccountType()
        // Inflate the layout for this fragment
        return bindingThirdPage?.root
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
    private fun validateUI(binding: FragmentBankAccountBinding): Boolean {

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
//        else if(binding.pinET.text.toString().isNullOrEmpty()){
//            showCustomDialog(requireContext(),"PIN code can't be Empty","Error")
//            status = false
//
//        }
//
//        else if(!binding.pinET.text.toString().isValidPINcode()){
//            showCustomDialog(requireContext(),"PIN code not Valid","Error")
//            status = false
//
//        }


        else {
            return true
        }
        return status
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BankAccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BankAccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}