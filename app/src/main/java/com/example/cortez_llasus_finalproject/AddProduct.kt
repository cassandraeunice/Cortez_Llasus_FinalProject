package com.example.cortez_llasus_finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class AddProduct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        // Find the TextView in the layout of AddProduct
        val textResult = findViewById<TextView>(R.id.textResult)

        // Retrieve the scanned result from the intent
        val scannedResult = intent.getStringExtra("SCAN_RESULT")

        // Set the text of the TextView
        textResult.text = scannedResult

        var itemNameEditText = findViewById<EditText>(R.id.etName)
        var categoryEditText = findViewById<EditText>(R.id.etCategory)
        var quantityEditText = findViewById<EditText>(R.id.etQuantity)
        var dateaddedEditText = findViewById<EditText>(R.id.etDateAdded)

        val dbHelper = DatabaseHelper(this)

        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            val barcode = textResult.text.toString()
            val itemname = itemNameEditText.text.toString()
            val category = categoryEditText.text.toString()
            val quantity = quantityEditText.text.toString()
            val dateadded = dateaddedEditText.text.toString()

            if (barcode.trim().isNotEmpty() && itemname.trim().isNotEmpty() &&
                category.trim().isNotEmpty() && quantity.trim().isNotEmpty() &&
                dateadded.trim().isNotEmpty()){
                val success = dbHelper.addInventory(barcode, itemname, category, quantity, dateadded)

                Toast.makeText(this, "Item added to the inventory", Toast.LENGTH_LONG).show()

                val i = Intent(this, ViewInventory::class.java)
                startActivity(i)

                itemNameEditText.text.clear()
                categoryEditText.text.clear()
                quantityEditText.text.clear()
                dateaddedEditText.text.clear()

                finish()
            } // add else statements
            else{
                Toast.makeText(this, "Item is not added to the inventory. Try Again", Toast.LENGTH_LONG).show()
            }
        }
    }

}