package com.example.travelwishlist

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Place(val name: String, val reason: String, val dateAdded: Date = Date()) {
    fun formattedDate(): String {
        /** Return date as a formatted string.
         *  Example: "Wed, 4 July 2001" */
        return SimpleDateFormat("EEE, d MMMM yyyy", Locale.getDefault()).format(dateAdded)
    }
}