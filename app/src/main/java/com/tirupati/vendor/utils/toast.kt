package com.tirupati.vendor.utils

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tirupati.vendor.R
import com.bumptech.glide.Glide


fun Context?.toast(message: String?, timeout: Int = Toast.LENGTH_SHORT) {
    if (this != null)
        Toast.makeText(this, "$message", timeout).show()
}

fun Fragment?.toast(message: String?, timeout: Int = Toast.LENGTH_SHORT) {
    try {
        if (this != null)
            Toast.makeText(context, "$message", timeout).show()
    } catch (e: Exception) {
    }
}

fun Activity?.toast(message: String?, timeout: Int = Toast.LENGTH_SHORT) {
    try {
        if (this != null)
            Toast.makeText(this, "$message", timeout).show()
    } catch (e: Exception) {
    }
}

fun ImageView?.loadFromUrl(imageUrl: String?, placeHolder: Int = R.color.greyBorderColor) {
    try {
        if (this != null && imageUrl != null)
            Glide.with(this.context)
                .load(imageUrl)
                .placeholder(placeHolder)
                .error(placeHolder)
                .into(this)
    } catch (e: Exception) {
    }
}


fun ImageView?.load(drawable: Int, placeHolder: Int = R.drawable.ic_launcher_background) {
    try {
        if (this != null)
            Glide.with(this.context)
                .load(drawable)
                .placeholder(placeHolder)
                .error(placeHolder)
                .into(this)
    } catch (e: Exception) {
    }
}
