package com.example.cortez_llasus_finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class AddProduct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        val textResult = findViewById<TextView>(R.id.textResult)
        val scannedResult = intent.getStringExtra("SCAN_RESULT")
        textResult.text = scannedResult

        val itemNameEditText = findViewById<EditText>(R.id.etName)
        val categoryEditText = findViewById<EditText>(R.id.etCategory)
        val quantityEditText = findViewById<EditText>(R.id.etQuantity)
        val dateaddedEditText = findViewById<EditText>(R.id.etDateAdded)

        val dbHelper = DatabaseHelper(this)

        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            val barcode = textResult.text.toString()
            val itemname = itemNameEditText.text.toString()
            val category = categoryEditText.text.toString()
            val quantity = quantityEditText.text.toString()
            val dateadded = dateaddedEditText.text.toString()

            val sessionManager = SessionManager(this)
            val user_id = sessionManager.userId

            if (barcode.trim().isNotEmpty() && itemname.trim().isNotEmpty() &&
                category.trim().isNotEmpty() && quantity.trim().isNotEmpty() &&
                dateadded.trim().isNotEmpty() && user_id != -1L) {

                val dbHelper = DatabaseHelper(this)

                // Check for selected item from ViewInventory
                val selectedItem = intent.getSerializableExtra("SELECTED_ITEM") as? InventoryItem

                if (selectedItem != null) {
                    // Update the selected item if it exists
                    val rowsAffected = dbHelper.updateInventoryItem(
                        selectedItem.inventory_id,
                        itemname,
                        category,
                        quantity,
                        dateadded
                    )

                    if (rowsAffected > 0) {
                        Toast.makeText(this, "Item updated in the inventory", Toast.LENGTH_LONG).show()
                        val i = Intent(this, ViewInventory::class.java)
                        startActivity(i)

                        itemNameEditText.text.clear()
                        categoryEditText.text.clear()
                        quantityEditText.text.clear()
                        dateaddedEditText.text.clear()

                        finish()
                    } else {
                        Toast.makeText(this, "Failed to update item in the inventory. Try Again", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // Add a new item if no item is selected
                    val result = dbHelper.addInventory(user_id, barcode, itemname, category, quantity, dateadded)

                    if (result != -1L) {
                        Toast.makeText(this, "Item added to the inventory", Toast.LENGTH_LONG).show()
                        val i = Intent(this, ViewInventory::class.java)
                        startActivity(i)

                        itemNameEditText.text.clear()
                        categoryEditText.text.clear()
                        quantityEditText.text.clear()
                        dateaddedEditText.text.clear()

                        finish()
                    } else {
                        Toast.makeText(this, "Item is not added to the inventory. Try Again", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
            }
        }


        // Check for selected item from ViewInventory
        val selectedItem = intent.getSerializableExtra("SELECTED_ITEM") as? InventoryItem
        if (selectedItem != null) {
            // Fill the corresponding fields with the selected item data
            textResult.setText(selectedItem.barcode)
            itemNameEditText.setText(selectedItem.itemName)
            categoryEditText.setText(selectedItem.category)
            quantityEditText.setText(selectedItem.quantity)
            dateaddedEditText.setText(selectedItem.dateAdded)
        }
    }
}


