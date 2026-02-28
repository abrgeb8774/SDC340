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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit
// Abreham Gebremariam
// FoodExpiryandLowStockTracker
// 02/22/2026
// This screen displays a list of food items that are expiring soon,
// based on the user's notification settings.

class ItemsExpiringScreen : AppCompatActivity() {

    private lateinit var expiringItemsAdapter: ExpiringItemsAdapter
    private var allFoodItems: List<FoodItem> = listOf()

    // This is the main function that runs when the screen is created.
    // It sets up the user interface and initializes all the necessary components.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_items_expiring_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load the list of food items and the user's notification settings.
        allFoodItems = loadFoodItems()
        val notificationDays = loadNotificationDays()

        // Filter the list to get only the items that are expiring soon.
        val expiringItems = filterExpiringItems(allFoodItems, notificationDays)

        // Set up the RecyclerView to display the list of expiring items.
        val recyclerView = findViewById<RecyclerView>(R.id.expiring_items_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        expiringItemsAdapter = ExpiringItemsAdapter(expiringItems)
        recyclerView.adapter = expiringItemsAdapter

        // Set up the home button to navigate back to the main screen.
        val homeButton = findViewById<ImageButton>(R.id.imageButton4)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }
    }

    // This function loads the list of food items from SharedPreferences.
    // It filters out any items with a quantity of zero so they don't appear in this list.
    private fun loadFoodItems(): List<FoodItem> {
        val sharedPreferences = getSharedPreferences("food_items", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("food_items_list", "[]")
        val type = object : TypeToken<List<FoodItem>>() {}.type
        val allItems: List<FoodItem> = Gson().fromJson(json, type)
        return allItems.filter { it.quantity > 0 }
    }

    // This function loads the number of days for the expiry notification from the settings.
    private fun loadNotificationDays(): Int {
        val sharedPreferences = getSharedPreferences(settingScreen.PREFS_NAME, Context.MODE_PRIVATE)
        // Default to 7 days if not set
        return sharedPreferences.getInt(settingScreen.NOTIFICATION_DAYS, 7)
    }

    // This function filters the list of food items to find only the ones that are expiring soon.
    private fun filterExpiringItems(items: List<FoodItem>, notificationDays: Int): List<FoodItem> {
        val todayCal = Calendar.getInstance()
        todayCal.set(Calendar.HOUR_OF_DAY, 0)
        todayCal.set(Calendar.MINUTE, 0)
        todayCal.set(Calendar.SECOND, 0)
        todayCal.set(Calendar.MILLISECOND, 0)
        val today = todayCal.time

        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)

        return items.filter { item ->
            val expiryDate = try {
                dateFormat.parse(item.expiryDate)
            } catch (e: Exception) {
                null
            }
            
            if (expiryDate != null) {
                val diffInMillis = expiryDate.time - today.time
                val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)
                diffInDays <= notificationDays
            } else {
                false
            }
        }
    }
}