package com.example.myweatherapp.service

import android.content.Context
import android.util.Log
import com.example.myweatherapp.data.Resp
import com.example.myweatherapp.data.RespFromID
import retrofit2.Callback
import retrofit2.Response

class Call {
    companion object {
        fun callByCityName(temp: String, cidade: String, context: Context, callback: (Resp?, Context, Boolean) -> Unit) {
            val call = RetrofitInitializer().repoService().getWeather(cidade, temp)

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

        fun callByCityId(temp: String, id: String, context: Context, callback: (RespFromID?, Context, Boolean) -> Unit) {
            val call = RetrofitInitializer().repoService().getWeatherById(id, units = temp)

            call.enqueue(object : Callback<RespFromID> {
                override fun onResponse(call: retrofit2.Call<RespFromID>, resp: Response<RespFromID>) {
                    resp?.body()?.let {
                        val reponse: RespFromID = it
                        callback(reponse, context, true)
                    }
                }

                override fun onFailure(call: retrofit2.Call<RespFromID>, t: Throwable) {
                    Log.d("erro", t.toString())
                    callback(null, context, false)
                }

            })
        }
    }
}