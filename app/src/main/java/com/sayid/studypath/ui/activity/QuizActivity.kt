package com.sayid.studypath.ui.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sayid.studypath.R
import com.sayid.studypath.data.remote.response.QuizAnswer
import com.sayid.studypath.data.remote.response.QuizItem
import com.sayid.studypath.databinding.ActivityQuizBinding
import com.sayid.studypath.utils.QuizAnswerSingleton
import com.sayid.studypath.utils.showToast
import com.sayid.studypath.viewmodel.QuizActivityViewModel
import com.sayid.studypath.viewmodel.factory.ViewModelFactory

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private var currentProgress = 1
    private lateinit var soundPool: SoundPool
    private var spLoaded = false
    private var soundId: Int = 0
    private var isAnimating = false
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this)
    }
    private val quizActivityViewModel: QuizActivityViewModel by viewModels {
        factory
    }
    private var currentStage: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.getInt(KEY_CURRENT_PROGRESS)?.let {
            currentProgress = it
        }
        savedInstanceState?.getBoolean(KEY_IS_ANIMATING)?.let {
            isAnimating = it
        }
        playSfx()
        // Get List Quiz
        currentStage = intent.getIntExtra(STAGE, 1)

        when (currentStage) {
            1 -> {
                quizActivityViewModel.quizStage1.observe(this) { showQuiz(it) }
            }

            2 -> {
                quizActivityViewModel.quizStage2.observe(this) { showQuiz(it) }
            }

            3 -> {
                quizActivityViewModel.quizStage3.observe(this) { showQuiz(it) }
            }

            4 -> {
                quizActivityViewModel.quizStage4.observe(this) { showQuiz(it) }
            }

            5 -> {
                quizActivityViewModel.quizStage5.observe(this) { showQuiz(it) }
            }
        }
    }

    private fun playSfx() {
        soundPool =
            SoundPool
                .Builder()
                .setMaxStreams(10)
                .build()
        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) {
                spLoaded = true
            } else {
                showToast(this, "Gagal Load")
            }
        }
        soundId = soundPool.load(this, R.raw.test_answer, 1)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_PROGRESS, currentProgress)
        outState.putBoolean(KEY_IS_ANIMATING, isAnimating)
    }

    private fun showQuiz(quizs: List<QuizItem>) {
        var currentQuizCode = ""

        fun updateQuiz() {
            if (currentProgress <= quizs.size) {
                binding.apply {
                    val progressQuiz = getString(R.string.quiz_question, currentProgress)
                    tvTestHeader.text = progressQuiz
                    tvTestQuestion.text = quizs[currentProgress - 1].questionTextID
                    currentQuizCode = quizs[currentProgress - 1].questionCode
                }
            } else {
                // Logika jika kuis selesai, misalnya navigasi ke hasil
                nextStage(currentStage)
            }
        }
// Fungsi untuk berpindah ke pertanyaan berikutnya
        val onAnswerSelected = {
            currentProgress++
            updateQuiz()
        }

        fun onActionUp(
            view: View,
            event: MotionEvent,
            buttonEnum: ButtonEnum,
            answerValue: Int,
        ) {
            val rect = android.graphics.Rect()
            view.getGlobalVisibleRect(rect)
            val isWithinBounds = rect.contains(event.rawX.toInt(), event.rawY.toInt())
            if (!isAnimating) {
                soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
                isAnimating = true
                animateButtons(buttonEnum) {
                    if (isWithinBounds) {
                        onAnswerSelected()
                        QuizAnswerSingleton.addQuizAnswer(
                            QuizAnswer(
                                currentQuizCode,
                                answerValue,
                            ),
                        )
                        animateButtons(buttonEnum, isRecover = true) {
                            isAnimating = false
                        }
                    }
                }
            }
        }

        binding.apply {
            btnVeryAgree.setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        onActionUp(view, event, ButtonEnum.VAGG, 5)
                        view.performClick()
                    }
                }
                true
            }
            btnAgree.setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        onActionUp(view, event, ButtonEnum.AGG, 4)
                        view.performClick()
                    }
                }
                true
            }
            btnNeutral.setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        onActionUp(view, event, ButtonEnum.NEUT, 3)
                        view.performClick()
                    }
                }
                true
            }
            btnDisagree.setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        onActionUp(view, event, ButtonEnum.DIS, 2)
                        view.performClick()
                    }
                }
                true
            }
            btnVeryDisagree.setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        onActionUp(view, event, ButtonEnum.VDIS, 1)
                        view.performClick()
                    }
                }
                true
            }
        }

        // Tampilkan pertanyaan pertama
        updateQuiz()
    }

    private fun animateButtons(
        selectedButton: ButtonEnum,
        isRecover: Boolean = false,
        onComplete: () -> Unit = {},
    ) {
        val animations = mutableListOf<ObjectAnimator>()
        val alpha = if (isRecover) 1f else 0f
        val duration: Long = if (isRecover) 500 else 250

        binding.apply {
            if (selectedButton != ButtonEnum.VDIS) {
                animations.add(
                    ObjectAnimator
                        .ofFloat(btnVeryDisagree, View.ALPHA, alpha)
                        .setDuration(duration),
                )
            } else {
                animations.add(
                    ObjectAnimator
                        .ofFloat(btnVeryDisagree, View.ALPHA, alpha)
                        .setDuration(if (isRecover) duration else duration * 3),
                )
            }
            if (selectedButton != ButtonEnum.DIS) {
                animations.add(
                    ObjectAnimator.ofFloat(btnDisagree, View.ALPHA, alpha).setDuration(duration),
                )
            } else {
                animations.add(
                    ObjectAnimator
                        .ofFloat(btnDisagree, View.ALPHA, alpha)
                        .setDuration(if (isRecover) duration else duration * 3),
                )
            }
            if (selectedButton != ButtonEnum.NEUT) {
                animations.add(
                    ObjectAnimator.ofFloat(btnNeutral, View.ALPHA, alpha).setDuration(duration),
                )
            } else {
                animations.add(
                    ObjectAnimator
                        .ofFloat(btnNeutral, View.ALPHA, alpha)
                        .setDuration(if (isRecover) duration else duration * 3),
                )
            }
            if (selectedButton != ButtonEnum.AGG) {
                animations.add(
                    ObjectAnimator.ofFloat(btnAgree, View.ALPHA, alpha).setDuration(duration),
                )
            } else {
                animations.add(
                    ObjectAnimator
                        .ofFloat(btnAgree, View.ALPHA, alpha)
                        .setDuration(if (isRecover) duration else duration * 3),
                )
            }
            if (selectedButton != ButtonEnum.VAGG) {
                animations.add(
                    ObjectAnimator.ofFloat(btnVeryAgree, View.ALPHA, alpha).setDuration(duration),
                )
            } else {
                animations.add(
                    ObjectAnimator
                        .ofFloat(btnVeryAgree, View.ALPHA, alpha)
                        .setDuration(if (isRecover) duration else duration * 3),
                )
            }
            animations.add(
                ObjectAnimator.ofFloat(tvTestHeader, View.ALPHA, alpha).setDuration(duration),
            )
            animations.add(
                ObjectAnimator.ofFloat(tvTestQuestion, View.ALPHA, alpha).setDuration(duration),
            )
        }

        AnimatorSet().apply {
            playTogether(animations as Collection<Animator>)

            addListener(
                object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}

                    override fun onAnimationEnd(animation: Animator) {
                        onComplete()
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        isAnimating = false
                    }

                    override fun onAnimationRepeat(animation: Animator) {}
                },
            )
            start()
        }
    }

    private fun nextStage(stage: Int) {
        val intent = Intent(this@QuizActivity, StageActivity::class.java)
        intent.putExtra(StageActivity.CURRENTSTAGE, stage)
        startActivity(intent)
        finish()
    }

    enum class ButtonEnum {
        // Button Enumeration
        VDIS,
        DIS,
        NEUT,
        AGG,
        VAGG,
    }

    companion object {
        const val STAGE = "stage"
        private const val KEY_CURRENT_PROGRESS = "current_progress"
        private const val KEY_IS_ANIMATING = "is_animating"
    }
}
