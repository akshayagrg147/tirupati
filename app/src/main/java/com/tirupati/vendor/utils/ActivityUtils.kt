package com.tirupati.vendor.utils

import android.app.Activity
import android.content.Intent

fun <T> Activity.moveToActivity(cls: Class<T>){
    startActivity(Intent(this,cls))
}
fun onAttach(activity: Activity,title:String){
    activity?.title = "My Title"
}
