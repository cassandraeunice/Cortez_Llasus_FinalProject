package com.example.cortez_llasus_finalproject

import java.io.Serializable

data class InventoryItem(
    val inventory_id: Long,
    val barcode: String,
    val itemName: String,
    val category: String,
    val quantity: String,
    val dateAdded: String
) : Serializable