package com.tirupati.vendor.utils

import android.R
import android.app.Activity
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat


fun setStatusBarColor(activity: Activity, statusBar: View, color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val w: Window = activity.getWindow()
        w.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
        //status bar height
        var actionBarHeight = 0
        val tv = TypedValue()
        if (activity.getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight =
                TypedValue.complexToDimensionPixelSize(
                    tv.data,
                    activity.getResources().getDisplayMetrics()
                )
        }
        var statusBarHeight = 0
        val resourceId: Int =
            activity.getResources().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId)
        }


        //action bar height
        statusBar.getLayoutParams().height = actionBarHeight + statusBarHeight
        statusBar.setBackgroundColor(color)
    }
}


fun setStatusBarColor(activity: Activity, color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity?.window?.statusBarColor = ContextCompat.getColor(activity, color)
    }
}


fun removeStatusBar(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.window.statusBarColor = 0x00000000
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity.window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }  // transparent
}

fun addStatusBar(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.window.statusBarColor = 0x00FFFFFF
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        activity.window.decorView.systemUiVisibility =
//            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }  // transparent
}
