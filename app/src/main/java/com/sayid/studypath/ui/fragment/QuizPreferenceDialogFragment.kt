package com.sayid.studypath.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.sayid.studypath.R
import com.sayid.studypath.databinding.FragmentQuizPreferenceDialogBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class QuizPreferenceDialogFragment : DialogFragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentQuizPreferenceDialogBinding? = null
    private var onPressed: Boolean = false
    private val binding get() = _binding!!

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding =
            FragmentQuizPreferenceDialogBinding.inflate(LayoutInflater.from(requireContext()))

        val builder =
            AlertDialog
                .Builder(requireContext())
                .setTitle("Atur Pengingat")
                .setView(binding.root)
                .setPositiveButton("Simpan", null)
                .setNegativeButton("Hapus") { dialog, _ ->
                    delete()
                    dialog.dismiss()
                }
        val dialog = builder.create()

        dialog.setOnShowListener {
            dialog.window?.setBackgroundDrawableResource(R.drawable.bg_radius_xlarge)

            binding.apply {
                btnOnceDate.setOnClickListener {
                    val calendar = Calendar.getInstance()
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)
                    val datePickerDialog =
                        DatePickerDialog(
                            requireContext(),
                            { _, selectedYear, selectedMonth, selectedDay ->
                                onDateSet(selectedYear, selectedMonth, selectedDay)
                            },
                            year,
                            month,
                            day,
                        )
                    datePickerDialog.show()
                }
                btnOnceTime.setOnClickListener {
                    val calendar = Calendar.getInstance()
                    val hour = calendar.get(Calendar.HOUR_OF_DAY)
                    val minute = calendar.get(Calendar.MINUTE)

                    val timePickerDialog =
                        TimePickerDialog(
                            requireContext(),
                            { _, selectedHour, selectedMinute ->
                                onTimeSet(selectedHour, selectedMinute)
                            },
                            hour,
                            minute,
                            true,
                        )
                    timePickerDialog.show()
                }

                val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

                positiveButton.setOnClickListener {
                    val onceDate = tvOnceDate.text.toString().trim()
                    val onceTime = tvOnceTime.text.toString().trim()

                    if (onPressed) return@setOnClickListener
                    onPressed = true

                    parentFragmentManager.setFragmentResult(
                        "set_reminder",
                        Bundle().apply {
                            putString("date", onceDate)
                            putString("time", onceTime)
                        },
                    )
                    dialog?.dismiss()
                }
            }
        }
        return dialog
    }

    private fun delete() {
        parentFragmentManager.setFragmentResult(
            "set_reminder",
            Bundle().apply {
                putString("date", "batal")
                putString("time", "batal")
            },
        )
    }

    private fun onDateSet(
        year: Int,
        month: Int,
        dayOfMonth: Int,
    ) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        binding.tvOnceDate.text = dateFormat.format(calendar.time)
    }

    private fun onTimeSet(
        hourOfDay: Int,
        minute: Int,
    ) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        binding.tvOnceTime.text = dateFormat.format(calendar.time)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun showDialog(): QuizPreferenceDialogFragment {
            val fragment = QuizPreferenceDialogFragment()
            val args =
                Bundle().apply {
                }
            fragment.arguments = args
            return fragment
        }
    }
}
