package com.example.myweatherapp.data

data class Resp(val list: List<Lista>)

data class RespFromID(val weather: List<Weather>, val id: String, val name: String, val main: Main)
