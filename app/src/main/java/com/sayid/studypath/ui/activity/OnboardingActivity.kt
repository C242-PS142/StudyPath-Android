package com.sayid.studypath.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.sayid.studypath.R
import com.sayid.studypath.databinding.ActivityOnboardingBinding
import com.sayid.studypath.ui.adapter.OnboardingPageAdapter
import com.sayid.studypath.utils.updateIndicator

class OnboardingActivity : AppCompatActivity() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: ActivityOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeOnboardingPage()

        binding.btnOnboarding.setOnClickListener {
            if (binding.viewPager.currentItem < (binding.viewPager.adapter?.itemCount ?: 0) - 1) {
                binding.viewPager.currentItem += 1
            } else {
                val intent = Intent(this, NewUserDataActivity::class.java)
                startActivity(intent)
            }
        }
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
