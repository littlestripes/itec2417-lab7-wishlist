package com.example.travelwishlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaceRecyclerAdapter(private val places: List<Place>,
                           private val onListItemClickedListener: OnListItemClickedListener) :
    RecyclerView.Adapter<PlaceRecyclerAdapter.ViewHolder>() {

    class ViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        fun bind(place: Place, onListItemClickedListener: OnListItemClickedListener) {
            val placeNameText: TextView = view.findViewById(R.id.place_name)
            placeNameText.text = place.name

            val dateCreatedOnText : TextView = view.findViewById(R.id.date_place_added)
            dateCreatedOnText.text = view.context.getString(R.string.created_on, place.formattedDate())

            val mapIcon: ImageView = view.findViewById(R.id.map_icon)
            mapIcon.setOnClickListener {
                onListItemClickedListener.onListItemClicked(place)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = places[position]
        holder.bind(place, onListItemClickedListener)
    }

    override fun getItemCount(): Int {
        return places.size
    }
}