package com.wtw.whattheweather.ui.upload

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UploadViewModel : ViewModel() {

    val selectedLocation = MutableLiveData("")

    val uploadResultText = MutableLiveData("")
    val uploadWeatherStatus = MutableLiveData("")
    val uploadAddress = MutableLiveData("")
    val uploadWeatherProb = MutableLiveData("")

}