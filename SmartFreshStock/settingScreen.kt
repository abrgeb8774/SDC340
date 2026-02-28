package com.example.foodexpiryandlowstocktracker

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
// Abreham Gebremariam
// FoodExpiryandLowStockTracker
// 02/22/2026
// This screen allows the user to configure settings for expiry notifications.
class settingScreen : AppCompatActivity() {

    private lateinit var expiryAlertSwitch: Switch
    private lateinit var daysEditText: EditText
    private lateinit var notificationTimeTextView: TextView
    private lateinit var saveButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private val calendar = Calendar.getInstance()

    // These are constant values used as keys for storing settings in SharedPreferences.
    companion object {
        const val PREFS_NAME = "settings_prefs"
        const val EXPIRY_ALERT_ENABLED = "expiry_alert_enabled"
        const val NOTIFICATION_DAYS = "notification_days"
        const val NOTIFICATION_TIME_HOUR = "notification_time_hour"
        const val NOTIFICATION_TIME_MINUTE = "notification_time_minute"
    }

    // This is the main function that runs when the screen is created.
    // It sets up the user interface and initializes all the necessary components.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_setting_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize SharedPreferences to store and retrieve settings.
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Get references to all the input fields and buttons from the layout.
        expiryAlertSwitch = findViewById(R.id.btnOnOff)
        daysEditText = findViewById(R.id.editTextNumber)
        notificationTimeTextView = findViewById(R.id.notification_time_text_view)
        saveButton = findViewById(R.id.btnSave1)

        // Load any saved settings from SharedPreferences.
        loadSettings()

        // Set up a click listener for the notification time to show a time picker dialog.
        notificationTimeTextView.setOnClickListener {
            showTimePickerDialog()
        }

        // Set up the click listener for the 'Save' button to save the current settings.
        saveButton.setOnClickListener {
            saveSettings()
        }

        // Set up the home button to navigate back to the main screen.
        val homeButton = findViewById<ImageButton>(R.id.imageButton3)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }
    }

    // This function loads the user's saved settings from SharedPreferences and updates the UI.
    private fun loadSettings() {
        val alertEnabled = sharedPreferences.getBoolean(EXPIRY_ALERT_ENABLED, false)
        val notificationDays = sharedPreferences.getInt(NOTIFICATION_DAYS, 7)
        val hour = sharedPreferences.getInt(NOTIFICATION_TIME_HOUR, 10)
        val minute = sharedPreferences.getInt(NOTIFICATION_TIME_MINUTE, 0)

        expiryAlertSwitch.isChecked = alertEnabled
        daysEditText.setText(notificationDays.toString())
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        updateNotificationTimeView()
    }

    // This function saves the user's current settings to SharedPreferences.
    private fun saveSettings() {
        val alertEnabled = expiryAlertSwitch.isChecked
        val notificationDays = daysEditText.text.toString().toIntOrNull() ?: 7

        sharedPreferences.edit().apply {
            putBoolean(EXPIRY_ALERT_ENABLED, alertEnabled)
            putInt(NOTIFICATION_DAYS, notificationDays)
            putInt(NOTIFICATION_TIME_HOUR, calendar.get(Calendar.HOUR_OF_DAY))
            putInt(NOTIFICATION_TIME_MINUTE, calendar.get(Calendar.MINUTE))
            apply()
        }

        Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show()
    }

    // This function shows a time picker dialog to allow the user to select a notification time.
    private fun showTimePickerDialog() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            updateNotificationTimeView()
        }

        TimePickerDialog(
            this,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        ).show()
    }

    // This function updates the notification time TextView to display the selected time.
    private fun updateNotificationTimeView() {
        val myFormat = "h:mm a"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        notificationTimeTextView.text = sdf.format(calendar.time)
    }
}