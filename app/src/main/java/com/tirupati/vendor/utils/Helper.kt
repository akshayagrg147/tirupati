package com.tirupati.vendor.utils

import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.startActivity
import java.lang.Exception
import java.util.*

class Helper {
    companion object {
        fun isValidEmail(email: String): Boolean {
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun hideKeyboard(view: View){
            try {
                val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }catch (e: Exception){

            }
        }

        fun shareText(activity: Activity, content: String){
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, content)
            intent.type = "text/plain"
            activity.startActivity(Intent.createChooser(intent, "Share Via"))
        }


    }





//    fun getCountryDialCode(activity: Activity, code: String): String? {
//        var contryId: String? = null
//        var contryDialCode: String? = null
//        val telephonyMngr = activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//        contryId = telephonyMngr.simCountryIso.uppercase(Locale.getDefault())
//        val arrContryCode: Array<String> =
//            activity.getResources().getStringArray(R.array  .)
//        for (i in arrContryCode.indices) {
//            val arrDial = arrContryCode[i].split(",".toRegex()).toTypedArray()
//            if (arrDial[1].trim { it <= ' ' } == CountryID.trim()) {
//                contryDialCode = arrDial[0]
//                break
//            }
//        }
//        return contryDialCode
//    }

}