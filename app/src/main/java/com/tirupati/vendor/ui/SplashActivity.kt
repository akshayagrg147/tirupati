package com.tirupati.vendor.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import com.tirupati.vendor.databinding.ActivitySplashBinding
import com.tirupati.vendor.helper.SessionManager
import com.tirupati.vendor.utils.moveToActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


const val MY_REQUEST_CODE: Int = 101
const val FORCE_UPDATE: Int = 102
const val APP_UPDATE_CODE: Int = 500

@AndroidEntryPoint
class SplashActivity : /*BaseActivity<ActivitySplashBinding>*/AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    private var isLogin: Boolean = false

    private var isUpdate: Boolean = false
    @Inject
    lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view: View = binding!!.root


       /* Glide.with(this@SplashActivity)
            .load("https://goodies.icons8.com/web/common/header/flags/us.svg")
            // you may not need this

            .into(binding.nameLogo);*/



        setContentView(view)
        if (isUpdate) {
            Log.i("UPDATEE", "We have an update.")

        } else {
            Handler(Looper.getMainLooper()).postDelayed({
               // if (sessionManager.virtualAddress==null){

                if (sessionManager.user!=null){

                    if(sessionManager.user!!.USER_TYPE=="Superviser"){
                    startActivity(Intent(this, LandingScreenCustomerActivity::class.java))

                    finish()}
                    else  if(sessionManager.user!!.USER_TYPE=="Vendor"){

//
                        startActivity(Intent(this, LandingVendorSActivity::class.java))

                        finish()}


                    else{
                        startActivity(Intent(this, LandingScreenGateKeeperActivity::class.java))

                    }

                }else{
                    startActivity(Intent(this, SIgnInRegisterActivity::class.java))

                    finish()
                }

//                }
//                else{
//                    startActivity(Intent(this, SIgnInRegisterActivity::class.java))
//                    finish()
//                }
            }, 2500L) // 850

        }

    }

//    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.e("MY_APP", "Update flow failed! Result code: $resultCode")
                // If the update is cancelled or fails,
                // you can request to start the update again.
            } else {

            }
        }

    }






    //TO GENERATE YOUR SHA,MD Keys(Set up by Arnab)
    fun getSig(context: Context, key: String) {
        /*try {
            val info = context.packageManager.getPackageInfo(

                BuildConfig.APPLICATION_ID,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance(key)
                md.update(signature.toByteArray())
                val digest = md.digest()
                val toRet = StringBuilder()
                for (i in digest.indices) {
                    if (i != 0) toRet.append(":")
                    val b = digest[i].toInt() and 0xff
                    val hex = Integer.toHexString(b)
                    if (hex.length == 1) toRet.append("0")
                    toRet.append(hex)
                }
                val s = toRet.toString()
                Log.e("sig", s)

            }
        } catch (e1: PackageManager.NameNotFoundException) {
            Log.e("name not found", e1.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("no such an algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }*/
    }

}