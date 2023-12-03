package com.example.cortez_llasus_finalproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ViewInventory : AppCompatActivity() {
    private lateinit var inventoryListAdapter: InventoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_inventory)

        val sessionManager = SessionManager(this)
        val userId = sessionManager.userId

        inventoryListAdapter = InventoryListAdapter(this, ArrayList())

        inventoryListAdapter.setOnItemClickListener { clickedItem ->
            val intent = Intent(this, AddProduct::class.java)
            intent.putExtra("SELECTED_ITEM", clickedItem)
            startActivity(intent)
        }

        userId?.let { viewInventory(findViewById<View>(android.R.id.content), it) }
    }

    private fun viewInventory(view: View, userId: Long) {
        val databaseHelper: DatabaseHelper = DatabaseHelper(this)
        val items: List<InventoryItem> = databaseHelper.viewInventory(userId)

        inventoryListAdapter.updateData(items)

        val listView = findViewById<ListView>(R.id.listInventory)
        listView.adapter = inventoryListAdapter

        listView.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        Log.d("ViewInventory", "onResume")
    }
}
