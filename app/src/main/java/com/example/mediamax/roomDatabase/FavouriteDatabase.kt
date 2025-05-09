package com.example.mediamax.roomDatabase

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Favourite::class], version = 1, exportSchema = false)
abstract class FavouriteDatabase : RoomDatabase() {
    abstract fun favouriteDao(): FavouriteDao
    companion object {
        @Volatile
        private var INSTANCE: FavouriteDatabase? = null

        fun getDatabase(context: Context): FavouriteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavouriteDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


