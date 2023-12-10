package com.example.cortez_llasus_finalproject

import java.io.Serializable

data class InventoryItem(
    val inventory_id: Long,
    val barcode: Int,
    val itemName: String,
    val category: String,
    val quantity: String,
    val dateAdded: String
) : Serializable {
    override fun equals(other: Any?): Boolean {
        return if (other is InventoryItem) {
            this.barcode == other.barcode
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return barcode.hashCode()
    }
}