package com.sayid.studypath.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.sayid.studypath.R
import com.sayid.studypath.databinding.ActivityQuizConfirmationBinding
import com.sayid.studypath.utils.PredictionResultSingleton
import com.sayid.studypath.utils.initializePersonalityCard
import com.sayid.studypath.viewmodel.QuizActivityViewModel
import com.sayid.studypath.viewmodel.factory.ViewModelFactory

class QuizConfirmationActivity : AppCompatActivity() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: ActivityQuizConfirmationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityQuizConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val hasData = intent.getBooleanExtra(HAS_DATA, true)

        if (hasData) {
            PredictionResultSingleton.listPrediction.observe(this) { prediction ->
                prediction?.let {
                    initializePersonalityCard(
                        this,
                        binding.viewPager,
                        binding.indicatorLayout,
                        it
                    )
                }
            }
            enableHasDataView()
        } else {
            enableHasNoDataView()
        }
    }

    private fun enableHasDataView() {
        binding.apply {
            viewPager.visibility = View.VISIBLE
            indicatorLayout.visibility = View.VISIBLE
            btnRefreshData.visibility = View.VISIBLE

            tvConfirmationQuiz.text = getString(R.string.has_data)
            btnConfirmationQuiz.text = getString(R.string.load_data)

            btnConfirmationQuiz.setOnClickListener {
                val intent = Intent(this@QuizConfirmationActivity, MainActivity::class.java)
                startActivity(intent)
            }

            btnRefreshData.setOnClickListener {
                val intent = Intent(this@QuizConfirmationActivity, QuizActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun enableHasNoDataView() {
        binding.apply {
            tvConfirmationQuiz.text = getString(R.string.has_no_data)
            btnConfirmationQuiz.text = getString(R.string.take_test)

            btnConfirmationQuiz.setOnClickListener {
                val intent = Intent(this@QuizConfirmationActivity, QuizActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    companion object {
        const val HAS_DATA = "has_data"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
