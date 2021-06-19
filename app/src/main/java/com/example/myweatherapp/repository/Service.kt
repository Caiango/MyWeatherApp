package com.example.myweatherapp.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("find?APPID=0ca302e28ec58fd5de079352ae3aa7f1")
    fun getWeather(@Query("q") city: String?): Call<City>
    //fun getWeather(@Query("q") city: String?, @Query("w") param2: String?): Call<City>
}