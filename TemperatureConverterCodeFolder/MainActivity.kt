package com.example.abrehamgebremariam_temperatureconversion

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
//Abreham Gebremariam
//2.4 Performance Assessment - Temperature Conversion
//02/07/2026
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //add the icon to the Action Bar
        val actionBar = supportActionBar
        actionBar!!.setIcon(R.mipmap.ic_temperature)
        actionBar.setDisplayUseLogoEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        //get our button and add an event handler
        val btnConvert = findViewById<Button>(R.id.btn_convert)
        btnConvert.setOnClickListener {
            val txtTemperature = findViewById<EditText>(R.id.txtTemperature)
            val enteredTemp = txtTemperature.text.toString().toDouble()
            val btnFahToCel = findViewById<RadioButton>(R.id.radFahToCel)
            val txtResult = findViewById<TextView>(R.id.txtResult)

            if (btnFahToCel.isChecked) {
                //Fahrenheit to Celsius is checked, convert entered fahrenheit (°F) to Celsius (°C)
                //Only convert if entered temperature in (°F) is between -100 and 250
                if (enteredTemp >= -100 && enteredTemp <= 250) {
                    //Conversion formula is (°F − 32) * 5/9
                    val tempInCelsius = (enteredTemp - 32) * 5 / 9
                    //display the result, formatted as 1 decimal place
                    val txtTemp = String.format("%.1f", tempInCelsius) + " °C"
                    txtResult.text = "$txtTemp"
                } else {
                    //Show the user a message that the temperature value entered is outside the allowed range
                    Toast.makeText(this, "Fahrenheit must be between -100 and 250",
                        Toast.LENGTH_LONG).show()
                }
            } else {
                //Celsius to Fahrenheit must be checked since it's a radio group with only two buttons
                //Only convert if entered temperature in (°C) is between -75 and 125
                if (enteredTemp >= -75 && enteredTemp <= 125) {
                    //Conversion formula is (°C × 9/5) + 32
                    val tempInFahrenheit = (enteredTemp * 9 / 5) + 32
                    //display the result, formatted as 1 decimal place
                    val txtTemp = String.format("%.1f", tempInFahrenheit) + " °F"
                    txtResult.text = "$txtTemp"
                } else {
                    //Show the user a message that the temperature value entered is outside the allowed range
                    Toast.makeText(this, "Celsius must be between -75 and 125",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
