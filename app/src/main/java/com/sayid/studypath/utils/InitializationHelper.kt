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
    val personalityTitle =
        listOf(
            R.string.openness,
            R.string.agreeableness,
            R.string.neuroticism,
            R.string.conscientiousness,
            R.string.extroversion,
        )

    val illustrations =
        listOf(
            R.drawable.icon_keterbukaan,
            R.drawable.icon_kesepakatan,
            R.drawable.icon_kestabilan,
            R.drawable.icon_ketelitian,
            R.drawable.icon_sosial,
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
            personalityTitle,
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
