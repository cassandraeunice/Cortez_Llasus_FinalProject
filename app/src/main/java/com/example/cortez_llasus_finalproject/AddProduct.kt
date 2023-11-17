package com.example.cortez_llasus_finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

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
    }

}