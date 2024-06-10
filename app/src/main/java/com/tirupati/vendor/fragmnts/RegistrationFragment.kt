package com.tirupati.vendor.fragmnts

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.tirupati.vendor.R
import com.tirupati.vendor.databinding.FragmentRegistrationBinding
import com.tirupati.vendor.helper.isValidPhoneNumber
import com.tirupati.vendor.helper.showCustomDialog
import com.tirupati.vendor.utils.isValidEmail


class RegistrationFragment : Fragment() {
    private var bindingReg: FragmentRegistrationBinding? = null
            var disableBtn = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindingReg = FragmentRegistrationBinding.inflate(inflater, container, false)

        val paint = bindingReg!!.welcometitle.paint
        val width = paint.measureText(bindingReg!!.welcometitle.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, width, bindingReg!!.welcometitle.textSize, intArrayOf(
            Color.parseColor("#815343"),
            Color.parseColor("#E99972"),
            /*Color.parseColor("#64B678"),
            Color.parseColor("#478AEA"),*/
            //Color.parseColor("#8446CC")
        ), null, Shader.TileMode.REPEAT)

        bindingReg!!.welcometitle.paint.setShader(textShader)
        bindingReg!!.signupReg.setOnClickListener {
            requireActivity().onBackPressed()
        }
//        bindingReg!!.btnRegister.alpha = .5f
        bindingReg!!.btnRegister.setOnClickListener {
            if (validateUI(bindingReg!!)) {
                callAPI()
            }


        }

        bindingReg?.etContactNumber?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                if(s.length<0){
//                    disableBtn = false
//                    bindingReg?.btnRegister?.alpha=0.5f
//                }
//                else{
//                    disableBtn = true
//                    bindingReg?.btnRegister?.alpha=1f
//                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {

                // TODO Auto-generated method stub
            }
        });



        return bindingReg!!.root
    }






    private fun callAPI() {

        val args = Bundle()
//        NEW
        args.putString("ORG_NAME",bindingReg!!.etNameOrg.text.toString())
        args.putString("ORG_CONTACT",bindingReg!!.etContactNumber.text.toString())
        args.putString("ORG_EMAIL",bindingReg!!.etOrgEmailNumber.text.toString())

        Navigation.findNavController(bindingReg!!.root).
        navigate(R.id.action_registrationFragment_to_firstDetailPageFragment, args)
    }

    private fun validateUI(binding: FragmentRegistrationBinding): Boolean {

        var status = false
        if (binding.etNameOrg.text.isNullOrEmpty()) {
            showCustomDialog(requireContext(),"Name Of Organisation can't be empty!","Error")
            status = false
        }
        else if(binding.etContactNumber.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"Organisation’s Contact number can't be empty!","Error")
            status = false

        }

        else if(!binding.etContactNumber.text.toString().isValidPhoneNumber()){
            showCustomDialog(requireContext(),"Organisation’s Contact number is incorrect","Error")
            status = false

        }
        else if(binding.etOrgEmailNumber.text.isNullOrEmpty()){
            showCustomDialog(requireContext(),"Organisation’s Email id can't be empty!","Error")
            status = false

        }
        else if(!binding.etOrgEmailNumber.text.toString().isValidEmail()){
            showCustomDialog(requireContext(),"Organisation’s Email id is incorrect","Error")
            status = false

        }

        else {
            return true
        }
        return status
    }

}