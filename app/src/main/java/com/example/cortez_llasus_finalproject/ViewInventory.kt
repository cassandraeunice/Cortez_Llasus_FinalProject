package com.example.cortez_llasus_finalproject

import InventoryListAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView

class ViewInventory : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_inventory)

        // Call the viewInventory function when the activity is created
        viewInventory(findViewById(android.R.id.content))
    }

    private fun viewInventory(view: View) {
        val databaseHelper: DatabaseHelper = DatabaseHelper(this)
        val items: List<InventoryItem> = databaseHelper.viewInventory()

        val listView = findViewById<ListView>(R.id.listInventory)

        // Check if listView is not null before setting the adapter
        listView?.let {
            val inventoryListAdapter = InventoryListAdapter(this, items)
            it.adapter = inventoryListAdapter
        }
    }
}
