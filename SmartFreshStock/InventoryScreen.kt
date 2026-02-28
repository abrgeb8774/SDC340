package com.example.foodexpiryandlowstocktracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
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
// This screen displays the user's current inventory and allows them to filter by category.
class InventoryScreen : AppCompatActivity() {

    private lateinit var inventoryAdapter: InventoryAdapter
    private var allFoodItems: List<FoodItem> = listOf()

    // This is the main function that runs when the screen is created.
    // It sets up the user interface and initializes all the necessary components.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inventory_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load the list of food items from device storage.
        allFoodItems = loadFoodItems()

        // Set up the RecyclerView to display the list of inventory items.
        val recyclerView = findViewById<RecyclerView>(R.id.inventory_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        inventoryAdapter = InventoryAdapter(allFoodItems)
        recyclerView.adapter = inventoryAdapter

        // Set up the spinner that allows users to filter the inventory by category.
        val categorySpinner = findViewById<Spinner>(R.id.category_filter_spinner)
        val categories = listOf("All Categories") + resources.getStringArray(R.array.strarr_categories).toList().drop(1)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        categorySpinner.adapter = spinnerAdapter

        // Define the action to take when a user selects a category from the spinner.
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCategory = parent.getItemAtPosition(position).toString()
                filterInventory(selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing if no category is selected.
            }
        }

        // Set up the home button to navigate back to the main screen.
        val homeButton = findViewById<ImageButton>(R.id.imageButton)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }
    }

    // This function loads the list of food items from SharedPreferences.
    // It filters out any items with a quantity of zero so they don't appear in the inventory.
    private fun loadFoodItems(): List<FoodItem> {
        val sharedPreferences = getSharedPreferences("food_items", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("food_items_list", "[]")
        val type = object : TypeToken<List<FoodItem>>() {}.type
        val allItems: List<FoodItem> = Gson().fromJson(json, type)
        return allItems.filter { it.quantity > 0 }
    }

    // This function filters the displayed list of items based on the category the user selects.
    private fun filterInventory(category: String) {
        val filteredList = if (category == "All Categories") {
            allFoodItems
        } else {
            allFoodItems.filter { it.category == category }
        }
        inventoryAdapter.updateData(filteredList.sortedBy { it.category })
    }
}