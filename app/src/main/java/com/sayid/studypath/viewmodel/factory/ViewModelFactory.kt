package com.sayid.studypath.viewmodel.factory

import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.sayid.studypath.R
import com.sayid.studypath.data.repository.AuthRepository
import com.sayid.studypath.viewmodel.LoginViewModel

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository?,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return authRepository?.let { LoginViewModel(it) } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    AuthRepository(
                        FirebaseAuth.getInstance(),
                        GoogleSignIn.getClient(
                            context,
                            GoogleSignInOptions
                                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(getString(context, R.string.default_web_client_id))
                                .requestEmail()
                                .build(),
                        ),
                    ),
                )
            }.also { instance = it }
    }
}
