package com.sayid.studypath.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.sayid.studypath.R
import com.sayid.studypath.data.Result
import com.sayid.studypath.databinding.FragmentEditProfileBinding
import com.sayid.studypath.utils.reduceFileImage
import com.sayid.studypath.utils.showToast
import com.sayid.studypath.utils.uriToFile
import com.sayid.studypath.viewmodel.EditProfileViewModel
import com.sayid.studypath.viewmodel.factory.ViewModelFactory
import java.io.File

class EditProfileDialogFragment : DialogFragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null
    private var onPressed: Boolean = false
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }
    private val editProfileViewModel: EditProfileViewModel by viewModels {
        factory
    }

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentEditProfileBinding.inflate(LayoutInflater.from(requireContext()))

        viewModelListener()

        val name = arguments?.getString(ARG_NAME) ?: ""
        val avatarUrl = arguments?.getString(ARG_AVATAR_URL) ?: ""

        val builder =
            AlertDialog
                .Builder(requireContext())
                .setTitle("Edit Profil")
                .setView(binding.root)
                .setPositiveButton("Simpan", null)
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }

        val dialog = builder.create()

        dialog.setOnShowListener {
            dialog.window?.setBackgroundDrawableResource(R.drawable.bg_radius_xlarge)

            binding.apply {
                ibEdit.setOnClickListener {
                    startGallery()
                }
                Glide
                    .with(this@EditProfileDialogFragment)
                    .load(avatarUrl)
                    .circleCrop()
                    .into(ivAvatar)

                edtName.setText(name)

                savedInstanceState?.getString(KEY_CURRENT_IMAGE_URI)?.let { uriString ->
                    currentImageUri = Uri.parse(uriString)
                    showImage()
                }

                val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setOnClickListener {
                    if (onPressed) return@setOnClickListener
                    onPressed = true

                    val edtName = edtName.text.toString().trim()

                    if (currentImageUri == null) {
                        editProfileViewModel.editProfile(
                            edtName,
                            avatarUrl,
                        )
                    } else {
                        val avatar =
                            currentImageUri?.let { uri ->
                                uriToFile(
                                    uri,
                                    requireContext(),
                                ).reduceFileImage()
                            }

                        editProfileViewModel.editProfile(
                            edtName,
                            avatar as File,
                        )
                    }
                }
            }
        }

        return dialog
    }

    private fun viewModelListener() {
        editProfileViewModel.userResponse.observe(this) { result ->
            binding.apply {
                when (result) {
                    is Result.Loading -> loading.visibility = View.VISIBLE

                    is Result.Success -> {
                        loading.visibility = View.GONE
                        parentFragmentManager.setFragmentResult(
                            "edit_profile_result",
                            Bundle().apply {
                                putBoolean("update", true)
                            },
                        )
                        dialog?.dismiss()
                    }

                    is Result.Error -> {
                        loading.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia(),
        ) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                showImage()
            } else {
                showToast(requireContext(), "Tidak ada gambar yang dipilih")
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showImage() {
        binding.apply {
            Glide
                .with(ivAvatar.context)
                .load(currentImageUri)
                .circleCrop()
                .into(ivAvatar)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (currentImageUri != null) outState.putString(KEY_CURRENT_IMAGE_URI, currentImageUri.toString())
    }

    companion object {
        private const val ARG_NAME = "name"
        private const val ARG_AVATAR_URL = "avatar_url"
        private const val KEY_CURRENT_IMAGE_URI = "current_image_uri"

        fun showDialog(
            name: String,
            avatarUrl: String,
        ): EditProfileDialogFragment {
            val fragment = EditProfileDialogFragment()
            val args =
                Bundle().apply {
                    putString(ARG_NAME, name)
                    putString(ARG_AVATAR_URL, avatarUrl)
                }
            fragment.arguments = args
            return fragment
        }
    }
}
