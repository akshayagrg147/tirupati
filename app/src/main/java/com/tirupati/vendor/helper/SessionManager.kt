package com.tirupati.vendor.helper

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.tirupati.vendor.model.OTPverifiedModel


class SessionManager (private val mCtx: Context) {

    fun logout() {

        val sharedPreferences =
            mCtx.getSharedPreferences(
                SHARED_PREF_NAME,
                Context.MODE_PRIVATE
            )
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    /*=====================Token =================================*/




    var loginToken:String?
        get() {
            val sharedPreferences =
                mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(USER_TOKEN, "")
        }


        set(value) {
            val sharedPreferences =
                mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(USER_TOKEN, value)
            editor.apply()
        }

    var user: OTPverifiedModel?
        get() {
            val sharedPreferences =
                mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            val modelValue = sharedPreferences.getString(USER_INFO, "")
            val gson = Gson()

            return gson.fromJson(modelValue, OTPverifiedModel::class.java)
        }

        set(model) {

            if (model?.RESPONSEDATA?.USER_ID.isNullOrBlank())
                return

            val sharedPreferences =
                mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val gson = Gson()
            val json = gson.toJson(model)

            Log.d("TOKKEENNN",loginToken.toString())
            if (!model?.RESPONSEDATA?.USER_ID.isNullOrBlank()) {
                loginToken=model?.RESPONSEDATA?.LOG_TOKEN
                Log.d("TOKKEENNN",loginToken.toString())
            }

            editor.putString(USER_INFO, json)
            editor.apply()
        }

    //========================Static Variables==================//
    companion object {
        private const val TOKEN_ACTIVE = "com.tirupati.vendor"
        private const val TOKEN_ISACTIVE = "active_Token"
        const val SHARED_PREF_NAME = "com.tirupati.vendor"
        const val AUTH_TOKEN = "Auth_token"
        private var mInstance: SessionManager? = null
        private const val USER_INFO = "user_info"

        private const val USER_TOKEN = "user_token"


        @Synchronized
        fun getInstance(context: Context): SessionManager? {
            if (mInstance == null) {
                mInstance = SessionManager(context)
            }
            return mInstance
        }
    }


}