package com.wtw.whattheweather.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private const val baseUrl = "http://3.95.165.254:8080/"

    private fun getRetrofit() : Retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val networkService : NetworkService = getRetrofit().create(NetworkService::class.java)

}