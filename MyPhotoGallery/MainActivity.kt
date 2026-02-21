package com.example.abrehamgebremariam_photogallery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.GridView
//Abreham Gebremariam
//My Photo Gallery
//02/20/2026

//Main screen that displays a grid of images.
class MainActivity : AppCompatActivity() {
    lateinit var imgGrid: GridView
    lateinit var imagesList: List<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup action bar with logo.
        supportActionBar?.let {
            it.setIcon(R.mipmap.ic_launcher_foreground)
            it.setDisplayUseLogoEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        // Initialize the grid and populate it with images.
        imgGrid = findViewById(R.id.imagesGrid)
        imagesList = listOf(
            R.drawable.img_faith,
            R.drawable.img_family,
            R.drawable.img_friends,
            R.drawable.img_hobbies,
            R.drawable.img_office,
            R.drawable.img_school
        )
        val imgAdapter = ImageAdapter(this@MainActivity, imagesList)
        imgGrid.adapter = imgAdapter

        // Open full-screen image on click.
        imgGrid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent(this@MainActivity, ImageViewActivity::class.java)
            intent.putExtra("image", imagesList[position])
            startActivity(intent)
        }
    }
}
