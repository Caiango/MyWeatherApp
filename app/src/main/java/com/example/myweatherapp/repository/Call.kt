package com.example.myweatherapp.repository

import android.content.Context
import android.util.Log
import android.view.View
import com.example.myweatherapp.WeatherAdapter
import com.example.myweatherapp.data.Resp
import retrofit2.Callback
import retrofit2.Response

class Call {
    companion object{
        private fun call(cidade: String, context: Context) {

//            val call = RetrofitInitializer().repoService().getWeather(cidade)
//
//            call.enqueue(object : Callback<Resp> {
//                override fun onResponse(call: retrofit2.Call<Resp>, resp: Response<Resp>) {
//                    resp?.body()?.let {
//                        val reponse: Resp = it
//                        adapter = WeatherAdapter(reponse.list, context)
//                        recycler.adapter = adapter
//                        progressbar.visibility = View.INVISIBLE
//                    }
//                }
//
//                override fun onFailure(call: retrofit2.Call<Resp>, t: Throwable) {
//                    Log.d("erro", t.toString())
//                    progressbar.visibility = View.INVISIBLE
//                }
//
//            })
        }
    }
}