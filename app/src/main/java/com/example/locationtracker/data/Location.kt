package com.example.locationtracker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import java.sql.Time
import java.time.OffsetTime

@Entity
data class Location (
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,
    @ColumnInfo(name = "location")
    val locationLangLatLng: String?,
//    @ColumnInfo(name ="time")
//    val timeVisited:OffsetTime? = null
        )
