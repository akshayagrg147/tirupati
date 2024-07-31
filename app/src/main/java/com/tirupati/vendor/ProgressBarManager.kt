package com.tirupati.vendor

import android.app.ProgressDialog
import android.content.Context

class ProgressDialogHelper(private val context: Context) {

    private var progressDialog: ProgressDialog? = null

    fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(context).apply {
                setTitle("Please Wait")
                setMessage("Loading ...")
                setCancelable(false)
            }
        }
        progressDialog?.show()
    }

    fun dismissProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }
}
