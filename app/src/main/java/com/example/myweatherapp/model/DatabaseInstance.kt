package com.example.myweatherapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavouriteCity::class], version = 1)
abstract class DatabaseInstance : RoomDatabase() {

    abstract val weatherDao: FavouriteCityDao

    companion object {
        @Volatile
        var INSTANCE: DatabaseInstance? = null

        fun getInstance(context: Context): DatabaseInstance? {
            synchronized(this) {
                val instance = INSTANCE

                if (instance == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseInstance::class.java, "myDB"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }
    }
}
