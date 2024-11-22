package com.sayid.studypath.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayid.studypath.data.repository.AuthRepository
import com.sayid.studypath.viewmodel.AuthViewModel

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository?,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return authRepository?.let { AuthViewModel(it) } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(authRepository: AuthRepository?): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(authRepository)
            }.also { instance = it }
    }
}
