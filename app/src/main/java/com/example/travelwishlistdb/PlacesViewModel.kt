package com.example.travelwishlistdb

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TAG="PLACES_VIEW_MODEL"

class PlacesViewModel : ViewModel() {
    // testing data
    // TODO: replace with API data
    private val placeNames = mutableListOf<Place>(Place("Rio de Janeiro", "Very cool", starred = true), Place("Queen Maud Land", "VERY cool", starred = true), Place("Lake Baikal", "wow"))

    private val placeRepository = PlaceRepository()

    val allPlaces = MutableLiveData(listOf<Place>())

    init {
        getPlaces()
    }

    fun getPlaces() {
        // return placeNames
        viewModelScope.launch {
            val places = placeRepository.getAllPlaces()
            allPlaces.postValue(places)
        }
    }

    fun addNewPlace(place: Place) {
        viewModelScope.launch {
            val newPlace = placeRepository.addPlace(place)
            getPlaces()
        }
    }

    fun deletePlace(place: Place) {
        // todo
    }

    fun updatePlace(place: Place) {
        // todo
    }
}