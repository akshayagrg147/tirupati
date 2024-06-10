package com.varuna.parivartan.views

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet

open class CustomTextView : AppCompatTextView {
    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, -1)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}