package com.example.travelwishlist

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), OnListItemClickedListener, OnDataChangedListener {

    private lateinit var newPlaceEditText: EditText
    private lateinit var addNewPlaceButton: Button
    private lateinit var placeListRecyclerView: RecyclerView

    private lateinit var placesRecyclerAdapter: PlaceRecyclerAdapter

    private val placesListModel: PlacesViewModel by lazy {
        ViewModelProvider(this).get(PlacesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        placeListRecyclerView = findViewById(R.id.place_list)
        newPlaceEditText = findViewById(R.id.new_place_name)
        addNewPlaceButton = findViewById(R.id.add_new_place_button)

        val places = placesListModel.getPlaces()

        // configure RecyclerView
        placesRecyclerAdapter = PlaceRecyclerAdapter(places, this)
        placeListRecyclerView.layoutManager = LinearLayoutManager(this)
        placeListRecyclerView.adapter = placesRecyclerAdapter

        // create new ItemTouchHelper, pass MainActivity as listener and attach to recycler
        ItemTouchHelper(OnListItemSwipeListener(this))
            .attachToRecyclerView(placeListRecyclerView)

        addNewPlaceButton.setOnClickListener {
            addNewPlace()
        }
    }

    private fun addNewPlace() {
        val placeName = newPlaceEditText.text.toString()
        val name = placeName.trim()
        if (name.isEmpty()) {
            Toast.makeText(this, "Enter a place name", Toast.LENGTH_SHORT).show()
        } else {
            val place = Place(name)
            val positionAdded = placesListModel.addNewPlace(place)
            if (positionAdded == -1) {
                Toast.makeText(this, "You already added that place", Toast.LENGTH_SHORT).show()
            } else {
                placesRecyclerAdapter.notifyItemInserted(positionAdded)
                clearForm()
                hideKeyboard()
            }
        }
    }

    private fun clearForm() {
        newPlaceEditText.text.clear()
    }

    private fun hideKeyboard() {
        // close on-screen keyboard
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onListItemClicked(place: Place) {
        val placeLocationUri = Uri.parse("geo:0,0?q=${place.name}")
        val mapIntent = Intent(Intent.ACTION_VIEW, placeLocationUri)
        startActivity(mapIntent)
    }

    override fun onListItemMoved(from: Int, to: Int) {
        placesListModel.movePlace(from, to)
        placesRecyclerAdapter.notifyItemMoved(from, to)
    }

    override fun onListItemDeleted(position: Int) {
        /** Delete the place at position.
         * Display a Snackbar with an undo option and restore the
         * place if the undo action is tapped. */

        val place = placesListModel.deletePlace(position)
        placesRecyclerAdapter.notifyItemRemoved(position)

        Snackbar.make(findViewById(R.id.container), getString(R.string.place_deleted, place.name), Snackbar.LENGTH_LONG)
            .setActionTextColor(resources.getColor(R.color.red))
            .setBackgroundTint(resources.getColor(R.color.black))
            .setAction(getString(R.string.undo_delete)) {
                placesListModel.addNewPlace(place, position)
                placesRecyclerAdapter.notifyItemInserted(position)
            }
            .show()

        // note on getColor
        // newer Android versions require a theme arg so the correct color
        // can be fetched for the theme (dark/light) used
    }
}