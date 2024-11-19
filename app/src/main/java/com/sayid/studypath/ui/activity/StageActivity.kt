package com.sayid.studypath.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sayid.studypath.R
import com.sayid.studypath.databinding.ActivityStageBinding

class StageActivity : AppCompatActivity() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: ActivityStageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            val currentProgress = 5
            val progressText = getString(R.string.quiz_progress, currentProgress)
            tvStageHeader.text = progressText

            val quizName = "KETERBUKAAN TERHADAP PENGALAMAN"
            val message = getString(R.string.celebrate_progress_quiz, quizName)
            tvStageDescription.text = message

            btnNextStage.setOnClickListener {
                val intent = Intent(this@StageActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
