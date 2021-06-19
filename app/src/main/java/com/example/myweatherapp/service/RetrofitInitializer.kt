package com.example.myweatherapp.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {
    private val retrofit =
        Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun repoService() = retrofit.create(Service::class.java)

    //api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=0ca302e28ec58fd5de079352ae3aa7f1
}