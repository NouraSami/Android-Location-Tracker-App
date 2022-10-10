package com.example.locationtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.locationtracker.databinding.ActivityMainBinding
import com.example.locationtracker.fragments.LocationsListFragment
import com.example.locationtracker.fragments.MapFragment
import com.example.locationtracker.viewmodel.TrackerViewModel
import com.example.locationtracker.viewmodel.TrackerViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private val viewModel: TrackerViewModel by viewModels(){
        TrackerViewModelFactory(
            (this.application as TrackerApplication).database.locationDao()
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.delete()

//        val mapFragment:Fragment = MapFragment()
//
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.frame_layout,mapFragment)
//            .commit()


        navController = findNavController(R.id.nav_host)
        NavigationUI.setupActionBarWithNavController(this, navController)

//        val navHostFragment = supportFragmentManager
//            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        navController = navHostFragment.navController
//        setupActionBarWithNavController(navController)

    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}