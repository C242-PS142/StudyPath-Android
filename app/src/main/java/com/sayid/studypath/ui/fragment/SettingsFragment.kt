package com.sayid.studypath.ui.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.sayid.studypath.R
import com.sayid.studypath.data.Result
import com.sayid.studypath.databinding.FragmentSettingsBinding
import com.sayid.studypath.ui.activity.LoginActivity
import com.sayid.studypath.utils.showToast
import com.sayid.studypath.viewmodel.SettingsViewModel
import com.sayid.studypath.viewmodel.factory.ViewModelFactory

class SettingsFragment : Fragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private var hasAnimated = false

    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }

    private val settingsViewModel: SettingsViewModel by viewModels {
        factory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.getBoolean(HAS_ANIMATED)?.let {
            hasAnimated = it
            binding.settingsLayout.alpha = 1.0f
        }
        settingsViewModel.isDarkTheme.observe(viewLifecycleOwner) { isDarkTheme ->
            if (isDarkTheme != null) binding.switchTheme.isChecked = isDarkTheme
        }
        var fetchOnceMore = false
        settingsViewModel.userResponse.observe(viewLifecycleOwner) { result ->
            binding.apply {
                if (result != null) {
                    when (result) {
                        is Result.Loading -> loading.visibility = View.VISIBLE
                        is Result.Success -> {
                            val userData = result.data

                            Glide
                                .with(this@SettingsFragment)
                                .load(userData.avatar)
                                .placeholder(R.drawable.undraw_male_avatar)
                                .circleCrop()
                                .into(profilePict)
                            username.text = userData.name
                            emailUser.text = userData.email
                            loading.visibility = View.GONE

                            editProfile(userData.name, userData.avatar)
                        }

                        is Result.Error -> {
                            loading.visibility = View.GONE
                            if (!fetchOnceMore) {
                                settingsViewModel.getUserData()
                                fetchOnceMore = true
                            } else {
                                showToast(requireContext(), "Gagal Memuat Data Pengguna")
                            }
                            Log.d("RESULT", result.error)
                        }
                    }
                }
            }
        }

        binding.apply {
            btnLogout.setOnClickListener {
                settingsViewModel.signOut()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }
            switchTheme.setOnClickListener {
                settingsViewModel.setDarkTheme(switchTheme.isChecked)
            }
        }

        playAnimation()
    }

    private fun editProfile(
        name: String,
        avatar: String,
    ) {
        binding.btnEditProfile.setOnClickListener {
            it.isEnabled = false
            val dialog =
                EditProfileDialogFragment.showDialog(
                    name = name,
                    avatarUrl = avatar,
                )
            parentFragmentManager.setFragmentResultListener(
                "edit_profile_result",
                viewLifecycleOwner,
            ) { _, bundle ->
                val isUpdate = bundle.getBoolean("update", false)
                if (isUpdate) settingsViewModel.getUserData()
            }
            dialog.show(parentFragmentManager, "EditProfileDialog")
            it.postDelayed({ it.isEnabled = true }, 1000)
        }
    }

    private fun playAnimation() {
        if (hasAnimated) {
            binding.settingsLayout.alpha = 1.0f
            return
        } else {
            hasAnimated = true
            val containerAlpha =
                ObjectAnimator.ofFloat(binding.settingsLayout, View.ALPHA, 0f, 1f).setDuration(750)
            val containerMove =
                ObjectAnimator
                    .ofFloat(binding.settingsLayout, View.TRANSLATION_Y, 250f, 0f)
                    .setDuration(750)
            AnimatorSet().apply {
                playTogether(containerAlpha, containerMove)
                start()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(HAS_ANIMATED, hasAnimated)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val HAS_ANIMATED = "has_animated"
    }
}
