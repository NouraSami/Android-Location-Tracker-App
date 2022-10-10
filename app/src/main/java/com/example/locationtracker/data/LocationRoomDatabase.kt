package com.example.locationtracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Location::class], version = 1, exportSchema = false)
abstract class LocationRoomDatabase: RoomDatabase() {

    abstract fun locationDao(): LocationDao

    companion object{
        @Volatile
        private var INSTANCE: LocationRoomDatabase? = null

        fun getDatabase(context: Context): LocationRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationRoomDatabase::class.java,
                    "location_database"
                )   .allowMainThreadQueries()
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}