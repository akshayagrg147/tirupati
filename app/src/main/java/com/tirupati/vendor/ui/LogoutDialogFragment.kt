package com.tirupati.vendor.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import com.tirupati.vendor.databinding.DialogLogoutBinding
import com.tirupati.vendor.utils.fade
import com.tirupati.vendor.utils.scale
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogoutDialogFragment : androidx.fragment.app.DialogFragment() {
    var binding: DialogLogoutBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DialogLogoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.tvNo?.setOnClickListener {
            stopAnimation()
            clickListener?.onClick(it)
        }

        binding?.tvYes?.setOnClickListener {
            stopAnimation()
            clickListener?.onClick(it)
        }

        startAnimation()
        binding?.viewBackground?.setOnClickListener {
            stopAnimation()
        }
    }

    private fun startAnimation() {

        binding?.viewBackground?.fade(0f, 1f, 200)
        binding?.flCard?.scale(0f, 1f, 0f, 1f, 0.5f, 0.5f, 200) {
            it.interpolator = DecelerateInterpolator()
        }
    }

    private fun stopAnimation() {
        binding?.viewBackground?.fade(1f, 0f, 200)
        binding?.flCard?.scale(1f, 0f, 1f, 0f, 0.5f, 0.5f, 200) {
            it.interpolator = DecelerateInterpolator()
            it.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    dismiss()
                }

                override fun onAnimationStart(animation: Animation?) {
                }
            })
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
                super.onBackPressed()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window!!.setLayout(width, height)
        }
    }

    var clickListener: View.OnClickListener? = null
    fun setOnClickListener(clickListener: View.OnClickListener) {
        this@LogoutDialogFragment.clickListener = clickListener
    }
}