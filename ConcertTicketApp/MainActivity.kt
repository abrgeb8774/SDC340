package com.example.abreham_gebremariam_tickethub
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
//Abreham Gebremariam
//Concert Ticket Mobile App
//02/13/2026
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //add the icon to the Action Bar
        val actionBar = supportActionBar
        actionBar!!.setIcon(R.mipmap.ic_launcher_foreground)
        actionBar.setDisplayUseLogoEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        //get the UI controls with the values
        val events = findViewById<Spinner>(R.id.txtEvents)
        val tickets = findViewById<EditText>(R.id.numTickets)
        val txtResult = findViewById<TextView>(R.id.txtResult)

        //get the button to calculate the total cost
        val btnCalculate = findViewById<Button>(R.id.btn_calculate)
        btnCalculate.setOnClickListener {
            val eventsSel = events.selectedItem.toString()
            val ticketsNum = tickets.text.toString().toInt()

            //Convert the event choice from the string array to a value
            val choiceArr = resources.getStringArray(R.array.strarr_events)
            var eventPrice = 0.0
            //eventPrice 0 = Pop Music Festival, 1 = Jazz Music Festival, 2 = Country Music Festival
            if (eventsSel == choiceArr[0]) {
                eventPrice = 49.99
            } else if (eventsSel == choiceArr[1]) {
                eventPrice = 45.99
            } else if (eventsSel == choiceArr[2]) {
                eventPrice = 42.99
            }
            //Calculating the total cost of the tickets
            val totalCost = eventPrice * ticketsNum
            val txtTotal = String.format("%.2f", totalCost)
            //Displaying the total cost of the tickets for the user with dollar sign together
            txtResult.text = "$$txtTotal"
        }
    }
}