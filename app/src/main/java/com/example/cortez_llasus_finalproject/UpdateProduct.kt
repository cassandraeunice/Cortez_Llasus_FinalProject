package com.example.cortez_llasus_finalproject

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
            textResult.text = selectedItem?.barcode.toString()
            itemNameEditText.setText(selectedItem?.itemName)
            categoryEditText.setText(selectedItem?.category)
            quantityEditText.setText(selectedItem?.quantity)
            dateaddedEditText.setText(selectedItem?.dateAdded)
        }

        val btnDelete = findViewById<Button>(R.id.btnDelete)

        btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            updateSelectedItem()
        }

        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            val intent = Intent(this, ViewInventory::class.java)
            startActivity(intent)
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
            val rowsAffected = dbHelper.deleteInventoryItem(selectedItem!!.barcode)
            if (rowsAffected > 0) {
                Toast.makeText(this, "Item deleted in the inventory.", Toast.LENGTH_LONG).show()
                finish() // Close the activity after deletion
            } else {
                Toast.makeText(this, "Item deletion error.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateSelectedItem() {
        val itemNameEditText = findViewById<EditText>(R.id.etName)
        val categoryEditText = findViewById<EditText>(R.id.etCategory)
        val quantityEditText = findViewById<EditText>(R.id.etQuantity)
        val dateaddedEditText = findViewById<EditText>(R.id.etDateAdded)

        val itemName = itemNameEditText.text.toString()
        val category = categoryEditText.text.toString()
        val quantity = quantityEditText.text.toString()
        val dateAdded = dateaddedEditText.text.toString()

        // Validate quantity as an integer
        if (quantity.toIntOrNull() == null) {
            Toast.makeText(this, "Quantity must be a valid integer", Toast.LENGTH_LONG).show()
            return
        }

        // Validate date format
        if (!isValidDateFormat(dateAdded)) {
            Toast.makeText(this, "Invalid date format. Please use MM/DD/YY", Toast.LENGTH_LONG).show()
            return
        }

        if (selectedItem != null) {
            val rowsAffected = dbHelper.updateInventoryItem(
                selectedItem!!.barcode,
                itemName,
                category,
                quantity,
                dateAdded
            )

            if (rowsAffected > 0) {
                Toast.makeText(this, "Item updated successfully.", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to update item. Please fill in all fields.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isValidDateFormat(date: String): Boolean {
        val regex = """^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\d{2}$""".toRegex()
        return date.matches(regex)
    }
}