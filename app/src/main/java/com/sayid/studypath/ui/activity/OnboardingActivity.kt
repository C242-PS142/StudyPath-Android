package com.sayid.studypath.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.sayid.studypath.R
import com.sayid.studypath.databinding.ActivityOnboardingBinding
import com.sayid.studypath.ui.adapter.OnboardingPageAdapter
import com.sayid.studypath.utils.startActivityNoAnimation
import com.sayid.studypath.utils.updateIndicator
import com.sayid.studypath.viewmodel.OnboardingViewModel
import com.sayid.studypath.viewmodel.factory.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OnboardingActivity : AppCompatActivity() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: ActivityOnboardingBinding? = null
    private val binding get() = _binding!!
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this@OnboardingActivity)
    }

    private val onboardingViewModel: OnboardingViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
            val isCompleted = onboardingViewModel.isOnboardingCompleted()
            val isDarkTheme = onboardingViewModel.isDarkTheme()

            withContext(Dispatchers.Main) {
                when (isDarkTheme) {
                    true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

                when (isCompleted) {
                    true -> navigateToLoginScreen(false)
                    false -> {
                        playAnimation()
                        initializeOnboardingPage()
                        hasOnboardingCompleted()
                        setListener()
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        val linearLayout =
            ObjectAnimator.ofFloat(binding.linearLayout, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playTogether(linearLayout)
            start()
        }
    }

    private fun setListener() {
        binding.apply {
            btnOnboarding.setOnClickListener {
                if (binding.viewPager.currentItem < (
                        binding.viewPager.adapter?.itemCount
                            ?: 0
                    ) - 1
                ) {
                    binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
                } else {
                    onboardingViewModel.completeOnboarding()
                }
            }
        }
    }

    private fun hasOnboardingCompleted() {
        onboardingViewModel.isOnboardingCompleted.observe(this) { completed ->
            if (completed) {
                navigateToLoginScreen(true)
            }
        }
    }

    private fun navigateToLoginScreen(animation: Boolean) {
        val intent = Intent(this, LoginActivity::class.java)
        if (animation) startActivity(intent) else startActivityNoAnimation(intent)
        finish()
    }

    private fun initializeOnboardingPage() {
        val illustrations =
            listOf(
                R.drawable.icon_onboarding_1,
                R.drawable.icon_onboarding_2,
                R.drawable.icon_onboarding_3,
                R.drawable.icon_onboarding_4,
                R.drawable.icon_onboarding_5,
            )
        val personalityData =
            mapOf(
                getString(R.string.onboarding_title_1) to getString(R.string.onboarding_desc_1),
                getString(R.string.onboarding_title_2) to getString(R.string.onboarding_desc_2),
                getString(R.string.onboarding_title_3) to getString(R.string.onboarding_desc_3),
                getString(R.string.onboarding_title_4) to getString(R.string.onboarding_desc_4),
                getString(R.string.onboarding_title_5) to getString(R.string.onboarding_desc_5),
            )

        val viewPager = binding.viewPager
        val adapter =
            OnboardingPageAdapter(
                this,
                illustrations,
                personalityData,
            )
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateIndicator(position, binding.indicatorLayout)
                    binding.btnOnboarding.apply {
                        when (position) {
                            0 -> text = getString(R.string.start)
                            1, 2, 3 -> text = getString(R.string.next)
                            else -> text = getString(R.string.enter)
                        }
                    }
                }
            },
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
