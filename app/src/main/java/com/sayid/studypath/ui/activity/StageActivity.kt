package com.sayid.studypath.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sayid.studypath.R
import com.sayid.studypath.data.Result
import com.sayid.studypath.data.remote.response.QuizAnswerRequest
import com.sayid.studypath.databinding.ActivityStageBinding
import com.sayid.studypath.utils.QuizAnswerSingleton
import com.sayid.studypath.utils.showToast
import com.sayid.studypath.viewmodel.QuizActivityViewModel
import com.sayid.studypath.viewmodel.factory.ViewModelFactory

class StageActivity : AppCompatActivity() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: ActivityStageBinding? = null
    private val binding get() = _binding!!
    private var mediaPlayer: MediaPlayer? = null
    private var isPlayed = false
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this)
    }
    private val quizActivityViewModel: QuizActivityViewModel by viewModels<QuizActivityViewModel> { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var stage = intent.getIntExtra(CURRENTSTAGE, 1)
        val progressText = getString(R.string.quiz_progress, stage)

        savedInstanceState?.getBoolean(KEY_IS_PLAYED)?.let {
            isPlayed = it
        }
        if (!isPlayed) {
            isPlayed = true
            playSound(if (stage == 5) R.raw.finish_all_stage else R.raw.finish_stage)
        } else {
            playAnimation()
        }

        quizActivityViewModel.listQuizAnswerResponse.observe(this@StageActivity) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        isLoading(true)
                    }

                    is Result.Success -> {
                        isLoading(false)
                        val intent =
                            Intent(this@StageActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    is Result.Error -> {
                        isLoading(false)
                        showToast(this, "Masalah: ${result.error}, Silahkan coba lagi!")
                        Log.d(TAG, result.error)
                    }
                }
            }
        }

        binding.apply {
            tvStageHeader.text = progressText
            val quizName: String =
                when (stage) {
                    1 -> getString(R.string.extroversion)
                    2 -> getString(R.string.neuroticism)
                    3 -> getString(R.string.agreeableness)
                    4 -> getString(R.string.conscientiousness)
                    else -> getString(R.string.openness)
                }
            val message = getString(R.string.celebrate_progress_quiz, quizName)
            tvStageDescription.text = message

            btnNextStage.setOnClickListener {
                stage += 1
                if (stage <= 5) {
                    val intent = Intent(this@StageActivity, QuizActivity::class.java)
                    intent.putExtra(QuizActivity.STAGE, stage)
                    startActivity(intent)
                    finish()
                } else {
                    QuizAnswerSingleton.listQuizAnswer.observe(this@StageActivity) { answer ->
                        quizActivityViewModel.postQuizAnswers(
                            QuizAnswerRequest(answer),
                        )
                    }
                    btnNextStage.text = "Lihat Hasil Tes!"
                }
            }
        }
    }

    private fun playSound(resourceId: Int) {
        mediaPlayer?.release()

        mediaPlayer =
            MediaPlayer.create(this, resourceId).apply {
                setOnPreparedListener {
                    start()
                    playAnimation()
                }

                setOnCompletionListener {
                    release()
                    mediaPlayer = null
                }

                setOnErrorListener { _, _, _ ->
                    mediaPlayer?.release()
                    mediaPlayer = null
                    false
                }
            }
    }

    private fun playAnimation() {
        val main =
            ObjectAnimator.ofFloat(binding.scrollView, View.ALPHA, 1f).apply {
                duration = 500
            }
        val scaleX =
            ObjectAnimator.ofFloat(binding.scrollView, View.SCALE_X, 1f).apply {
                duration = 500
            }
        val scaleY =
            ObjectAnimator.ofFloat(binding.scrollView, View.SCALE_Y, 1f).apply {
                duration = 500
            }
        val bgAlphaTop =
            ObjectAnimator.ofFloat(binding.ivBgTop, View.ALPHA, 0f, 0.25f).apply {
                duration = 800
            }
        val bgTopMove =
            ObjectAnimator.ofFloat(binding.ivBgTop, View.TRANSLATION_Y, -50f, 0f).apply {
                duration = 1250
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }
        val bgAlphaBottom =
            ObjectAnimator.ofFloat(binding.ivBgBottom, View.ALPHA, 0f, 0.25f).apply {
                duration = 800
            }
        val bgBottomMove =
            ObjectAnimator.ofFloat(binding.ivBgBottom, View.TRANSLATION_Y, 50f, 0f).apply {
                duration = 1250
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }

        AnimatorSet().apply {
            playTogether(main, scaleX, scaleY, bgAlphaTop, bgAlphaBottom)
            start()

            addListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        binding.btnNextStage.isEnabled = true
                    }
                },
            )
        }

        AnimatorSet().apply {
            playTogether(bgTopMove, bgBottomMove)
            start()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_IS_PLAYED, isPlayed)
    }

    private fun isLoading(active: Boolean) {
        binding.btnNextStage.isEnabled = !active
        binding.loading.visibility = if (active) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "StageActivity"
        const val CURRENTSTAGE = "current_stage"
        private const val KEY_IS_PLAYED = "is_played"
    }
}
