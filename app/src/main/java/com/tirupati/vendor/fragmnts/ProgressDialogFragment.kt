package com.tirupati.vendor.fragmnts;

import android.app.Dialog
import android.os.Bundle
import android.view.*
import com.tirupati.vendor.R
import com.tirupati.vendor.databinding.InflateProgressViewBinding

class ProgressDialogFragment : androidx.fragment.app.DialogFragment() {

    var binding: InflateProgressViewBinding? = null

    companion object {
        fun newInstance(): ProgressDialogFragment {
            val dialogFragment = ProgressDialogFragment()
            dialogFragment.isCancelable = false
            return dialogFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = InflateProgressViewBinding.inflate(inflater, container, false)
        /*binding?.progressView?.visibility = View.VISIBLE
        binding?.progressView?.show()*/
        return binding?.root
    }

    override fun dismiss() {
       /* binding?.progressView?.hide()
        binding?.progressView?.visibility = View.GONE*/
        if (dialog != null)
            super.dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE);
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }
    override fun onStart() {
        super.onStart()

        val window = dialog!!.window
        val windowParams = window!!.attributes
        windowParams.dimAmount = 0f

        window.decorView.setBackgroundResource(R.color.transparent)
        windowParams.flags = windowParams.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        window.attributes = windowParams
    }
}