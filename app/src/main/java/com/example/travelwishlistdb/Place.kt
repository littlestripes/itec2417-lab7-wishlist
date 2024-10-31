package com.example.travelwishlistdb

import java.util.Date

data class Place(val name: String, val reason: String? = null, var starred: Boolean = false, val id: Int? = null, val dateAdded: Date = Date()) {
}