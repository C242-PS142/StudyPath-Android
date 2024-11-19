package com.sayid.studypath.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sayid.studypath.R
import com.sayid.studypath.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            val currentProgress = 10
            val progressText = getString(R.string.quiz_question, currentProgress)
            tvTestHeader.text = progressText
            tvTestQuestion.text =
                "KAMU MERASA LEBIH BERSEMANGAT DENGAN IDE-IDE BARU DAN KOMPLEKS DIBANDINGKAN DENGAN HAL-HAL YANG SEDERHANA DAN JELAS"

            btnAgree.setOnClickListener {
                nextStage()
            }
            btnDisagree.setOnClickListener {
                nextStage()
            }
            btnVeryAgree.setOnClickListener {
                nextStage()
            }
            btnVeryDisagree.setOnClickListener {
                nextStage()
            }
            btnNeutral.setOnClickListener {
                nextStage()
            }
        }
    }

    private fun nextStage() {
        startActivity(Intent(this@QuizActivity, StageActivity::class.java))
        finish()
    }
}
