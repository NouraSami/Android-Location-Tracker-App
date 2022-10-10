package com.example.locationtracker.adapter

import android.annotation.SuppressLint
import android.content.ClipData
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.locationtracker.data.Location
import com.example.locationtracker.databinding.FragmentLocationBinding
import java.text.SimpleDateFormat

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * */
class LocationsAdapter(
    private val onItemClicked: (Location) -> Unit
): ListAdapter<Location, LocationsAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object: DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem.locationLangLatLng == newItem.locationLangLatLng
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(
            FragmentLocationBinding.inflate(
                LayoutInflater.from( parent.context),
                parent,
                false
            )
        )
        return viewHolder

    }

    class ViewHolder(
        private var binding: FragmentLocationBinding
        ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(location: Location) {
            binding.apply {
                id.text = location.id.toString()
                address.text= location.locationLangLatLng
                //timeVisited.text= location.timeVisited.time.toString()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }
}