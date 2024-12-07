package com.sayid.studypath.ui.customview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton

class CustomImageButton
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
    ) : AppCompatImageButton(context, attrs) {
        override fun performClick(): Boolean {
            super.performClick()
            return true
        }
    }
