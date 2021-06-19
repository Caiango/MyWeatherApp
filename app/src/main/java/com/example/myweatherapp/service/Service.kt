package com.example.myweatherapp.service

import com.example.myweatherapp.utils.Constants
import com.example.myweatherapp.data.Resp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("find?")
    fun getWeather(
        @Query("q") city: String?,
        @Query("units") units: String? = "metric",
        @Query("APPID") key: String = Constants.key
    ): Call<Resp>
}