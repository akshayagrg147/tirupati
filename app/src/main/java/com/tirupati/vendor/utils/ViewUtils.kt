package com.tirupati.vendor.utils

import android.view.View

fun View?.add(){
    this?.visibility= View.VISIBLE
}
fun View?.remove(){
    this?.visibility= View.GONE
}
fun View?.invisible(){
    this?.visibility= View.INVISIBLE
}