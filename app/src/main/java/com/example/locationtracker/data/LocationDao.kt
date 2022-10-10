package com.example.locationtracker.data

import android.content.ClipData
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * from location ORDER BY id DESC")
    fun getItems(): Flow<List<Location>>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(location: Location)

    @Query("DELETE FROM Location")
    fun delete()
}