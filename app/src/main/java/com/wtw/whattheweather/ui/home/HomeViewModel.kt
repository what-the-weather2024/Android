package com.wtw.whattheweather.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    val feedDetailWeatherStatus = MutableLiveData("")
    val feedDetailCreatedAt = MutableLiveData("")
    val feedDetailAddress = MutableLiveData("")

}