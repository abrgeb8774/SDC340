package com.example.foodexpiryandlowstocktracker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
// Abreham Gebremariam
// FoodExpiryandLowStockTracker
// 02/22/2026
// This screen allows the user to record the consumption of a food item,
// updating its quantity in the inventory.

class ItemsConsumedScreen : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    // This is the main function that runs when the screen is created.
    // It sets up the user interface and initializes all the necessary components.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_items_consumed_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize SharedPreferences to store and retrieve food items.
        sharedPreferences = getSharedPreferences("food_items", Context.MODE_PRIVATE)

        // Get references to all the input fields and buttons from the layout.
        val itemNameInput = findViewById<EditText>(R.id.item_name_input)
        val categorySpinner = findViewById<Spinner>(R.id.category_spinner)
        val quantityInput = findViewById<EditText>(R.id.quantity_input)
        val saveButton = findViewById<Button>(R.id.btnSave)
        val cancelButton = findViewById<Button>(R.id.btnCancel)

        // Create a custom adapter for the category spinner to disable the first item, which acts as a hint.
        val categories = resources.getStringArray(R.array.strarr_categories)
        val adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                if (position == 0) {
                    view.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                }
                return view
            }
        }
        categorySpinner.adapter = adapter

        // Set up the click listener for the 'Save' button to validate and save the consumed item.
        saveButton.setOnClickListener {
            val itemName = itemNameInput.text.toString()
            val category = categorySpinner.selectedItem.toString()
            val quantityUsed = quantityInput.text.toString()

            // Validate all fields before proceeding. If any are empty, show a toast and exit.
            if (itemName.isEmpty() || categorySpinner.selectedItemPosition == 0 || quantityUsed.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields and select a category", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // If the item was successfully updated, clear the input fields.
            if (updateFoodItemQuantity(itemName, category, quantityUsed.toInt())) {
                clearFields(itemNameInput, quantityInput, categorySpinner)
            }
        }

        // Set up the click listener for the 'Cancel' button to clear all input fields.
        cancelButton.setOnClickListener {
            clearFields(itemNameInput, quantityInput, categorySpinner)
        }

        // Set up the home button to navigate back to the main screen.
        val homeButton = findViewById<ImageButton>(R.id.imageButton2)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }
    }

    // This function updates the quantity of a food item in SharedPreferences.
    // It returns true if the update was successful, and false otherwise.
    private fun updateFoodItemQuantity(itemName: String, category: String, quantityUsed: Int): Boolean {
        val json = sharedPreferences.getString("food_items_list", "[]")
        val type = object : TypeToken<MutableList<FoodItem>>() {}.type
        val foodItems: MutableList<FoodItem> = gson.fromJson(json, type)

        val itemToUpdate = foodItems.find { it.itemName.equals(itemName, ignoreCase = true) && it.category == category }

        // If the item is not found, show a toast and exit.
        if (itemToUpdate == null) {
            Toast.makeText(this, "Item not found in inventory.", Toast.LENGTH_SHORT).show()
            return false
        }

        // If the user tries to consume more than the available quantity, show a toast and exit.
        if (quantityUsed > itemToUpdate.quantity) {
            Toast.makeText(this, "Cannot consume more than the available quantity: ${itemToUpdate.quantity}", Toast.LENGTH_LONG).show()
            return false
        }

        // Decrease the quantity of the item and save the updated list to SharedPreferences.
        itemToUpdate.quantity -= quantityUsed
        if (itemToUpdate.quantity <= 0) {
            itemToUpdate.quantity = 0
            Toast.makeText(this, "Item is now out of stock.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Quantity updated.", Toast.LENGTH_SHORT).show()
        }

        val newJson = gson.toJson(foodItems)
        sharedPreferences.edit().putString("food_items_list", newJson).apply()
        return true
    }

    // This function clears all the input fields and resets the spinner to its default state.
    private fun clearFields(itemNameInput: EditText, quantityInput: EditText, categorySpinner: Spinner) {
        itemNameInput.text.clear()
        quantityInput.text.clear()
        categorySpinner.setSelection(0)
    }
}