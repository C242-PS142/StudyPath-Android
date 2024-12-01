package com.sayid.studypath.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sayid.studypath.data.model.LocalPreferences

class MainViewModel(
    private val localPreferences: LocalPreferences,
) : ViewModel() {
    val isDarkTheme: LiveData<Boolean?> = localPreferences.isDarkTheme.asLiveData()
}
