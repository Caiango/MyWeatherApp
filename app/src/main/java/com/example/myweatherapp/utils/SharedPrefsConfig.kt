package com.example.myweatherapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.myweatherapp.R

class SharedPrefsConfig(context: Context) {
    private val mContext = context

    fun getFromSharedPrefs(): String {
        val sharedPref: SharedPreferences =
            mContext.getSharedPreferences(
                mContext.getString(R.string.shared_settings),
                Context.MODE_PRIVATE
            )
        return sharedPref.getString("temp", "")!!

    }
}