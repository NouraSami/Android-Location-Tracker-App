package com.example.locationtracker.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.locationtracker.R
import com.example.locationtracker.TrackerApplication
import com.example.locationtracker.databinding.MapFragmentBinding
import com.example.locationtracker.viewmodel.TrackerViewModel
import com.example.locationtracker.viewmodel.TrackerViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapFragment: Fragment(),OnMapReadyCallback, GoogleMap.OnMarkerClickListener,LocationListener {


    private lateinit var mLocationRequest: com.google.android.gms.location.LocationRequest
    private lateinit var mMap: GoogleMap
    private var lastLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var binding: MapFragmentBinding? = null
    lateinit var location: com.example.locationtracker.data.Location
    private lateinit var list:List<Address>
    private val TAG:String = ""
    private lateinit var mLocationCallback: LocationCallback





    private val viewModel: TrackerViewModel by activityViewModels{
        TrackerViewModelFactory(
            (activity?.application as TrackerApplication).database.locationDao()
        )
    }



    companion object{
        private const val LOCATION_REQUEST_CODE = 1 }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //initialize view
        val view:View = inflater.inflate(R.layout.map_fragment,container,false)
        //initialize map fragment
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as? SupportMapFragment
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        //Async map
        binding = MapFragmentBinding.bind(view)
        mapFragment?.getMapAsync({ googleMap ->
            onMapReady(googleMap)
        })

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                for (location in p0.locations){
                    if (lastLocation != null && lastLocation != location)
                    {
                        setUpLocation(location)
                        addLocation()
                    }
                }
            }
        }

        mLocationRequest = com.google.android.gms.location.LocationRequest.create()
        mLocationRequest.interval = 40000
        mLocationRequest.fastestInterval = 20000
        mLocationRequest.priority = LocationRequest.QUALITY_HIGH_ACCURACY

        binding?.apply {
            toLocations.setOnClickListener() {
                findNavController().navigate(R.id.action_mapFragment_to_locationsListFragment)
            }
        }

        return view
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        setUpMap(requireContext())
    }

    private fun setUpMap(context: Context){

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)  {

            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),LOCATION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            setUpLocation(location)
        }
    }

    fun setUpLocation(location: Location){
        lastLocation = location
        val currentLatLng = LatLng(location.latitude, location.longitude)
        placeMarkerOnMap(currentLatLng)

    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        Log.d(TAG,"Location updates started")
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
            mLocationCallback,
            Looper.getMainLooper())
    }


    private fun placeMarkerOnMap(currentLatLng: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLng)
        markerOptions.title("$currentLatLng")
        mMap.addMarker(markerOptions)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
    }

    private fun addLocation(){
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        lastLocation?.let { list = geocoder.getFromLocation(it.latitude, it.longitude, 1)
            Log.d(TAG,"Address List is ${list[0]}")
            viewModel.addNewLocation(
                list.get(0).getAddressLine(0).toString(),

            )}
    }

    override fun onMarkerClick(p0: Marker) = false
    override fun onLocationChanged(location: Location) {
//        if (lastLocation != location)
//        {
//            setUpLocation(location)
//            addLocation()
//        }

    }

}