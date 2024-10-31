package com.example.travelwishlistdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaceRecyclerAdapter(var places: List<Place>,
                           private val onListItemClickedListener: OnListItemClickedListener) :
    RecyclerView.Adapter<PlaceRecyclerAdapter.ViewHolder>() {

    class ViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        fun bind(place: Place, onListItemClickedListener: OnListItemClickedListener) {
            val placeNameText: TextView = view.findViewById(R.id.place_name)
            placeNameText.text = place.name

            val placeReasonText: TextView = view.findViewById(R.id.place_reason)
            placeReasonText.text = place.reason

            val mapIcon: ImageView = view.findViewById(R.id.map_icon)
            mapIcon.setOnClickListener {
                onListItemClickedListener.onMapRequestButtonClicked(place)
            }

            view.findViewById<CheckBox>(R.id.star_check).apply {
                // remove listener if set
                setOnCheckedChangeListener(null)
                // set state
                isChecked = place.starred
                // then re-add listener (otherwise setting isChecked calls the listener, then recycler
                // view updates the item which sets the state etc. etc.
                setOnCheckedChangeListener { checkbox, isChecked ->
                    onListItemClickedListener.onStarredStatusChanged(place, isChecked)
                }
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