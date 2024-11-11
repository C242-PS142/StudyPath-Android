package com.sayid.studypath.utils

import android.content.Context
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.sayid.studypath.R
import com.sayid.studypath.ui.adapter.PersonalityCardAdapter

fun initializePersonalityCard(
    context: Context,
    viewPager2: ViewPager2,
    indicatorLayout: LinearLayout,
) {
    val gradients =
        listOf(
            R.drawable.gradient_openness,
            R.drawable.gradient_agreeableness,
            R.drawable.gradient_conscientiousness,
            R.drawable.gradient_extraversion,
            R.drawable.gradient_neuroticism,
        )

    val personalityData =
        mapOf(
            "Openness" to "Keterbukaan",
            "Agreeableness" to "Kesesuaian atau\nKebersamaan",
            "Extraversion" to "Interaksi\nSosial",
            "Conscientiousness" to "Ketekunan atau\nKehati-hatian",
            "Neuroticism" to "Kestabilan Emosi",
        )

    val illustrations =
        listOf(
            R.drawable.undraw_shared_goals,
            R.drawable.undraw_business_deal,
            R.drawable.undraw_solution_mindset,
            R.drawable.undraw_visionary_technology,
            R.drawable.undraw_meditation,
        )

    val dummyPercentages =
        listOf(
            20,
            60,
            90,
            75,
            40,
        )

    val adapter =
        PersonalityCardAdapter(
            context,
            gradients,
            personalityData,
            illustrations,
            dummyPercentages,
        )
    viewPager2.adapter = adapter

    viewPager2.registerOnPageChangeCallback(
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicator(position, indicatorLayout)
            }
        },
    )
}

fun updateIndicator(
    currentPosition: Int,
    indicatorLayout: LinearLayout,
) {
    for (i in 0 until indicatorLayout.childCount) {
        val view = indicatorLayout.getChildAt(i)
        if (i == currentPosition) {
            view.setBackgroundResource(R.drawable.indicator_active)
        } else {
            view.setBackgroundResource(R.drawable.indicator_inactive)
        }
    }
}
