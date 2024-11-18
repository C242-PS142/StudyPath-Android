package com.sayid.studypath.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.sayid.studypath.R
import com.sayid.studypath.databinding.ActivityOnboardingBinding
import com.sayid.studypath.ui.adapter.OnboardingPageAdapter
import com.sayid.studypath.utils.updateIndicator
import com.sayid.studypath.viewmodel.OnboardingViewModel

class OnboardingActivity : AppCompatActivity() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: ActivityOnboardingBinding? = null
    private val binding get() = _binding!!
    private val onboardingViewModel: OnboardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hasOnboardingCompleted()
        initializeOnboardingPage()
        setListener()
    }

    private fun setListener() {
        binding.apply {
            btnOnboarding.setOnClickListener {
                if (binding.viewPager.currentItem < (
                        binding.viewPager.adapter?.itemCount
                            ?: 0
                    ) - 1
                ) {
                    binding.viewPager.currentItem += 1
                } else {
                    onboardingViewModel.completeOnboarding()
                }
            }
        }
    }

    private fun hasOnboardingCompleted() {
        onboardingViewModel.onboardingCompleted.observe(this) { completed ->
            if (completed) {
                navigateToLoginScreen()
            }
        }
    }

    private fun navigateToLoginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
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
