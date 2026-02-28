package com.example.foodexpiryandlowstocktracker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
// Abreham Gebremariam
// FoodExpiryandLowStockTracker
// 02/22/2026
// This screen displays a list of all food items that have a quantity of zero.

class OutOfStockScreen : AppCompatActivity() {

    private lateinit var outOfStockAdapter: OutOfStockAdapter
    private var allFoodItems: List<FoodItem> = listOf()

    // This is the main function that runs when the screen is created.
    // It sets up the user interface and initializes all the necessary components.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_out_of_stock_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load the list of all food items from device storage.
        allFoodItems = loadFoodItems()

        // Filter the list to get only the items that are out of stock (quantity is zero).
        val outOfStockItems = allFoodItems.filter { it.quantity == 0 }

        // Set up the RecyclerView to display the list of out-of-stock items.
        val recyclerView = findViewById<RecyclerView>(R.id.out_of_stock_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        outOfStockAdapter = OutOfStockAdapter(outOfStockItems)
        recyclerView.adapter = outOfStockAdapter

        // Set up the home button to navigate back to the main screen.
        val homeButton = findViewById<ImageButton>(R.id.imageButton5)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }
    }

    // This function loads the list of food items from SharedPreferences.
    private fun loadFoodItems(): List<FoodItem> {
        val sharedPreferences = getSharedPreferences("food_items", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("food_items_list", "[]")
        val type = object : TypeToken<List<FoodItem>>() {}.type
        return Gson().fromJson(json, type)
    }
}