package com.example.travelwishlistdb

import android.util.Log
import com.example.travelwishlistdb.service.AuthorizationHeaderInterceptor
import com.example.travelwishlistdb.service.PlaceService
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlaceRepository {
    private val BASE_URL = "https://claraj.pythonanywhere.com/api/"

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthorizationHeaderInterceptor())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val placeService = retrofit.create(PlaceService::class.java)

    suspend fun getAllPlaces(): List<Place> {
        try {
            val response = placeService.getAllPlaces()

            if (response.isSuccessful) {  // connected, received data
                val places = response.body() ?: listOf()
            } else {  // connected to server but received an error
                Log.e(TAG, "Error fetching places from API server ${response.errorBody()}")
                return listOf()
            }
        } catch (ex: Exception) {  // can't connect, network error
            Log.e(TAG, "Error connecting to API server", ex)
            return listOf()
        }

        // compiler throws a hissy fit if this isn't here
        return listOf()
    }

    suspend fun addPlace(place: Place): Place? {
        try {
            val response = placeService.addPlace(place)
            if (response.isSuccessful) {
                Log.d(TAG, "Created new place $place")
                return response.body()
            } else {
                Log.e(TAG, "Error creating new place ${response.errorBody()}")
                return null
            }
        } catch (ex: Exception) {  // can't connect, network error
            Log.e(TAG, "Error connecting to API server", ex)
            return null
        }
    }
}