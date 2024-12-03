package com.sayid.studypath.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.DialogFragment
import com.sayid.studypath.R
import com.sayid.studypath.databinding.FragmentRecommendationDialogBinding

class RecommendationDialogFragment(private val title: String, private val description: String, private val recommendation: String, private val illustration: Int): DialogFragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentRecommendationDialogBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding =
            FragmentRecommendationDialogBinding.inflate(LayoutInflater.from(requireContext()))

        val builder =
            AlertDialog
                .Builder(requireContext())
                .setView(binding.root)
                .setNegativeButton("Tutup") { dialog, _ ->
                    dialog.dismiss()
                }
        val dialog = builder.create()

        dialog.setOnShowListener {
            dialog.window?.setBackgroundDrawableResource(R.drawable.bg_radius_xlarge)

            binding.apply {
               illustrationPersonality.setImageDrawable(getDrawable(requireActivity(), illustration))
                titlePersonality.text = title
                descriptionPersonality.text = description
                recommendationPersonality.text = recommendation
            }
        }
        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun showDialog(title: String, description: String, recommendation: String, illustration: Int): RecommendationDialogFragment {
            val fragment = RecommendationDialogFragment(title, description, recommendation, illustration)
            val args =
                Bundle().apply {
                }
            fragment.arguments = args
            return fragment
        }
    }
}