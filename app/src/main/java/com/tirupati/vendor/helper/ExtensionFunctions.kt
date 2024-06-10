package com.tirupati.vendor.helper

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.text.format.DateFormat
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.textfield.TextInputEditText
import com.tirupati.vendor.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.apache.commons.io.FileUtils
import java.io.File
import java.net.URL
import java.net.URLEncoder
import java.text.ParseException
import java.util.Calendar
import java.util.Date
import java.util.Timer
import java.util.TimerTask
import java.util.regex.Pattern
import kotlin.math.abs

fun View?.add() {
    this?.visibility = View.VISIBLE
}


fun View?.addIf(condition: Boolean) {
    this?.visibility = if (condition) View.VISIBLE
    else View.GONE
}
fun Fragment?.showToastLikeDialog(activity: Activity, msg: String) {

    val view = activity.layoutInflater.inflate(R.layout.custom_dialog_toastlike, null)
    //val close: ImageView = view?.findViewById(R.id.iv_close_pop_up) as ImageView
    val errormsg: TextView = view?.findViewById(R.id.text) as TextView
    val dialogBuilder = AlertDialog.Builder(this?.activity)
    dialogBuilder.setView(view)
//    val width = (this!!.resources.displayMetrics.widthPixels * 0.90).toInt()
//    val height = (this.resources.displayMetrics.heightPixels * 0.90).toInt()


    val dialog: AlertDialog = dialogBuilder.create()
    dialog.setCancelable(true)
    errormsg.text=msg
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    // dialog.window?.setLayout(width, height)
//    close.setOnClickListener {
//        dialog.dismiss()
//    }


    dialog.show()
    val t = Timer()
    t.schedule(object : TimerTask() {
        override fun run() {
            dialog.dismiss() // when the task active then close the dialog
            t.cancel() // also just top the timer thread, otherwise, you may receive a crash report
        }
    }, 1000)
}


fun View?.shown() {
    this?.visibility = View.VISIBLE
}

fun View?.hidden() {
    this?.visibility = View.GONE
}
fun showToastLikeAlert(context: Context?,msg:String,title:String){
    val dialog: android.app.AlertDialog? = android.app.AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(msg).setPositiveButton("Okay"){dialog, which ->
            dialog.dismiss()
        }
        .create()


    dialog?.show()
    dialog?.setOnShowListener {
        val positiveButton: Button? = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
        positiveButton?.setTextColor(Color.BLACK) // Change text color here
    }
}


fun showCustomDialog(context: Context,message: String, title: String ) {
    val dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout, null)
    val dtitle:TextView = dialogView.findViewById<TextView>(R.id.dialog_title)
    val dmsg:TextView = dialogView.findViewById<TextView>(R.id.dialog_message)
    val dBtn:TextView = dialogView.findViewById<TextView>(R.id.ok_button)
    // Set title and message
    dtitle.text = title
    dmsg.text = message

    // Create dialog builder
    val builder = AlertDialog.Builder(context)
        .setView(dialogView)

    // Create and set up the dialog
    val dialog = builder.create()

    // Set click listener for OK button
    dBtn.setOnClickListener {

        dialog.dismiss() // Dismiss the dialog
    }

    // Show the dialog
    dialog.show()
}

fun View?.remove() {
    this?.visibility = View.GONE
}
//TO MULTIPART
fun ImageView?.loadFromUrlWithRoundedCorners(imageUrl:String?, placeHolder:Int= androidx.cardview.R.color.cardview_shadow_end_color){
    try {
        if (this!=null&&imageUrl!=null)
            Glide.with(this.context)
                .load(imageUrl)
                .transform(CenterCrop(), RoundedCorners(12))
                .placeholder(placeHolder)
                .error(placeHolder)
                .into(this)

    }catch (e:Exception){}
}
fun getFileFromUri(context: Context, uri: Uri?): File? {
    uri ?: return null
    uri.path ?: return null

    var newUriString = uri.toString()
    newUriString = newUriString.replace(
        "content://com.android.providers.downloads.documents/",
        "content://com.android.providers.media.documents/"
    )
    newUriString = newUriString.replace(
        "/msf%3A", "/image%3A"
    )
    val newUri = Uri.parse(newUriString)

    var realPath = String()
    val databaseUri: Uri
    val selection: String?
    val selectionArgs: Array<String>?
    if (newUri.path?.contains("/document/image:") == true) {
        databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        selection = "_id=?"
        selectionArgs = arrayOf(DocumentsContract.getDocumentId(newUri).split(":")[1])
    } else {
        databaseUri = newUri
        selection = null
        selectionArgs = null
    }
    try {
        val column = "_data"
        val projection = arrayOf(column)
        val cursor = context.contentResolver.query(
            databaseUri,
            projection,
            selection,
            selectionArgs,
            null
        )
        cursor?.let {
            if (it.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(column)
                realPath = cursor.getString(columnIndex)
            }
            cursor.close()
        }
    } catch (e: Exception) {
        Log.i("GetFileUri Exception:", e.message ?: "")
    }
    val path = realPath.ifEmpty {
        when {
            newUri.path?.contains("/document/raw:") == true -> newUri.path?.replace(
                "/document/raw:",
                ""
            )
            newUri.path?.contains("/document/primary:") == true -> newUri.path?.replace(
                "/document/primary:",
                "/storage/emulated/0/"
            )
            else -> return null
        }
    }
    return if (path.isNullOrEmpty()) null else File(path)
}

fun createFileFromUri(context: Context, name: String, uri: Uri): File? {
    return try {
        val stream = context.contentResolver.openInputStream(uri)
        val file =
            File.createTempFile(
                "${name}_${System.currentTimeMillis()}",
                ".png",
                context.cacheDir
            )
        FileUtils.copyInputStreamToFile(
            stream,
            file
        )  // Use this one import org.apache.commons.io.FileUtils
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.N)
fun String?.stringDateToLong(date: String) {
    val f = SimpleDateFormat("dd-MMM-yyyy-MM-dd HH:mm:ss")
    try {
        val d: Date = f.parse(date)
        val milliseconds = d.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
}

/*@RequiresApi(Build.VERSION_CODES.N)
fun String.dateToLong(): Long{
    val f = SimpleDateFormat("dd-MMM-yyyy-MM-dd HH:mm:ss")
    try {
        val d: Date = f.parse(this)
        val milliseconds = d.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
}*/

fun String.urlToBitmap(): Bitmap {
    val url = URL(this)
    return BitmapFactory.decodeStream(url.openStream())
}


fun Long.checkDate(): String {

    val nowTime = Calendar.getInstance()
    val neededTime = Calendar.getInstance()
    neededTime.timeInMillis = this
    return if (neededTime[Calendar.YEAR] == nowTime[Calendar.YEAR]) {
        if (neededTime[Calendar.MONTH] == nowTime[Calendar.MONTH]) {
            if (neededTime[Calendar.DATE] - nowTime[Calendar.DATE] == 1) {
                //here return like "Tomorrow at 12:00"
                "Tomorrow at " + DateFormat.format("HH:mm:ss", neededTime)
            } else if (nowTime[Calendar.DATE] == neededTime[Calendar.DATE]) {
                //here return like "Today at 12:00"
                "Today at " + DateFormat.format("HH:mm:ss", neededTime)
            } else if (nowTime[Calendar.DATE] - neededTime[Calendar.DATE] == 1) {
                //here return like "Yesterday at 12:00"
                "Yesterday at " + DateFormat.format("HH:mm:ss", neededTime)
            } else {
                //here return like "May 31, 12:00"
                DateFormat.format("MMMM d, HH:mm:ss", neededTime).toString()
            }
        } else {
            //here return like "May 31, 12:00"
            DateFormat.format("MMMM d, HH:mm:ss", neededTime).toString()
        }
    } else {
        //here return like "May 31 2010, 12:00" - it's a different year we need to show it
        DateFormat.format("MMMM dd yyyy, HH:mm:ss", neededTime).toString()
    }
}


fun View?.addWithAnimation(duration: Long = 300) {
    this?.animate()?.translationY(0F)?.alpha(1f)?.setDuration(duration)
        ?.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                this@addWithAnimation.visibility = View.VISIBLE
            }
        })
}

fun TextView.setFlag(country: String) {
    /* val flagOffset = 0x1F1E6
     val asciiOffset = 0x41

     val firstChar = Character.codePointAt(country, 0) - asciiOffset + flagOffset
     val secondChar = Character.codePointAt(country, 1) - asciiOffset + flagOffset

     val flag = (String(Character.toChars(firstChar))
             + String(Character.toChars(secondChar)))

     text = flag
 */

}

fun EditText.multilineIme(action: Int) {
    inputType = EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
//    horizontalScroll(false)
    setHorizontallyScrolling(false)
    maxLines = Integer.MAX_VALUE
}

fun View?.removeWithAnimation(duration: Long = 300) {
    this?.animate()?.translationY(this@removeWithAnimation.height.toFloat())?.alpha(0.0f)
        ?.setDuration(duration)?.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                this@removeWithAnimation.visibility = View.GONE
            }
        })
}

fun Context?.toast(message: String?, time: Int = Toast.LENGTH_SHORT) {
    this?.let {
        Toast.makeText(it, message, time).show()
    }
}

fun Fragment?.toast(message: String, time: Int = Toast.LENGTH_SHORT) {
    this?.activity?.let {
        Toast.makeText(it, message, time).show()
    }
}

fun debug(message: String?) {
    Log.e("OkMainDebug", "${message}")
}

fun Fragment.getColor(color: Int) {
    ContextCompat.getColor(requireContext(), color)
}

fun Activity.getColor(color: Int) {
    ContextCompat.getColor(this, color)
}

fun View?.hideKeyboard(context: Context) {
    this.apply {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this?.windowToken, 0)
    }
}

fun TextView.setColor(color: Int) {
    setTextColor(ContextCompat.getColor(context, color))
}

fun String.toRequestBody() = RequestBody.create("text/plain".toMediaTypeOrNull(), this)

fun EditText.toRequestBody(): RequestBody =
    RequestBody.create("text/plain".toMediaTypeOrNull(), this.text.toString())


fun File.toMultipartBody(
    fieldName: String,
    parseType: String = "multipart/form-data",
    fileName: String = URLEncoder.encode(this.name, "utf-8")
): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        fieldName, fileName, (RequestBody.create(parseType.toMediaTypeOrNull(), this))
    )
}

fun ImageView?.loadFromUrl(
    imageUrl: String?,
    placeHolder: Int = R.color.grey
) {
    try {
        if (this != null)
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(this.context)
                    .load(imageUrl)
                    .placeholder(placeHolder)
                    .error(placeHolder)
                    .into(this)
            } else {
                this.setImageResource(placeHolder)
            }

    } catch (e: Exception) {
        debug(e.message)
    }
}

fun ImageView?.loadFromUrlWOP(
    imageUrl: String?
) {
    try {
        if (this != null)
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(this.context)
                    .load(imageUrl)
                    .error(R.color.grey)
                    .into(this)
            } else {
                this.setImageResource(R.color.grey)
            }

    } catch (e: Exception) {
        debug(e.message)
    }
}

fun AppBarLayout.appBarFix(view: View, verticalOffset: Int, marginValue: Float) {
    val maxScroll = totalScrollRange
    val percentage =
        (100 * abs(verticalOffset).toFloat() / maxScroll.toFloat()).toInt()

    val params = LinearLayout.LayoutParams(view.layoutParams)
    val r: Resources = this.context.getResources()
    val dp = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        marginValue,
        r.displayMetrics
    ).toInt()

    val marginTop = dp * percentage / 100

    params.setMargins(0, marginTop, 0, 0)
    view.layoutParams = params
}

fun View.appBarFix(appBarLayout: AppBarLayout, marginValue: Float) {
    appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
        val maxScroll = appBarLayout.totalScrollRange
        val percentage =
            (100 * abs(verticalOffset).toFloat() / maxScroll.toFloat()).toInt()

        val params = LinearLayout.LayoutParams(this.layoutParams)
        val r: Resources = this.context.getResources()
        val dp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            marginValue,
            r.displayMetrics
        ).toInt()

        val marginTop = dp * percentage / 100

        params.setMargins(0, marginTop, 0, 0)
        this.layoutParams = params
    })
}

fun CharSequence?.isValidEmail(): Boolean {
    return TextUtils.isEmpty(this) || !Patterns.EMAIL_ADDRESS.matcher(this!!).matches()
}

/*
fun CharSequence?.isValidMobile(): Boolean {
    return Patterns.PHONE.matcher(this!!).matches()
}*/


fun EditText.isNotEmpty(msg: String): Boolean {
    if (this.text.isNotBlank()) {
        return true
    }
    this.context.toast(msg)
    return false
}


fun TextInputEditText.isNotEmpty(msg: String): Boolean {
    if (this.text != null && this.text?.isNotBlank()!!) {
        return true
    }
    this.context.toast(msg)
    return false
}

/*------------ Email Validation --------------*/
fun String.isValidEmail(): Boolean {
    return TextUtils.isEmpty(this.trim()) || !Patterns.EMAIL_ADDRESS.matcher(this.trim()).matches()
}

/*------------ TextView text Color change -------------*/
fun getColoredSpanned(text: String, color: Int): String {
    return "<b><font color=$color> $text</font></b>"
}

/*----------- Status Bar TextColor Black/White ------------*/
fun lightStatusBar(activity: Activity) {
    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
}

/*------------ Status Bar Color Change --------------*/
fun setStatusBarColor(activity: Activity, color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.window?.statusBarColor = ContextCompat.getColor(activity, color)
    }
}

/*------------- Status Bar Transparent Color -----------*/
fun headerStatusTransparent(activity: Activity?) {

    val window: Window? = (activity)?.window
    window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        window?.statusBarColor = 0x00000000
    }
}

/*------------- Status Bar Stable Layout -----------*/
fun headerStable(activity: Activity) {

    val window: Window = (activity).window
    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE)

}

fun String.isValidPhoneNumber() : Boolean {
    val patterns =  "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$"
    return Pattern.compile(patterns).matcher(this).matches()
}

fun String.isValidPINcode() : Boolean {
    val patterns =  "^[1-9][0-9]{5}\$"
    return Pattern.compile(patterns).matcher(this).matches()
}

fun String.isValidGST() : Boolean {
    val patterns =  "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}[0-9A-Z]{1}[0-9]{1}"
    return Pattern.compile(patterns).matcher(this).matches()
}
fun String.isValidAdharCard() : Boolean {
    val patterns =  "^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}\$"
    return Pattern.compile(patterns).matcher(this).matches()
}
fun String.isIFSC() : Boolean {
    val patterns =  "^[A-Z]{4}[A-Z0-9]{7}\$"
    return Pattern.compile(patterns).matcher(this).matches()
}


/*--------------- FullScreen layout & Stable status bar ----------*/
fun headerFullAndStable(activity: Activity) {
    val window: Window = (activity).window
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
}



fun fullScreenStatusBar(activity: Activity) {
    activity.window.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )

}










