package com.sayid.studypath.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.sayid.studypath.data.Result
import com.sayid.studypath.data.remote.response.LoginData
import com.sayid.studypath.databinding.ActivityLoginBinding
import com.sayid.studypath.utils.showToast
import com.sayid.studypath.utils.startActivityNoAnimation
import com.sayid.studypath.viewmodel.LoginViewModel
import com.sayid.studypath.viewmodel.factory.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private var newLogin = true
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this)
    }

    private val loginViewModel: LoginViewModel by viewModels {
        factory
    }

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                if (task.isSuccessful) {
                    task.result?.idToken?.let { loginViewModel.signInWithGoogle(it) }
                } else {
                    Log.e(TAG, "googleSignInLauncher: Sign-in failed - ${task.exception?.message}")
                }
            } else {
                Log.e(TAG, "googleSignInLauncher: Sign-in failed - ${task.exception?.message}")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeIdToken()
        observeUserResponse()
    }

    private fun observeUserResponse() {
        loginViewModel.userResponse.observe(this@LoginActivity) { result ->
            binding.apply {
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            loggingIn(true)
                        }

                        is Result.Success -> {
                            val response = result.data.data
                            navigateToNextScreen(response)
                            loggingIn(false)
                        }

                        is Result.Error -> {
                            loggingIn(false)
                            playAnimation()
                            setListener()
                            observeAuthResult()
                            Log.d(TAG, result.error)
                            showToast(this@LoginActivity, result.error)
                        }
                    }
                }
            }
        }
    }

    private fun observeIdToken() {
        loginViewModel.idToken.observe(this@LoginActivity) { idToken ->
            loggingIn(true)
            when (idToken) {
                null -> {
                    loggingIn(false)
                    playAnimation()
                    setListener()
                    observeAuthResult()
                }

                else -> {
                    newLogin = false
                    loggingIn(true)
                }
            }
        }
    }

    private fun playAnimation() {
        val containerAlpha =
            ObjectAnimator.ofFloat(binding.scrollView2, View.ALPHA, 0f, 1f).setDuration(1000)
        val containerMove =
            ObjectAnimator
                .ofFloat(binding.scrollView2, View.TRANSLATION_Y, 100f, 0f)
                .setDuration(1000)
        AnimatorSet().apply {
            playTogether(containerAlpha, containerMove)
            start()
        }
    }

    private fun setListener() {
        binding.btnLogin.setOnClickListener {
            val signInIntent = loginViewModel.getGoogleSignInIntent()
            googleSignInLauncher.launch(signInIntent)
        }
    }

    private fun navigateToNextScreen(loginData: LoginData) {
        val hasTakenQuiz = loginData.isAnswerQuiz
        val hasRegistered = loginData.isRegister

        val intentMain = Intent(this, MainActivity::class.java)
        val intentQuizConfirmation = Intent(this, QuizConfirmationActivity::class.java)
        val intentNewUserData = Intent(this, NewUserDataActivity::class.java)

        Log.e(TAG, hasTakenQuiz.toString())
        Log.e(TAG, hasRegistered.toString())

        intentNewUserData.putExtra(
            NewUserDataActivity.USER_NAME,
            loginData.result.name,
        )

        Log.e(TAG, loginData.result.name)

        if (hasRegistered) {
            if (newLogin) {
                startActivity(intentQuizConfirmation)
            } else {
                if (hasTakenQuiz) {
                    startActivityNoAnimation(intentMain)
                } else {
                    startActivityNoAnimation(intentQuizConfirmation)
                }
            }
        } else {
            if (newLogin) {
                startActivity(intentNewUserData)
            } else {
                startActivityNoAnimation(
                    intentNewUserData,
                )
            }
        }
        finish()
    }

    private fun observeAuthResult() {
        loginViewModel.authResult.observe(this) { result ->
            loggingIn(true)
            Log.d(TAG, "AuthResult: $result")
            when (result) {
                null -> {
                    Log.d(TAG, "Is Loading")
                    loggingIn(true)
                }

                else -> {
                    result
                        .onSuccess {
                            newLogin = true
                            loginViewModel.login()
                            loggingIn(true)
                        }.onFailure {
                            showToast(this@LoginActivity, "Network Error")
                            loggingIn(false)
                        }
                }
            }
        }
    }

    private fun loggingIn(active: Boolean) {
        binding.apply {
            loading.visibility = if (active) View.VISIBLE else View.GONE
            btnLogin.isClickable = !active
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}
