package com.sayid.studypath.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.sayid.studypath.R
import com.sayid.studypath.data.remote.response.QuizItem
import com.sayid.studypath.databinding.ActivityQuizBinding
import com.sayid.studypath.viewmodel.QuizActivityViewModel

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private val quizActivityViewModel: QuizActivityViewModel by viewModels<QuizActivityViewModel>()
    private var currentStage: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get List Quiz
        currentStage = intent.getIntExtra(STAGE, 1)

        when(currentStage){
            1 -> {
                quizActivityViewModel.quizStage1.observe(this){ showQuiz(it) }
            }
            2 -> {
                quizActivityViewModel.quizStage2.observe(this){ showQuiz(it) }
            }
            3 -> {
                quizActivityViewModel.quizStage3.observe(this){ showQuiz(it) }
            }
            4 -> {
                quizActivityViewModel.quizStage4.observe(this){ showQuiz(it) }
            }
            5 -> {
                quizActivityViewModel.quizStage5.observe(this){ showQuiz(it) }
            }
        }
    }

    private fun showQuiz(quizs: List<QuizItem>) {
        var currentProgress = 1

        fun updateQuiz() {
            if (currentProgress <= quizs.size) {
                binding.apply {
                    val progressQuiz = getString(R.string.quiz_question, currentProgress)
                    tvTestHeader.text = progressQuiz
                    tvTestQuestion.text = quizs[currentProgress - 1].questionText
                }
            } else {
                // Logika jika kuis selesai, misalnya navigasi ke hasil
                nextStage(currentStage)
            }
        }

        binding.apply {
            // Fungsi untuk berpindah ke pertanyaan berikutnya
            val onAnswerSelected = {
                currentProgress++
                updateQuiz()
            }

            // Atur semua tombol menggunakan fungsi listener yang sama
            btnAgree.setOnClickListener { onAnswerSelected() }
            btnDisagree.setOnClickListener { onAnswerSelected() }
            btnVeryAgree.setOnClickListener { onAnswerSelected() }
            btnVeryDisagree.setOnClickListener { onAnswerSelected() }
            btnNeutral.setOnClickListener { onAnswerSelected() }
        }

        // Tampilkan pertanyaan pertama
        updateQuiz()
    }

    private fun nextStage(stage: Int) {
        val intent = Intent(this@QuizActivity, StageActivity::class.java)
        intent.putExtra(StageActivity.CURRENTSTAGE, stage)
        startActivity(intent)
        finish()
    }

    companion object{
        var STAGE = "stage"
    }
}
