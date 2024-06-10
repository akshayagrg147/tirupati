package com.tirupati.vendor.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.view.animation.*


fun View.fade(from: Float, to: Float, duration: Long, func: (Animation) -> Unit = {}) {
    val fadeIn = AlphaAnimation(from, to)
    fadeIn.duration = duration
    func(fadeIn)
    startAnimation(fadeIn)
}

fun View.translateRepeat(
    fromX: Float,
    toX: Float,
    fromY: Float,
    toY: Float,
    duration: Long,
    func: (Animation) -> Unit = {},
) {
    val translate = TranslateAnimation(
        Animation.ABSOLUTE, fromX, Animation.ABSOLUTE, toX,
        Animation.ABSOLUTE, fromY, Animation.ABSOLUTE, toY
    )
    translate.duration = duration
    translate.repeatCount = Animation.INFINITE
    func(translate)
    startAnimation(translate)
}

fun View.translate(
    fromX: Float,
    toX: Float,
    fromY: Float,
    toY: Float,
    duration: Long,
    func: (Animation) -> Unit = {},
) {
    val translate = TranslateAnimation(
        Animation.ABSOLUTE, fromX, Animation.ABSOLUTE, toX,
        Animation.ABSOLUTE, fromY, Animation.ABSOLUTE, toY
    )
    translate.duration = duration
    func(translate)
    startAnimation(translate)
}

fun View.scale(
    fromX: Float,
    toX: Float,
    fromY: Float,
    toY: Float,
    pivotX: Float,
    pivotY: Float,
    duration: Long,
    func: (Animation) -> Unit = {}
) {
    val anim = ScaleAnimation(
        fromX,
        toX,
        fromY,
        toY,
        Animation.RELATIVE_TO_SELF,
        pivotX,
        Animation.RELATIVE_TO_SELF,
        pivotY
    )
    anim.duration = duration
    func(anim)
    startAnimation(anim)
}


fun View.scale(
    fromX: Float,
    toX: Float,
    fromY: Float,
    toY: Float,
    pivotX: Float,
    pivotY: Float,
    startOffset: Long,
    duration: Long,
    repeat: Boolean,
    reverse: Int,
    func: (Animation) -> Unit = {},
) {
    val anim = ScaleAnimation(
        fromX,
        toX,
        fromY,
        toY,
        Animation.RELATIVE_TO_SELF,
        pivotX,
        Animation.RELATIVE_TO_SELF,
        pivotY
    )
    anim.duration = duration
    anim.startOffset = startOffset
    if (repeat) {
        when (reverse) {
            0 -> anim.repeatMode = Animation.REVERSE
            1 -> anim.repeatMode = Animation.RESTART
        }
        anim.repeatCount = Animation.INFINITE
    }
    func(anim)
    startAnimation(anim)
}

fun View.scale(
    fromX: Float,
    toX: Float,
    fromY: Float,
    toY: Float,
    startOffset: Long,
    duration: Long,
    repeat: Boolean,
    reverse: Int,
    func: (Animation) -> Unit = {},
) {
    val anim = ScaleAnimation(fromX, toX, fromY, toY)
    anim.duration = duration
    anim.startOffset = startOffset
    if (repeat) {
        when (reverse) {
            0 -> anim.repeatMode = Animation.REVERSE
            1 -> anim.repeatMode = Animation.RESTART
        }
        anim.repeatCount = Animation.INFINITE
    }
    func(anim)
    startAnimation(anim)
}

@SuppressLint("NotifyDataSetChanged")
fun ViewGroup.runLayoutAnimation(resourceId: Int) {
    val context = this.context
    val controller = AnimationUtils.loadLayoutAnimation(context, resourceId)

    this.layoutAnimation = controller
    (this as? androidx.recyclerview.widget.RecyclerView)?.adapter?.notifyDataSetChanged()
    this.scheduleLayoutAnimation()
}


fun View.expand() {
    val matchParentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec((this.parent as View).width, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    this.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = this.measuredHeight

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    this.layoutParams.height = 1
    this.visibility = View.VISIBLE
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            layoutParams.height =
                if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Expansion speed of 1dp/ms
    a.duration = (targetHeight / this.context.resources.displayMetrics.density).toInt().toLong()
    startAnimation(a)
}

fun View.fastExpand() {
    val matchParentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec((this.parent as View).width, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    this.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = this.measuredHeight

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    this.layoutParams.height = 1
    this.visibility = View.VISIBLE
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            layoutParams.height =
                if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Expansion speed of 1dp/ms
    a.duration = (500).toInt().toLong()
    startAnimation(a)
}

fun View.collapse() {
    val initialHeight = this.measuredHeight
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                visibility = View.GONE
            } else {
                layoutParams.height =
                    initialHeight - (initialHeight * interpolatedTime).toInt()
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Collapse speed of 1dp/ms
    a.duration = (initialHeight / this.context.resources.displayMetrics.density).toInt().toLong()
    startAnimation(a)
}

fun View.fastCollapse() {
    val initialHeight = this.measuredHeight
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                visibility = View.GONE
            } else {
                layoutParams.height =
                    initialHeight - (initialHeight * interpolatedTime).toInt()
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Collapse speed of 1dp/ms
    a.duration = (500).toInt().toLong()
    startAnimation(a)
}


fun View.fadeOut() {
    val initialHeight = this.measuredHeight
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                visibility = View.GONE
            } else {
                layoutParams.height =
                    initialHeight - (initialHeight * interpolatedTime).toInt()
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Collapse speed of 1dp/ms
    a.duration = (500).toInt().toLong()
    startAnimation(a)
}


//            binding.feedbacksFab.animate()
//                .translationX(-width.toFloat())
//                .setDuration(2000)
//                .setInterpolator(LinearInterpolator())
//                .setListener(object : AnimatorListenerAdapter() {
//                    override fun onAnimationEnd(animation: Animator) {
//                        super.onAnimationEnd(animation)
//                        //camion.setVisibility(View.GONE);
//                    }
//                })


