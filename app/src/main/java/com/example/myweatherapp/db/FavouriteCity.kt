package com.example.myweatherapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favouritesCities")
data class FavouriteCity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val temp: String,
    val img: String
)