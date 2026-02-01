package com.example.abreham_gebremariam_contactsaddressbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button

//Abreham Gebremariam
//01/02/2026
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val alexanderButton = findViewById<Button> (R.id.alexanderButton)
        alexanderButton.setOnClickListener {
            val intent = Intent(this, ContactOneActivity::class.java)
            startActivity(intent)
        }
        val adrianButton = findViewById<Button> (R.id.adrianButton)
        adrianButton.setOnClickListener {
            val intent = Intent(this, ContactTwoActivity::class.java)
            startActivity(intent)
        }
    }
}
