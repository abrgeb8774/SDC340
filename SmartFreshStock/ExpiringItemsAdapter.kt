package com.example.foodexpiryandlowstocktracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit
// Abreham Gebremariam
// FoodExpiryandLowStockTracker
// 02/22/2026
// This adapter is responsible for displaying the list of expiring food items
// in a RecyclerView(display a scrollable list of items).
class ExpiringItemsAdapter(private var foodItems: List<FoodItem>) : RecyclerView.Adapter<ExpiringItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.expiring_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodItem = foodItems[position]
        holder.itemName.text = foodItem.itemName
        holder.itemCategory.text = foodItem.category
        holder.itemQuantity.text = foodItem.quantity.toString()

        val daysLeft = getDaysLeft(foodItem.expiryDate)
        // If daysLeft is null (due to an invalid date), display "N/A". Otherwise, show the number.
        holder.itemDaysLeft.text = daysLeft?.toString() ?: "N/A"
    }

    override fun getItemCount(): Int {
        return foodItems.size
    }

    fun updateData(newFoodItems: List<FoodItem>) {
        foodItems = newFoodItems
        notifyDataSetChanged()
    }

    private fun getDaysLeft(expiryDate: String): Long? {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val date = try {
            dateFormat.parse(expiryDate)
        } catch (e: Exception) {
            null // If parsing fails, the date will be null.
        }

        // If the date is not valid, we cannot calculate the days left, so we exit early.
        if (date == null) {
            return null
        }

        // If we reach this point, we have a valid date and can proceed with the calculation.
        val todayCal = Calendar.getInstance()
        todayCal.set(Calendar.HOUR_OF_DAY, 0)
        todayCal.set(Calendar.MINUTE, 0)
        todayCal.set(Calendar.SECOND, 0)
        todayCal.set(Calendar.MILLISECOND, 0)
        val today = todayCal.time

        val diffInMillis = date.time - today.time
        return TimeUnit.MILLISECONDS.toDays(diffInMillis)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.item_name_text_view)
        val itemCategory: TextView = itemView.findViewById(R.id.item_category_text_view)
        val itemQuantity: TextView = itemView.findViewById(R.id.item_quantity_text_view)
        val itemDaysLeft: TextView = itemView.findViewById(R.id.item_days_left_text_view)
    }
}
