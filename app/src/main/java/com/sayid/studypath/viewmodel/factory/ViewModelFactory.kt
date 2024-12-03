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
import com.sayid.studypath.viewmodel.EditProfileViewModel
import com.sayid.studypath.viewmodel.LoginViewModel
import com.sayid.studypath.viewmodel.MainViewModel
import com.sayid.studypath.viewmodel.NewUserDataViewModel
import com.sayid.studypath.viewmodel.OnboardingViewModel
import com.sayid.studypath.viewmodel.QuizActivityViewModel
import com.sayid.studypath.viewmodel.QuizConfirmationViewModel
import com.sayid.studypath.viewmodel.SettingsViewModel

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository,
    private val localPreferences: LocalPreferences,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authRepository) as T
            }

            modelClass.isAssignableFrom(NewUserDataViewModel::class.java) -> {
                NewUserDataViewModel(authRepository) as T
            }

            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(authRepository, localPreferences) as T
            }

            modelClass.isAssignableFrom(OnboardingViewModel::class.java) -> {
                OnboardingViewModel(localPreferences) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(localPreferences, authRepository) as T
            }

            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(authRepository) as T
            }

            modelClass.isAssignableFrom(QuizActivityViewModel::class.java) -> {
                QuizActivityViewModel(authRepository) as T
            }

            modelClass.isAssignableFrom(QuizConfirmationViewModel::class.java) -> {
                QuizConfirmationViewModel(authRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
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
                    LocalPreferences(context.dataStore),
                )
            }.also { instance = it }
    }
}
