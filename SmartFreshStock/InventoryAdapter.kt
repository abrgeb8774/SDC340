package com.example.foodexpiryandlowstocktracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
// Abreham Gebremariam
// FoodExpiryandLowStockTracker
// 02/22/2026
// This adapter is responsible for displaying the list of all food items in the inventory.
class InventoryAdapter(private var foodItems: List<FoodItem>) : RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {

    // This function is called when the RecyclerView needs a new ViewHolder to represent an item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.inventory_item, parent, false)
        return ViewHolder(view)
    }

    // This function is called to display the data at the specified position.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodItem = foodItems[position]
        holder.itemName.text = foodItem.itemName
        holder.itemCategory.text = foodItem.category
        holder.itemQuantity.text = "Qty: ${foodItem.quantity}"
    }

    // This function returns the total number of items in the list.
    override fun getItemCount(): Int {
        return foodItems.size
    }

    // This function is used to update the list of food items and refresh the RecyclerView.
    fun updateData(newFoodItems: List<FoodItem>) {
        foodItems = newFoodItems
        notifyDataSetChanged()
    }

    // This class represents a single item view in the RecyclerView.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.item_name_text_view)
        val itemCategory: TextView = itemView.findViewById(R.id.item_category_text_view)
        val itemQuantity: TextView = itemView.findViewById(R.id.item_quantity_text_view)
    }
}