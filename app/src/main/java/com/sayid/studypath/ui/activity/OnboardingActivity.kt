package com.sayid.studypath.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.sayid.studypath.R
import com.sayid.studypath.data.model.OnboardingPreference
import com.sayid.studypath.databinding.ActivityOnboardingBinding
import com.sayid.studypath.ui.adapter.OnboardingPageAdapter
import com.sayid.studypath.utils.updateIndicator
import com.sayid.studypath.viewmodel.OnboardingViewModel
import com.sayid.studypath.viewmodel.factory.OnboardingViewModelFactory

class OnboardingActivity : AppCompatActivity() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: ActivityOnboardingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: OnboardingViewModel

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
                    viewModel.completeOnboarding()
                }
            }
        }
    }

    private fun hasOnboardingCompleted() {
        val onboardingPreference = OnboardingPreference(this)
        viewModel =
            ViewModelProvider(
                this,
                OnboardingViewModelFactory(onboardingPreference),
            )[OnboardingViewModel::class.java]

        viewModel.onboardingCompleted.observe(this) { completed ->
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
                R.drawable.undraw_onboarding_welcoming,
                R.drawable.undraw_onboarding_exams,
                R.drawable.undraw_onboarding_mobile_app,
                R.drawable.undraw_onboarding_chat_bot,
                R.drawable.undraw_onboarding_certification,
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
