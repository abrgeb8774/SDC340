package com.example.abrehamgebremariam_photogallery

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
//Abreham Gebremariam
//My Photo Gallery
//02/20/2026

//Displays a single, full-screen image.
class ImageViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)

        // Get image from intent and display it.
        val imageView: ImageView = findViewById(R.id.fullImageView)
        val imageResId = intent.getIntExtra("image", 0)

        if (imageResId != 0) {
            imageView.setImageResource(imageResId)
        }
    }
}
