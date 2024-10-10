package com.example.travelwishlist

import android.util.Log
import androidx.lifecycle.ViewModel

const val TAG="PLACES_VIEW_MODEL"

class PlacesViewModel : ViewModel() {
    private val places = mutableListOf<Place>(Place("Rio de Janeiro", "Very cool"), Place("Queen Maud Land", "VERY cool"), Place("Lake Baikal", "wow"))

    fun getPlaces(): List<Place> {
        return places  // Kotlin will convert mutable list to list
    }

    fun addNewPlace(place: Place, position: Int? = null): Int {
        /** If a place name is in the list, return -1 (case-insensitive). */
        if (places.any { it.name.uppercase() == place.name.uppercase() }) {
            // already in the list, therefore:
            return -1
        }

        return if (position != null) {
            // position is cast to Int and won't be null, Kotlin does
            // a "smart cast" from Int? to Int due to null check
            // if-statement null check is appropriate for parameters
            // which are vals but can be nullable
            places.add(position, place)
            position
        } else {
            // position remains an Int? and will be null
            places.add(place)
            places.lastIndex
        }
    }

    fun movePlace(from: Int, to: Int) {
        // remove place and save value
        val place = places.removeAt(from)
        Log.d(TAG, "Removed ${place.name} at ${from.toString()} from places")
        // insert into list at new position
        places.add(to, place)
        Log.d(TAG, "Inserted ${place.name} at ${to.toString()}")
    }

    fun deletePlace(position: Int): Place {
        // remove a Place at the given position, then return that Place
        return places.removeAt(position)
    }
}