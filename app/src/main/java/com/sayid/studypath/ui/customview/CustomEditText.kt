package com.sayid.studypath.ui.customview

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager

class CustomEditText
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
    ) : androidx.appcompat.widget.AppCompatEditText(context, attrs) {
        init {
            inputType = InputType.TYPE_CLASS_TEXT

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard()
                    clearFocus()
                    true
                } else {
                    false
                }
            }
        }

        private fun hideKeyboard() {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        }
    }
