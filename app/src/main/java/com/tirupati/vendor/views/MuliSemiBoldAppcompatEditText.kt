package com.varuna.parivartan.views

import android.content.Context
import android.util.AttributeSet

/**
 * @author surya devi
 */
class MuliSemiBoldAppcompatEditText : androidx.appcompat.widget.AppCompatEditText {
    constructor(context: Context) : super(context) {}

    private fun applyCustomFont(font: Context) {
        val customFont = typeface
        typeface = customFont
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        applyCustomFont(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        applyCustomFont(context)
    }
}
