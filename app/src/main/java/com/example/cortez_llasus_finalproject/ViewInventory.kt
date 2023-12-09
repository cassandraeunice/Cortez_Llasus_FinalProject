package com.example.cortez_llasus_finalproject

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ViewInventory : AppCompatActivity(), OnItemDeletedListener {
    private lateinit var inventoryListAdapter: InventoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_inventory)

        val sessionManager = SessionManager(this)
        val userId = sessionManager.userId

        inventoryListAdapter = InventoryListAdapter(this, ArrayList())

        inventoryListAdapter.setOnItemClickListener { clickedItem ->
            val intent = Intent(this, UpdateProduct::class.java)
            intent.putExtra("SELECTED_ITEM", clickedItem)
            startActivity(intent)
        }

        inventoryListAdapter.setOnItemLongClickListener { longClickedItem ->
            showDeleteConfirmationDialog(longClickedItem)
            true
        }

        userId?.let { viewInventory(findViewById<View>(android.R.id.content), it) }

        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }
    }

    private fun viewInventory(view: View, userId: Long) {
        val databaseHelper: DatabaseHelper = DatabaseHelper(this)
        val items: List<InventoryItem> = databaseHelper.viewInventory(userId)

        inventoryListAdapter.updateData(items)

        val listView = findViewById<ListView>(R.id.listInventory)
        listView.adapter = inventoryListAdapter

        listView.visibility = View.VISIBLE

        val totalCount = items.size
        val textViewTotalCount = findViewById<TextView>(R.id.textViewTotalCount)
        textViewTotalCount.text = "Total Inventory Count: $totalCount"
    }

    private fun showDeleteConfirmationDialog(itemToDelete: InventoryItem) {
        AlertDialog.Builder(this)
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Yes") { _, _ ->
                // Call deleteItem on the adapter to remove the item
                inventoryListAdapter.deleteItem(itemToDelete)

                // If needed, you can also delete the item from the database here
                val barcode = itemToDelete.barcode
                barcode?.let {
                    val databaseHelper: DatabaseHelper = DatabaseHelper(this)
                    val rowsAffected = databaseHelper.deleteInventoryItem(it)
                    if (rowsAffected > 0) {
                        // Item deleted successfully
                        Log.d("ViewInventory", "Item deleted from the database")
                    } else {
                        // Failed to delete item, handle accordingly
                        Log.d("ViewInventory", "Failed to delete item from the database")
                    }
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        Log.d("ViewInventory", "onResume")
    }

    override fun onItemDeleted() {
        // Refresh the list view after deleting an item
        refreshListView()
    }

    private fun refreshListView() {
        // Refresh the list view by updating the data
        inventoryListAdapter.notifyDataSetChanged()
    }

}