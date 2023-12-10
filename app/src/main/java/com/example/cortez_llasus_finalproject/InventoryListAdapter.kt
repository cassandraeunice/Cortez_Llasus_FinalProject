package com.example.cortez_llasus_finalproject

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class InventoryListAdapter(
    context: Context,
    private var itemList: MutableList<InventoryItem>,
    private var onItemDeletedListener: OnItemDeletedListener? = null
) : ArrayAdapter<InventoryItem>(context, R.layout.list_item_inventory), OnItemDeletedListener {

    private var onItemClickListener: ((InventoryItem) -> Unit)? = null
    private var onItemLongClickListener: ((InventoryItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (InventoryItem) -> Unit) {
        this.onItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: (InventoryItem) -> Unit) {
        this.onItemLongClickListener = listener
    }

    fun updateData(newList: List<InventoryItem>) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
        Log.d("InventoryListAdapter", "Updated with ${itemList.size} items")
    }

    fun deleteItem(itemToDelete: InventoryItem) {
        val barcodeToDelete = itemToDelete.barcode

        // Remove the item from the list based on the barcode
        itemList.removeAll { it.barcode == barcodeToDelete }

        notifyDataSetChanged()
        onItemDeletedListener?.onItemDeleted()
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

        itemView.setOnClickListener {
            onItemClickListener?.invoke(currentItem)
        }

        itemView.setOnLongClickListener {
            onItemLongClickListener?.invoke(currentItem)
            true
        }

        return itemView
    }

    override fun getCount(): Int {
        return itemList.size
    }

    override fun onItemDeleted() {
        deleteItem(InventoryItem(0, 0, "", "", "", ""))
    }


}