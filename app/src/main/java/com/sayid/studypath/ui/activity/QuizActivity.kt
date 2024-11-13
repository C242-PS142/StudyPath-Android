package com.sayid.studypath.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.sayid.studypath.R
import com.sayid.studypath.databinding.ActivityQuizBinding
import com.sayid.studypath.ui.adapter.QuizAnswerAdapter
import kotlin.math.abs

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager: ViewPager2 = binding.quizAnswerViewpager

        // Daftar item untuk ViewPager
        val items = listOf(
            listOf("Agree", R.color.md_theme_primary),
            listOf("Neutral", R.color.md_theme_tertiary),
            listOf("Disagree", R.color.md_theme_errorContainer_mediumContrast),
        )

        // Set adapter ke ViewPager2
        val adapter = QuizAnswerAdapter(items)
        viewPager.adapter = adapter

        viewPager.setPageTransformer { page, position ->
            val pageTranslationX = 40 * position
            page.translationX = -pageTranslationX
            // Optional: Set scale for a centered effect
            page.scaleY = 1 - (0.15f * abs(position))
        }

        viewPager.setOffscreenPageLimit(1);
        val middleItemIndex = items.size / 2
        viewPager.setCurrentItem(middleItemIndex, false)
    }
}