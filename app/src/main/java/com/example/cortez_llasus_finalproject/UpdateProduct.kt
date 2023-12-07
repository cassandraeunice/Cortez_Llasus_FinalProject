package com.example.cortez_llasus_finalproject

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UpdateProduct : AppCompatActivity() {

    private val dbHelper = DatabaseHelper(this)
    private var selectedItem: InventoryItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_product)

        val textResult = findViewById<TextView>(R.id.textResult)
        val scannedResult = intent.getStringExtra("SCAN_RESULT")
        textResult.text = scannedResult

        val itemNameEditText = findViewById<EditText>(R.id.etName)
        val categoryEditText = findViewById<EditText>(R.id.etCategory)
        val quantityEditText = findViewById<EditText>(R.id.etQuantity)
        val dateaddedEditText = findViewById<EditText>(R.id.etDateAdded)

        selectedItem = intent.getSerializableExtra("SELECTED_ITEM") as? InventoryItem
        if (selectedItem != null) {
            // Fill the corresponding fields with the selected item data
            textResult.text = selectedItem?.barcode
            itemNameEditText.setText(selectedItem?.itemName)
            categoryEditText.setText(selectedItem?.category)
            quantityEditText.setText(selectedItem?.quantity)
            dateaddedEditText.setText(selectedItem?.dateAdded)
        }

        val btnDelete = findViewById<Button>(R.id.btnDelete)

        btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Yes") { _, _ ->
                deleteSelectedItem()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteSelectedItem() {
        if (selectedItem != null) {
            val rowsAffected = dbHelper.deleteInventoryItem(selectedItem!!.inventory_id)
            if (rowsAffected > 0) {
                // Item deleted successfully
                finish() // Close the activity after deletion
            } else {
                // Failed to delete item, handle accordingly
                // You can show a toast or log a message
            }
        }
    }
}