package com.sayid.studypath.ui.fragment

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.sayid.studypath.R
import com.sayid.studypath.data.Result
import com.sayid.studypath.databinding.FragmentSettingsBinding
import com.sayid.studypath.service.QuizReminder
import com.sayid.studypath.ui.activity.LoginActivity
import com.sayid.studypath.utils.UserLoginDataSingleton
import com.sayid.studypath.utils.showToast
import com.sayid.studypath.viewmodel.SettingsViewModel
import com.sayid.studypath.viewmodel.factory.ViewModelFactory

class SettingsFragment : Fragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private var hasAnimated = false
    private lateinit var quizReminder: QuizReminder

    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(requireContext())
    }

    private val settingsViewModel: SettingsViewModel by viewModels {
        factory
    }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                showToast(requireContext(), "Permission granted")
                Log.d("Permission", "Permission granted")
            }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestPermissionLauncher.launch(Manifest.permission.SCHEDULE_EXACT_ALARM)
        }

        quizReminder = QuizReminder()

        savedInstanceState?.getBoolean(HAS_ANIMATED)?.let {
            hasAnimated = it
            binding.settingsLayout.alpha = 1.0f
        }

        settingsViewModel.isDarkTheme.observe(viewLifecycleOwner) { isDarkTheme ->
            if (isDarkTheme != null) binding.switchTheme.isChecked = isDarkTheme
        }

        settingsViewModel.isReminderSet.observe(viewLifecycleOwner) {
            binding.btnReminder.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    if (it) R.drawable.baseline_access_time_filled_24 else R.drawable.baseline_access_time_24,
                ),
            )
        }

        var fetchOnceMore = false
        UserLoginDataSingleton.userLoginData.observe(viewLifecycleOwner) { result ->
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
                        }
                    }
                }
            }
        }
        setListener()
        playAnimation()
    }

    private fun setListener() {
        binding.apply {
            btnLogout.setOnClickListener {
                val dialog =
                    AlertDialog
                        .Builder(requireContext())
                        .setTitle("Keluar")
                        .setMessage("Apakah Kamu Yakin Ingin Keluar?")
                        .setPositiveButton("Ya") { _, _ ->
                            settingsViewModel.signOut()
                            startActivity(Intent(requireContext(), LoginActivity::class.java))
                            requireActivity().finish()
                        }.setNegativeButton("Tidak") { dialog, _ ->
                            dialog.dismiss()
                        }.create()

                dialog.window?.setBackgroundDrawableResource(R.drawable.bg_radius_xlarge)

                dialog.setOnShowListener {
                    val positiveButton = dialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                    val negativeButton = dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)

                    positiveButton.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.md_theme_error,
                        ),
                    )
                    negativeButton.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.md_theme_onSurface,
                        ),
                    )
                }

                dialog.show()
            }
            switchTheme.setOnCheckedChangeListener { _, isChecked ->
                settingsViewModel.setDarkTheme(
                    isChecked,
                )
            }
            btnReminder.setOnClickListener {
                val dialog =
                    QuizPreferenceDialogFragment.showDialog()

                parentFragmentManager.setFragmentResultListener(
                    "set_reminder",
                    viewLifecycleOwner,
                ) { _, bundle ->
                    val onceDate = bundle.getString("date")
                    val onceTime = bundle.getString("time")
                    val onceMessage = "Ayo Tes Ulang Kepribadian Kamu!"

                    if (!onceDate.isNullOrEmpty() && !onceTime.isNullOrEmpty()) {
                        if (onceDate.contains("batal") && onceTime.contains("batal")) {
                            try {
                                checkAndRequestPermission {
                                    quizReminder.cancelOneTimeAlarm(requireContext())
                                    settingsViewModel.setReminder(
                                        false,
                                    )
                                }
                            } catch (e: Exception) {
                                showToast(requireContext(), e.message.toString())
                            }
                        } else {
                            try {
                                checkAndRequestPermission {
                                    quizReminder.setOneTimeAlarm(
                                        requireContext(),
                                        QuizReminder.TYPE_ONE_TIME,
                                        onceDate,
                                        onceTime,
                                        onceMessage,
                                    )
                                    settingsViewModel.setReminder(
                                        true,
                                    )
                                }
                            } catch (e: Exception) {
                                showToast(requireContext(), e.message.toString())
                                Log.e("ERROR", e.message.toString())
                            }
                        }
                    } else {
                        showToast(requireContext(), "Gagal: Waktu belum ditentukan!")
                    }
                }
                dialog.show(parentFragmentManager, "EditPreferensiQuiz")
                btnReminder.postDelayed({ btnReminder.isEnabled = true }, 1000)
            }
        }
    }

    private fun checkAndRequestPermission(onGranted: () -> Unit) {
        if (Build.VERSION.SDK_INT >= 33) {
            when {
                requireContext().checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) ==
                    android.content.pm.PackageManager.PERMISSION_GRANTED -> {
                    onGranted()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    AlertDialog
                        .Builder(requireContext())
                        .setTitle("Permission Needed")
                        .setMessage("We need this permission to send notifications.")
                        .setPositiveButton("OK") { _, _ ->
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }.setNegativeButton("Cancel", null)
                        .show()
                }

                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            onGranted()
        }
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
