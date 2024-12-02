package com.sayid.studypath.ui.fragment

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class TimePickerFragment :
    DialogFragment(),
    TimePickerDialog.OnTimeSetListener {
    private var mListener: DialogTimeListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = parentFragment as? DialogTimeListener
            ?: throw ClassCastException("$parentFragment must implement DialogTimeListener")
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val is24HourFormat = true
        return TimePickerDialog(requireContext(), this, hour, minute, is24HourFormat)
    }

    override fun onTimeSet(
        view: TimePicker,
        hourOfDay: Int,
        minute: Int,
    ) {
        mListener?.onDialogTimeSet(tag, hourOfDay, minute)
    }

    interface DialogTimeListener {
        fun onDialogTimeSet(
            tag: String?,
            hourOfDay: Int,
            minute: Int,
        )
    }
}
