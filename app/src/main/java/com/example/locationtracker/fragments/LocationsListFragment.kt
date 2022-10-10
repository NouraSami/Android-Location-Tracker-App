package com.example.locationtracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.locationtracker.R
import com.example.locationtracker.TrackerApplication
import com.example.locationtracker.adapter.LocationsAdapter
import com.example.locationtracker.databinding.LocationsListFragmentBinding
import com.example.locationtracker.viewmodel.TrackerViewModel
import com.example.locationtracker.viewmodel.TrackerViewModelFactory
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class LocationsListFragment : Fragment() {
    private var binding: LocationsListFragmentBinding? = null

    private val viewModel: TrackerViewModel by activityViewModels{
        TrackerViewModelFactory(
            (activity?.application as TrackerApplication).database.locationDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LocationsListFragmentBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = LocationsAdapter{
        }

        binding?.let {
            it.recyclerView.adapter = adapter
        }

        viewModel.allLocations.observe(this.viewLifecycleOwner){ location ->
            location.let {
                adapter.submitList(it)
            }
        }

        binding?.let {
            it.recyclerView.layoutManager = LinearLayoutManager(this.context)
        }
//        lifecycle.coroutineScope.launch{
//            viewModel.allLocations().collect(){
//                LocationsAdapter.
//            }
//        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}