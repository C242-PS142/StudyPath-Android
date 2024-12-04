package com.sayid.studypath.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sayid.studypath.data.remote.response.Prediction

object PredictionResultSingleton {
    private val _listPrediction = MutableLiveData<Prediction?>()
    val listPrediction: LiveData<Prediction?> = _listPrediction

    fun updatePrediction(prediction: Prediction) {
        _listPrediction.postValue(prediction)
    }

    fun clearPrediction() {
        _listPrediction.postValue(null)
    }
}
