package com.example.myweatherapp.model

import androidx.room.*

@Dao
interface FavouriteCityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: FavouriteCity)

    @Delete
    suspend fun delete(city: FavouriteCity)

    @Query("SELECT * FROM favouritesCities")
    suspend fun getAllFavouriteCities(): List<FavouriteCity>

    @Query("DELETE FROM favouritesCities")
    suspend fun deleteAll()
}