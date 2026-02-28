package com.example.foodexpiryandlowstocktracker
// Abreham Gebremariam
// FoodExpiryandLowStockTracker
// 02/22/2026
// Defines the data structure for a single food item.
data class FoodItem(
    val itemName: String,
    val category: String,
    var quantity: Int,
    val purchaseDate: String,
    val expiryDate: String
)