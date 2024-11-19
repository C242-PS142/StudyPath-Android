package com.sayid.studypath.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sayid.studypath.databinding.ActivityLoginBinding
import com.sayid.studypath.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels()
    private var isNewLogin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hasLogin()
        setListener()
    }

    private fun setListener() {
        binding.btnLogin.setOnClickListener {
            // Simulate Add Access Token
            isNewLogin = true

            val token = "sample_access_token"
            loginViewModel.saveAccessToken(token)

            startActivity(Intent(this, NewUserDataActivity::class.java))
            finish()
        }
    }

    private fun hasLogin() {
        loginViewModel.accessToken.observe(
            this,
        ) { token ->
            if (token != null && !isNewLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
