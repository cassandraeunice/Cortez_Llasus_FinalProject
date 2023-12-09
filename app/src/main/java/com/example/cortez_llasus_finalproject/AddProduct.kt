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
            // Assuming you have a session manager to get the user_id
            val sessionManager = SessionManager(this)
            val user_id = sessionManager.userId

            if (barcode.trim().isNotEmpty() && itemname.trim().isNotEmpty() &&
                category.trim().isNotEmpty() && quantity.trim().isNotEmpty() &&
                dateadded.trim().isNotEmpty() && user_id != -1L) {

                // Validate quantity as an integer
                if (quantity.toIntOrNull() == null) {
                    Toast.makeText(this, "Quantity must be a valid integer", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                // Validate date format
                if (!isValidDateFormat(dateadded)) {
                    Toast.makeText(this, "Invalid date format. Please use MM/DD/YY", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                val result = dbHelper.addInventory(user_id, barcode, itemname, category, quantity, dateadded)

                if (result != -1L) {
                            // Insert successful
                            Toast.makeText(this, "Item added to the inventory", Toast.LENGTH_LONG).show()
                            val i = Intent(this, ViewInventory::class.java)
                            startActivity(i)
                            itemNameEditText.text.clear()
                            categoryEditText.text.clear()
                            quantityEditText.text.clear()
                            dateaddedEditText.text.clear()
                            finish()
                        } else {
                            // Insert failed
                            Toast.makeText(this, "Item is not added to the inventory. Try Again", Toast.LENGTH_LONG).show()
                        }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
            }
        }

        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }
    }

    private fun isValidDateFormat(date: String): Boolean {
        val regex = """^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\d{2}$""".toRegex()
        return date.matches(regex)
    }
}