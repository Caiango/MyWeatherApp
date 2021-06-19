package com.example.myweatherapp.repository

import com.example.myweatherapp.Constants
import com.example.myweatherapp.data.Resp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("find?APPID=${Constants.key}")
    //fun getWeather(@Query("q") city: String?): Call<Resp>
    fun getWeather(@Query("q") city: String?, @Query("units") param2: String? = "metric"): Call<Resp>
}