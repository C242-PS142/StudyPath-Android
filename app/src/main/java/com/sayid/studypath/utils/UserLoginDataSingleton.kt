package com.sayid.studypath.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sayid.studypath.data.Result
import com.sayid.studypath.data.remote.response.UserLoginData

object UserLoginDataSingleton {
    private val _userLoginData = MutableLiveData<Result<UserLoginData>?>()
    val userLoginData: LiveData<Result<UserLoginData>?> = _userLoginData

    fun updateLoginData(prediction: Result<UserLoginData>) {
        _userLoginData.postValue(prediction)
    }

    fun clearLoginData() {
        _userLoginData.postValue(null)
    }
}
