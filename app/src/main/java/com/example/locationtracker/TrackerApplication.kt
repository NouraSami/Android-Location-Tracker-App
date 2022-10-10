package com.example.locationtracker

import android.app.Application
import com.example.locationtracker.data.LocationRoomDatabase

class TrackerApplication : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database: LocationRoomDatabase by lazy { LocationRoomDatabase.getDatabase(this) }
}