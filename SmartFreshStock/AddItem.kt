
package com.example.foodexpiryandlowstocktracker

import android.app.DatePickerDialog
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
// Abreham Gebremariam
// FoodExpiryandLowStockTracker
// 02/22/2026
// This screen allows the user to add a new food item to the inventory
// or update the quantity of an existing item.
class AddItem : AppCompatActivity() {

    private lateinit var purchaseDateInput: EditText
    private lateinit var expiryDateInput: EditText
    private val calendar = Calendar.getInstance()
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    // This is the main function that runs when the screen is created.
    // It sets up the user interface and initializes all the necessary components.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_item)
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
        purchaseDateInput = findViewById(R.id.purchase_date_input)
        expiryDateInput = findViewById(R.id.expiry_date_input)
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

        // Set up click listeners for the date fields to show a date picker dialog.
        purchaseDateInput.setOnClickListener { showDatePickerDialog(purchaseDateInput) }
        expiryDateInput.setOnClickListener { showDatePickerDialog(expiryDateInput) }

        // Set up the click listener for the 'Save' button to validate and save the food item.
        saveButton.setOnClickListener {
            val itemName = itemNameInput.text.toString()
            val category = categorySpinner.selectedItem.toString()
            val quantity = quantityInput.text.toString()
            val purchaseDate = purchaseDateInput.text.toString()
            val expiryDate = expiryDateInput.text.toString()

            // Validate all fields before proceeding. If any are empty, show a toast and exit.
            if (itemName.isEmpty() || categorySpinner.selectedItemPosition == 0 || quantity.isEmpty() || purchaseDate.isEmpty() || expiryDate.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields and select a category", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a new FoodItem and save it to storage, then clear the input fields.
            val foodItem = FoodItem(itemName, category, quantity.toInt(), purchaseDate, expiryDate)
            saveFoodItem(foodItem)
            clearFields(itemNameInput, quantityInput, purchaseDateInput, expiryDateInput, categorySpinner)
        }

        // Set up the click listener for the 'Cancel' button to clear all input fields.
        cancelButton.setOnClickListener {
            clearFields(itemNameInput, quantityInput, purchaseDateInput, expiryDateInput, categorySpinner)
        }

        // Set up the home button to navigate back to the main screen.
        val homeButton = findViewById<ImageButton>(R.id.imageButton2)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }
    }

    // This function displays a date picker dialog and updates the provided EditText with the selected date.
    private fun showDatePickerDialog(dateInput: EditText) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView(dateInput)
        }

        DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // This function formats the selected date and sets it as the text of the provided EditText.
    private fun updateDateInView(dateInput: EditText) {
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        dateInput.setText(sdf.format(calendar.time))
    }

    // This function saves a food item to SharedPreferences. If the item already exists, it updates the quantity.
    private fun saveFoodItem(foodItem: FoodItem) {
        val json = sharedPreferences.getString("food_items_list", "[]")
        val type = object : TypeToken<MutableList<FoodItem>>() {}.type
        val foodItems: MutableList<FoodItem> = gson.fromJson(json, type)

        val existingItem = foodItems.find { it.itemName.equals(foodItem.itemName, ignoreCase = true) && it.category == foodItem.category }
        if (existingItem != null) {
            existingItem.quantity += foodItem.quantity
            Toast.makeText(this, "Item quantity updated!", Toast.LENGTH_SHORT).show()
        } else {
            foodItems.add(foodItem)
            Toast.makeText(this, "New item saved!", Toast.LENGTH_SHORT).show()
        }

        val newJson = gson.toJson(foodItems)
        sharedPreferences.edit().putString("food_items_list", newJson).apply()
    }

    // This function clears all the input fields and resets the spinner to its default state.
    private fun clearFields(itemNameInput: EditText, quantityInput: EditText, purchaseDateInput: EditText, expiryDateInput: EditText, categorySpinner: Spinner) {
        itemNameInput.text.clear()
        quantityInput.text.clear()
        purchaseDateInput.text.clear()
        expiryDateInput.text.clear()
        categorySpinner.setSelection(0)
    }
}