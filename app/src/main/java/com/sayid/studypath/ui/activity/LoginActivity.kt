package com.sayid.studypath.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.sayid.studypath.databinding.ActivityLoginBinding
import com.sayid.studypath.viewmodel.LoginViewModel
import com.sayid.studypath.viewmodel.factory.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

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

        setListener()
        observeViewModel()
    }

    private fun setListener() {
        binding.btnLogin.setOnClickListener {
            val signInIntent = loginViewModel.getGoogleSignInIntent()
            googleSignInLauncher.launch(signInIntent)
        }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun observeViewModel() {
        loginViewModel.idToken.observe(this) { idToken ->
            when (idToken) {
                null -> {
                    if (loginViewModel.getCurrentUser() != null) {
                        Log.d(TAG, "Force Logout!")
                        loginViewModel.signOut()
                    }
                }

                else -> {
                    navigateToHome()
                    Log.d(TAG, "Token: $idToken")
                }
            }
        }
        loginViewModel.authResult.observe(this) { result ->
            Log.d(TAG, "AuthResult: $result")
            when (result) {
                null -> {
                    Log.d(TAG, "Is Loading")
                }

                else -> {
                    result
                        .onSuccess {
                            Log.d(TAG, "observeViewModel: Login successful")
                            navigateToHome()
                        }.onFailure { exception ->
                            Log.e(
                                TAG,
                                "observeViewModel: Login failed - ${exception.message}",
                                exception,
                            )
                        }
                }
            }
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
