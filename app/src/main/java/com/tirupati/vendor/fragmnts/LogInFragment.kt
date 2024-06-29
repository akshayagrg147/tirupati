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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.tirupati.vendor.R
import com.tirupati.vendor.databinding.FragmentLogInBinding
import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.helper.hidden
import com.tirupati.vendor.helper.showCustomDialog
import com.tirupati.vendor.helper.shown
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.viewmodels.LogInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LogInFragment : Fragment() {
    private val logInVm: LogInViewModel by viewModels()

    private var binding: FragmentLogInBinding? = null
    var disableBtn = true
    @Inject
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //view.findViewById<TextView>(R.id.btnContinueLogin)?.alpha=0.5f

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLogInBinding.inflate(inflater, container, false)

        val paint = binding!!.welcometitle.paint
        val width = paint.measureText(binding!!.welcometitle.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, width, binding!!.welcometitle.textSize, intArrayOf(
            Color.parseColor("#815343"),
            Color.parseColor("#E99972"),
            /*Color.parseColor("#64B678"),
            Color.parseColor("#478AEA"),*/
            //Color.parseColor("#8446CC")
        ), null, Shader.TileMode.REPEAT)

        binding!!.welcometitle.paint.setShader(textShader)
        binding?.inputUserGST?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
               /* if(s.length<0){
                    disableBtn = false
                binding?.btnContinueLogin?.alpha=0.5f
                }
                else{
                    disableBtn = true
                    binding?.btnContinueLogin?.alpha=1f
                }
            }*/}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {

                // TODO Auto-generated method stub
            }
        });
      /* if( disableBtn){
           binding?.btnContinueLogin?.alpha=0.5f
       }else{
           binding?.btnContinueLogin?.alpha=0.5f
       }*/
            binding?.btnContinueLogin?.setOnClickListener{

            }
        binding!!.btnContinueLogin.setOnClickListener {
            if (validateUI(binding!!)) {
                logInRequest(binding?.inputUserGST?.text.toString())
            }
        }
        binding!!.signupReg.setOnClickListener {
            val args = Bundle()

            Navigation.findNavController(binding!!.root).
            navigate(R.id.action_logInFragment2_to_registrationFragment, args)
        }
        return binding?.root!!
    }
    private fun validateUI(binding: FragmentLogInBinding): Boolean {

        var status = false
        if (binding.inputUserGST.text.isNullOrEmpty()) {
            showCustomDialog(requireContext(),"Gst Number can't be empty!", "Error")
            status = false
        }else {
            return true
        }
        return status
    }


    fun logInRequest(gstNumber: String) {
        binding!!.loginProgressBar.progressBar.shown()
        lifecycleScope.launch {
            var response = logInVm.getLogIn(gstNumber)


            when (response) {
                is NetworkState.Success->{
                    binding!!.loginProgressBar.progressBar.hidden()

                    var otp=response.body.OTP
                    var phn_nbr = response.body.MOBILE_NO
                    var type = response.body.USER_TYPE
                    val args = Bundle()
                    args.putString("GST",binding?.inputUserGST?.text.toString())
                    args.putString("OTP_IS",otp)
                    args.putString("PH_NO",phn_nbr)
                    args.putString("USERTYPE",type)
                    Navigation.findNavController(binding!!.root).
                    navigate(R.id.action_logInFragment2_to_OTPFragment2, args)
                }

                is NetworkState.Error<*>->{
                    binding!!.loginProgressBar.progressBar.hidden()

                    Toast.makeText(context,response.msg.toString(),Toast.LENGTH_SHORT).show()
                }

                is NetworkState.NetworkException->{
                    binding!!.loginProgressBar.progressBar.hidden()

                        showCustomDialog(requireContext(),response.msg.toString(), "Error")


                }
                is NetworkState.HttpErrors.InternalServerError->{
                    binding!!.loginProgressBar.progressBar.hidden()
                    showCustomDialog(requireContext(),response.msg.toString(), "Error")

                }
                is NetworkState.HttpErrors.ResourceNotFound->{
                    binding!!.loginProgressBar.progressBar.hidden()
                    showCustomDialog(requireContext(),response.msg.toString(), "Error")

                }
                else->{
                    showCustomDialog(requireContext(),"something went wrong", "Error")
                    binding!!.loginProgressBar.progressBar.hidden()

                }
            }


        }


    }


}