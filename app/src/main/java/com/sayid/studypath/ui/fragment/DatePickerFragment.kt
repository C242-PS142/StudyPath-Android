package com.sayid.studypath.ui.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class DatePickerFragment :
    DialogFragment(),
    DatePickerDialog.OnDateSetListener {
    private var mListener: DialogDateListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        mListener =
            when {
                parent is DialogDateListener -> parent
                context is DialogDateListener -> context
                else -> throw ClassCastException("${parent ?: context} must implement DialogDateListener")
            }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DATE)
        return DatePickerDialog(requireContext(), this, year, month, date)
    }

    override fun onDateSet(
        view: DatePicker,
        year: Int,
        month: Int,
        dayOfMonth: Int,
    ) {
        mListener?.onDialogDateSet(tag, year, month, dayOfMonth)
    }

    interface DialogDateListener {
        fun onDialogDateSet(
            tag: String?,
            year: Int,
            month: Int,
            dayOfMonth: Int,
        )
    }
}
