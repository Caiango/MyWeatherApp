package com.example.myweatherapp.db

import androidx.room.*

@Dao
interface FavouriteCityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: FavouriteCity)

    @Update
    fun update(city: FavouriteCity)

    @Delete
    fun delete(city: FavouriteCity)

    @Query("SELECT * FROM favouritesCities")
    suspend fun getAllFavouriteCities(): List<FavouriteCity>

    @Query("DELETE FROM favouritesCities")
    fun deleteAll()
}