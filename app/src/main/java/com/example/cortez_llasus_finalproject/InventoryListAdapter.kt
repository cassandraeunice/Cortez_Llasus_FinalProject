package com.example.cortez_llasus_finalproject
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class InventoryListAdapter(context: Context, arrayList: ArrayList<Any>) :
    ArrayAdapter<InventoryItem>(context, R.layout.list_item_inventory) {

    private var itemList: List<InventoryItem> = emptyList()

    fun updateData(newList: List<InventoryItem>) {
        itemList = newList
        notifyDataSetChanged()
        Log.d("InventoryListAdapter", "Updated with ${itemList.size} items")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_inventory, parent, false)

        val currentItem = itemList[position]

        val barcodeTextView = itemView.findViewById<TextView>(R.id.textViewBarcode)
        val itemNameTextView = itemView.findViewById<TextView>(R.id.textViewItemName)
        val categoryTextView = itemView.findViewById<TextView>(R.id.textViewCategory)
        val quantityTextView = itemView.findViewById<TextView>(R.id.textViewQuantity)
        val dateAddedTextView = itemView.findViewById<TextView>(R.id.textViewDateAdded)

        barcodeTextView.text = "Barcode: ${currentItem.barcode}"
        itemNameTextView.text = "Item Name: ${currentItem.itemName}"
        categoryTextView.text = "Category: ${currentItem.category}"
        quantityTextView.text = "Quantity: ${currentItem.quantity}"
        dateAddedTextView.text = "Date Added: ${currentItem.dateAdded}"

        return itemView
    }

    override fun getCount(): Int {
        return itemList.size
    }

}


