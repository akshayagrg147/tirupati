package com.tirupati.vendor.fragmnts

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tirupati.vendor.databinding.FragmentOTPBinding
import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.network.NetworkState
import com.tirupati.vendor.ui.LandingScreenCustomerActivity
import com.tirupati.vendor.ui.LandingScreenGateKeeperActivity
import com.tirupati.vendor.ui.LandingVendorActivity
import com.tirupati.vendor.ui.LandingVendorSActivity
import com.tirupati.vendor.utils.moveToActivity
import com.tirupati.vendor.utils.toast
import com.tirupati.vendor.viewmodels.LogInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class OTPFragment : Fragment() {
    private var bindingOTP: FragmentOTPBinding? = null
    private val logInVm: LogInViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager
    lateinit var bundledData: Bundle
    var GST = ""
    var OTPReceived = ""
    var Mobile = ""
    var UserType = ""

    var callApi = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        if (args != null) {

            bundledData = args
            Log.d("bndldata", bundledData.toString())

            GST = args.getString("GST", "")
            OTPReceived = args.getString("OTP_IS", "")
            Mobile = args.getString("PH_NO", "")
            UserType = args.getString("USERTYPE","")

        }
//        initComponent()

    }

    /*private fun initComponent() {
        bindingOTP?.cardView?.post {
            bindingOTP?.customOTPView1?.setUpViews()

            //     binding.customOTPView1.setAutofillHints(View.AUTOFILL_HINT_PASSWORD)

        }
        Handler(Looper.getMainLooper()).postDelayed({
            bindingOTP!!.customOTPView1.binding.edt2.requestFocus()
            Handler(Looper.getMainLooper()).postDelayed({
                bindingOTP!!.customOTPView1.binding.edt1.requestFocus()
            }, 10)
        }, 10)
    }*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bindingOTP = FragmentOTPBinding.inflate(inflater, container, false)
        initViews()
        if (bindingOTP?.otpValueVOFF?.text.toString().length == 3) {

        }

        bindingOTP!!.textPhoneOrEmail.setText(formatPhoneNumber(Mobile))



//                closeDrawer()
        bindingOTP!!.resend.setOnClickListener {
            if (callApi) {
                bindingOTP?.resend?.visibility=View.GONE

                lifecycleScope.launch {
                    var response = logInVm.getLogIn(GST)

                    when (response) {

                        is NetworkState.Success -> {
                            OTPReceived = response.body.OTP
                            initViews()
                        }

                        is NetworkState.Error<*> -> {
                            // Toast.makeText(context,response.msg.toString(),Toast.LENGTH_SHORT).show()
                        }

                        is NetworkState.NetworkException -> {
                        }

                        is NetworkState.HttpErrors.InternalServerError -> {
                        }

                        is NetworkState.HttpErrors.ResourceNotFound -> {
                        }

                        else -> {
                        }
                    }


                }

            } else {

            }

        }


        bindingOTP!!.btnVerify.setOnClickListener {

            if (bindingOTP?.otpValueVOFF?.text.toString().equals(OTPReceived)) {
                callVerifiedOTP()
            } else {
                toast("OTP is not matched!")
            }
        }

        return bindingOTP!!.root
    }
    fun formatPhoneNumber(phoneNumber: String): String {
        // Check if the phone number has at least 10 digits
        if (phoneNumber.length >= 10) {
            val firstTwoDigits = phoneNumber.substring(0, 2)
            val lastTwoDigits = phoneNumber.substring(phoneNumber.length - 2)
            return "$firstTwoDigits******$lastTwoDigits"
        }
        // If the phone number is not at least 10 digits long, return it as it is
        return phoneNumber
    }

    fun callVerifiedOTP() {
        lifecycleScope.launch {
            var response = logInVm.getOtpVerified(bindingOTP?.otpValueVOFF?.text.toString(), Mobile, UserType)

            when (response) {

                is NetworkState.Success -> {
                    sessionManager.user= response.body
                    var type= sessionManager.user!!.USER_TYPE
                    //Gatekeeper
                    //Vendor
                    if(type=="Superviser"){

//                        requireActivity().moveToActivity(LandingScreenCustomerActivity::class.java)
//                        requireActivity().finish() //customerFragment //gateentrychecklist
                        requireActivity().moveToActivity(LandingScreenCustomerActivity::class.java)
                        requireActivity().finish()
                    }
                    else  if(type=="Vendor"){

//                        requireActivity().moveToActivity(LandingScreenCustomerActivity::class.java)
//                        requireActivity().finish()//polist //24AAACC4175D1Z5
                        requireActivity().moveToActivity(LandingVendorSActivity::class.java)
                        requireActivity().finish()
                    }
                    else{
                        //gateEntryFragment //vendorList
                        requireActivity().moveToActivity(LandingScreenGateKeeperActivity::class.java)
                        requireActivity().finish()
                    }
                }

                is NetworkState.Error<*> -> {
                    // Toast.makeText(context,response.msg.toString(),Toast.LENGTH_SHORT).show()
                }

                is NetworkState.NetworkException -> {
                }

                is NetworkState.HttpErrors.InternalServerError -> {
                }

                is NetworkState.HttpErrors.ResourceNotFound -> {
                }

                else -> {
                }
            }


        }

    }

    private fun initViews() {


        var mCountDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val text = " ${
                    String.format(
                        "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(
                                        millisUntilFinished
                                    )
                                )
                    )
                }"
                bindingOTP?.countTimer?.text = text
                callApi = false
            }

            override fun onFinish() {

                bindingOTP?.resend?.visibility=View.VISIBLE


                bindingOTP?.countTimer?.text = ""
                callApi = true
            }
        }.start()
    }
}