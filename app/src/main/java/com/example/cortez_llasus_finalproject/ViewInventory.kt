package com.example.cortez_llasus_finalproject

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ViewInventory : AppCompatActivity() {
    private lateinit var inventoryListAdapter: InventoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ViewInventory", "onCreate")
        setContentView(R.layout.activity_view_inventory)

        // Create an instance of SessionManager
        val sessionManager = SessionManager(this)

        // Get the user_id using the session manager
        val userId = sessionManager.userId

        Log.d("ViewInventory", "Inventory ID from SessionManager: $userId")


        // Initialize the inventoryListAdapter
        inventoryListAdapter = InventoryListAdapter(this, ArrayList())


        // Call the viewInventory function with the user_id
        userId?.let { viewInventory(findViewById<View>(android.R.id.content), it) }
    }




    private fun viewInventory(view: View, userId: Int) {
        val databaseHelper: DatabaseHelper = DatabaseHelper(this)
        val items: List<InventoryItem> = databaseHelper.viewInventory(userId)

        Log.d("ViewInventory", "Retrieved items count: ${items.size}")
        Log.d("ViewInventory", "Retrieved items: $items")


        // Update the data in the adapter
        inventoryListAdapter.updateData(items)

        val listView = findViewById<ListView>(R.id.listInventory)
        listView.adapter = inventoryListAdapter
        Log.d("ViewInventory", "Adapter: $inventoryListAdapter, ListView: $listView")


        // Set ListView visibility to VISIBLE
        listView.visibility = View.VISIBLE

        Log.d("ViewInventory", "ListView visibility: ${listView.visibility}")
    }


    override fun onResume() {
        super.onResume()
        Log.d("ViewInventory", "onResume")
    }

}

