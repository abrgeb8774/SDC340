package com.example.abrehamgebremariam_photogallery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
//Abreham Gebremariam
//My Photo Gallery
//02/20/2026

// Adapter for displaying images in a GridView
class ImageAdapter(
    private val context: Context,
    private val imageIds: List<Int>
) : BaseAdapter() {
    private val layoutInflater: LayoutInflater =
        LayoutInflater.from(context)

    // Returns the number of images.
    override fun getCount(): Int = imageIds.size
    override fun getItem(position: Int): Any = imageIds[position]
    override fun getItemId(position: Int): Long = position.toLong()

     // Creates and populates each grid item.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: layoutInflater.inflate(R.layout.row_item, parent, false)
        val imageView: ImageView = view.findViewById(R.id.imgRowItem)
        imageView.setImageResource(imageIds[position])
        return view
    }
}
