package com.example.myweatherapp.service

import android.content.Context
import android.util.Log
import com.example.myweatherapp.data.Resp
import retrofit2.Callback
import retrofit2.Response

class Call {
    companion object {
        fun call(cidade: String, context: Context, callback: (Resp?, Context, Boolean) -> Unit) {
            val call = RetrofitInitializer().repoService().getWeather(cidade)

            call.enqueue(object : Callback<Resp> {
                override fun onResponse(call: retrofit2.Call<Resp>, resp: Response<Resp>) {
                    resp?.body()?.let {
                        val reponse: Resp = it
                        callback(reponse, context, true)
                    }
                }

                override fun onFailure(call: retrofit2.Call<Resp>, t: Throwable) {
                    Log.d("erro", t.toString())
                    callback(null, context, false)
                }

            })
        }
    }
}