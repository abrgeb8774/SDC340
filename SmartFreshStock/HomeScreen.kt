package com.example.foodexpiryandlowstocktracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
// Abreham Gebremariam
// FoodExpiryandLowStockTracker
// 02/22/2026
// This is the main navigation screen of the application.
class HomeScreen : AppCompatActivity() {

    // This is the main function that runs when the screen is created.
    // It sets up the user interface and initializes all the necessary components.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get references to all the radio buttons from the layout.
        val radAddItem = findViewById<RadioButton>(R.id.radAddItem)
        val radItemsConsumed = findViewById<RadioButton>(R.id.radItemsConsumed)
        val radViewInventory = findViewById<RadioButton>(R.id.radViewInventory)
        val radSetting = findViewById<RadioButton>(R.id.radSetting)
        val radItemExpiry = findViewById<RadioButton>(R.id.radItemExpiry)
        val radOutOfStock = findViewById<RadioButton>(R.id.radOutOfStock)
        val btnNext = findViewById<Button>(R.id.btnNext)

        // Set a click listener for the 'Next' button to navigate to the selected screen.
        btnNext.setOnClickListener {
            // Use a 'when' statement to determine which screen to navigate to based on the selected radio button.
            val intent = when {
                radAddItem.isChecked -> Intent(this, AddItem::class.java)
                radItemsConsumed.isChecked -> Intent(this, ItemsConsumedScreen::class.java)
                radViewInventory.isChecked -> Intent(this, InventoryScreen::class.java)
                radSetting.isChecked -> Intent(this, settingScreen::class.java)
                radItemExpiry.isChecked -> Intent(this, ItemsExpiringScreen::class.java)
                radOutOfStock.isChecked -> Intent(this, OutOfStockScreen::class.java)
                else -> null
            }
            // If an intent was created, start the corresponding activity.
            intent?.let { startActivity(it) }
        }
    }
}