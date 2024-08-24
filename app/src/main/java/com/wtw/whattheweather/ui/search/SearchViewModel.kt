package com.wtw.whattheweather.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    val searchKeyword = MutableLiveData("")
    val searchWeatherInfo = MutableLiveData("")
}