package com.sayid.studypath.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.sayid.studypath.R
import com.sayid.studypath.databinding.FragmentHomeBinding
import com.sayid.studypath.ui.adapter.PersonalityCardAdapter

class HomeFragment : Fragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvUsername.text = "Sayid Achmad Maulana"
        initializeCarousel()

        binding.apply {
            btnGetRecommendation.setOnClickListener {
                if (tvWait.visibility == View.GONE) {
                    tvRecommendation.visibility = View.VISIBLE
                    tvWait.visibility = View.VISIBLE
                } else {
                    tvRecommendation.visibility = View.GONE
                    tvWait.visibility = View.GONE
                }
            }
        }
    }

    private fun initializeCarousel() {
        val gradients =
            listOf(
                R.drawable.gradient_agreeableness,
                R.drawable.gradient_conscientiousness,
                R.drawable.gradient_extraversion,
                R.drawable.gradient_neuroticism,
                R.drawable.gradient_openness,
            )

        val personalityData =
            mapOf(
                "Agreeableness" to "Kesesuaian atau\nKebersamaan",
                "Extraversion" to "Interaksi\nSosial",
                "Conscientiousness" to "Ketekunan atau\nKehati-hatian",
                "Neuroticism" to "Kestabilan Emosi",
                "Openness" to "Keterbukaan",
            )

        val illustrations =
            listOf(
                R.drawable.undraw_business_deal,
                R.drawable.undraw_solution_mindset,
                R.drawable.undraw_visionary_technology,
                R.drawable.undraw_meditation,
                R.drawable.undraw_shared_goals,
            )

        val dummyPercentages =
            listOf(
                60,
                90,
                75,
                40,
                20,
            )

        val viewPager = binding.viewPager
        val adapter =
            PersonalityCardAdapter(
                requireContext(),
                gradients,
                personalityData,
                illustrations,
                dummyPercentages,
            )
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateIndicator(position)
                }
            },
        )
    }

    private fun updateIndicator(currentPosition: Int) {
        val indicatorLayout = binding.indicatorLayout
        for (i in 0 until indicatorLayout.childCount) {
            val view = indicatorLayout.getChildAt(i)
            if (i == currentPosition) {
                view.setBackgroundResource(R.drawable.indicator_active)
            } else {
                view.setBackgroundResource(R.drawable.indicator_inactive)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
