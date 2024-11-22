package com.sayid.studypath.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.sayid.studypath.R
import com.sayid.studypath.data.repository.AuthRepository
import com.sayid.studypath.databinding.ActivityLoginBinding
import com.sayid.studypath.viewmodel.AuthViewModel
import com.sayid.studypath.viewmodel.factory.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "LoginActivity"
    }

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(
            AuthRepository(
                FirebaseAuth.getInstance(),
                GoogleSignIn.getClient(
                    this,
                    GoogleSignInOptions
                        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build(),
                ),
            ),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (authViewModel.isLoggedIn()) {
            Log.d(TAG, "onCreate: User is already logged in")
            navigateToHome()
        } else {
            Log.d(TAG, "onCreate: User is not logged in")
        }

        setListener()
        observeViewModel()
    }

    private fun navigateToHome() {
        Log.d(TAG, "navigateToHome: Navigating to MainActivity")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun setListener() {
        binding.btnLogin.setOnClickListener {
            Log.d(TAG, "setListener: Login button clicked")
            val signInIntent = authViewModel.getGoogleSignInIntent()
            googleSignInLauncher.launch(signInIntent)
        }
    }

    private fun observeViewModel() {
        authViewModel.authResult.observe(this) { result ->
            Log.d(TAG, "observeViewModel: Observing authResult")
            result
                .onSuccess {
                    Log.d(TAG, "observeViewModel: Login successful")
                    navigateToHome()
                }.onFailure { exception ->
                    Log.e(TAG, "observeViewModel: Login failed - ${exception.message}", exception)
                }
        }
    }

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d(TAG, "googleSignInLauncher: Received sign-in result")
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                if (task.isSuccessful) {
                    val idToken = task.result?.idToken
                    if (idToken != null) {
                        Log.d(TAG, "googleSignInLauncher: ID token received")
                        Log.d(TAG, idToken)
                        authViewModel.signInWithGoogle(idToken)
                    } else {
                        Log.e(TAG, "googleSignInLauncher: ID token is null")
                    }
                } else {
                    Log.e(TAG, "googleSignInLauncher: Sign-in failed - ${task.exception?.message}")
                }
            } else {
                Log.e(TAG, "googleSignInLauncher: Sign-in failed - ${task.exception?.message}")
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
