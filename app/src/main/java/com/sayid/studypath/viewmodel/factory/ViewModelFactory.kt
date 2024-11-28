package com.sayid.studypath.viewmodel.factory

import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.sayid.studypath.R
import com.sayid.studypath.data.model.LocalPreferences
import com.sayid.studypath.data.model.dataStore
import com.sayid.studypath.data.repository.AuthRepository
import com.sayid.studypath.data.repository.QuizRepository
import com.sayid.studypath.viewmodel.LoginViewModel
import com.sayid.studypath.viewmodel.MainViewModel
import com.sayid.studypath.viewmodel.NewUserDataViewModel
import com.sayid.studypath.viewmodel.OnboardingViewModel
import com.sayid.studypath.viewmodel.QuizActivityViewModel
import com.sayid.studypath.viewmodel.SettingsViewModel

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository,
    private val quizRepository: QuizRepository,
    private val localPreferences: LocalPreferences,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(NewUserDataViewModel::class.java)) {
            return NewUserDataViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(authRepository, localPreferences) as T
        } else if (modelClass.isAssignableFrom(OnboardingViewModel::class.java)) {
            return OnboardingViewModel(localPreferences) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(localPreferences) as T
        }
        if(modelClass.isAssignableFrom(QuizActivityViewModel::class.java)){
            return QuizActivityViewModel(quizRepository) as T
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
                                .requestIdToken(context.getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build(),
                        ),
                    ),
                    QuizRepository(),
                    LocalPreferences(context.dataStore),
                )
            }.also { instance = it }
    }
}