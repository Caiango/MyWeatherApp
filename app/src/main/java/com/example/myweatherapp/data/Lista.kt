package com.example.myweatherapp.data

data class Lista(val id: Int, val name: String, val main: Main, val weather: List<Weather>)

data class Main(val temp: String)

data class Weather(val icon: String)