package com.example.locationtracker.viewmodel

import android.location.Location
import androidx.lifecycle.*
import com.example.locationtracker.data.LocationDao
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

class TrackerViewModel(private val locationDao: LocationDao): ViewModel() {

    fun allLocations(): Flow<List<com.example.locationtracker.data.Location>> =
        locationDao.getItems()
    val allLocations: LiveData<List<com.example.locationtracker.data.Location>> = locationDao.getItems().asLiveData()

    private val _currentLocation: MutableLiveData<Location> = MutableLiveData<Location>()
    val currentLocation: MutableLiveData<Location>
        get() = _currentLocation

    fun addNewLocation(locationLangLat: String) {
        val newLocation = getNewLocationEntry(locationLangLat)
        insertLocation(newLocation)
    }

    private fun insertLocation(location: com.example.locationtracker.data.Location) {
        viewModelScope.launch {
            locationDao.insert(location)
        }
    }

    fun isEntryValid(locationName: String): Boolean {
        if (locationName.isBlank()) {
            return false
        }
        return true
    }

    private fun getNewLocationEntry(
        locationLangLat: String
    ): com.example.locationtracker.data.Location {
        return com.example.locationtracker.data.Location(
            locationLangLatLng = locationLangLat
        )
    }

    fun delete(){
        viewModelScope.launch {
            locationDao.delete()
        }
    }
}

class TrackerViewModelFactory(private val locationDao: LocationDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrackerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TrackerViewModel(locationDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}