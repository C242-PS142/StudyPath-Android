package com.sayid.studypath.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sayid.studypath.R
import com.sayid.studypath.data.remote.response.QuizAnswerRequest
import com.sayid.studypath.databinding.ActivityStageBinding
import com.sayid.studypath.viewmodel.QuizActivityViewModel

class StageActivity : AppCompatActivity() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: ActivityStageBinding? = null
    private val binding get() = _binding!!
    private val quizActivityViewModel: QuizActivityViewModel by viewModels<QuizActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            var stage = intent.getIntExtra(CURRENTSTAGE, 1)
            val progressText = getString(R.string.quiz_progress, stage)
            tvStageHeader.text = progressText

            val quizName = "KETERBUKAAN TERHADAP PENGALAMAN"
            val message = getString(R.string.celebrate_progress_quiz, quizName)
            tvStageDescription.text = message

            btnNextStage.setOnClickListener {
                stage += 1
                if (stage <= 5){
                    val intent = Intent(this@StageActivity, QuizActivity::class.java)
                    intent.putExtra(QuizActivity.STAGE, stage)
                    startActivity(intent)
                }else{
//                    quizActivityViewModel.postQuizAnswers(QuizAnswerRequest(quizActivityViewModel.listQuizAnswer))
                    val intent = Intent(this@StageActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                finish()
            }
        }
    }

    companion object{
        var CURRENTSTAGE = "current_stage"
    }
}
