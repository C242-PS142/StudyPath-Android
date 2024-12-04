package com.sayid.studypath.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import com.sayid.studypath.R
import com.sayid.studypath.data.Result
import com.sayid.studypath.databinding.ActivityQuizConfirmationBinding
import com.sayid.studypath.utils.PredictionResultSingleton
import com.sayid.studypath.utils.initializePersonalityCard
import com.sayid.studypath.viewmodel.QuizConfirmationViewModel
import com.sayid.studypath.viewmodel.factory.ViewModelFactory

class QuizConfirmationActivity : AppCompatActivity() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: ActivityQuizConfirmationBinding? = null
    private val binding get() = _binding!!

    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this)
    }

    private val quizConfirmationViewModel: QuizConfirmationViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityQuizConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }

        val navIcon =
            AppCompatResources.getDrawable(this, R.drawable.baseline_arrow_back_24)
        supportActionBar?.setHomeAsUpIndicator(navIcon)

        quizConfirmationViewModel.userPredictionResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    loggingIn(true)
                }

                is Result.Success -> {
                    loggingIn(false)
                    val prediction = result.data.data.personality

                    if (prediction.ketelitian == 0.0f &&
                        prediction.kesepakatan == 0.0f &&
                        prediction.kestabilanEmosi == 0.0f &&
                        prediction.keterbukaanTerhadapPengalaman == 0.0f &&
                        prediction.keterbukaanSosialEnergiDanAntusiasme == 0.0f
                    ) {
                        enableHasNoDataView()
                    } else {
                        PredictionResultSingleton.updatePrediction(prediction)
                        initializePersonalityCard(
                            this,
                            binding.viewPager,
                            binding.indicatorLayout,
                            prediction,
                        )
                        enableHasDataView()
                    }
                }

                is Result.Error -> {
                    loggingIn(false)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                quizConfirmationViewModel.signOut()
                startActivity(
                    Intent(this@QuizConfirmationActivity, LoginActivity::class.java),
                )
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    private fun loggingIn(active: Boolean) {
        binding.apply {
            btnConfirmationQuiz.isClickable = !active
            btnRefreshData.isClickable = !active
        }
    }

    private fun enableHasDataView() {
        binding.apply {
            tvConfirmationQuiz.visibility = View.VISIBLE
            btnConfirmationQuiz.visibility = View.VISIBLE
            viewPager.visibility = View.VISIBLE
            indicatorLayout.visibility = View.VISIBLE
            btnRefreshData.visibility = View.VISIBLE

            tvConfirmationQuiz.text = getString(R.string.has_data)
            btnConfirmationQuiz.text = getString(R.string.load_data)

            btnConfirmationQuiz.setOnClickListener {
                val intent = Intent(this@QuizConfirmationActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            btnRefreshData.setOnClickListener {
                val intent = Intent(this@QuizConfirmationActivity, QuizActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun enableHasNoDataView() {
        binding.apply {
            tvConfirmationQuiz.visibility = View.VISIBLE
            btnConfirmationQuiz.visibility = View.VISIBLE

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
