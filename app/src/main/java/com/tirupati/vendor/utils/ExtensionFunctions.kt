package com.tirupati.vendor.utils

import android.app.Activity
import android.content.Context
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.URLEncoder
import java.util.regex.Pattern


fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}


fun debug(s: Any) {
    Log.d("devdebug", "" + s)
}

// Has line break
fun getBase64LineBreak(input: String): String? {
    return Base64.encodeToString(input.toByteArray(), Base64.DEFAULT)
}

// No line break
fun getBase64NoLineBreak(input: String): String? {
    return Base64.encodeToString(input.toByteArray(), Base64.NO_WRAP)
}

//fun convertStreamToString(response: java.net.http.HttpResponse): String? {
//    var responseBody: String? = null
//    val entity: HttpEntity = response.getEntity()
//    if (entity != null) {
//        try {
//            responseBody = EntityUtils.toString(entity)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//    return responseBody
//}

fun File.toMultipartBody(
    fieldName: String,
    parseType: String = "multipart/form-data",
    fileName: String = URLEncoder.encode(this.name, "utf-8")
): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        fieldName, fileName, (RequestBody.create(parseType.toMediaTypeOrNull(), this))
    )
}


fun String.toRequestBody(): RequestBody {
    return RequestBody.create(
        "text/plain".toMediaTypeOrNull(),
//        MediaType.parse("multipart/form-data"),
        this
    ) as RequestBody

}


fun Fragment.toast(msg: String) {
    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
}

fun View.showToUi() {
    let {
        it.visibility = View.VISIBLE
    }
}

fun View.hideFromUi() {
    let {
        it.visibility = View.GONE
    }
}

/*fun CharSequence?.isValidEmail(): Boolean {
    return TextUtils.isEmpty(this) || !Patterns.EMAIL_ADDRESS.matcher(this!!).matches()
}*/
fun String.isValidEmail() : Boolean {

    return Patterns.EMAIL_ADDRESS.matcher(this).matches();
}
fun String.isValidPANNumber() : Boolean {
    val patterns =  "[A-Z]{5}[0-9]{4}[A-Z]{1}"
    return Pattern.compile(patterns).matcher(this).matches()
}


fun Activity?.hideKeyboard() {
    this?.window?.setSoftInputMode(
        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
    )
}
