package com.example.cortez_llasus_finalproject

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 11
        private const val DATABASE_NAME = "supplymateDB.db"
        private const val TABLE_USER = "UserInfo"
        private const val TABLE_INVENTORY = "InventoryInfo"
        private const val KEY_ID = "id"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"


        private const val KEY_INVENTORY_ID = "inventory_id"
        private const val KEY_BARCODE = "barcode"
        private const val KEY_ITEMNAME = "itemName"
        private const val KEY_CATEGORY = "category"
        private const val KEY_QUANTITY = "quantity"
        private const val KEY_DATEADDED = "dateAdded"

        const val EMAIL_ALREADY_IN_USE = -2L
    }

    private var currentUserId: Long = -1


    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USERS_TABLE = ("CREATE TABLE $TABLE_USER (" +
                "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$KEY_USERNAME TEXT," +
                "$KEY_EMAIL TEXT," +
                "$KEY_PASSWORD TEXT)")

        val CREATE_INVENTORY_TABLE = ("CREATE TABLE $TABLE_INVENTORY (" +
                "$KEY_INVENTORY_ID INTEGER," +
                "$KEY_BARCODE INTEGER," +
                "$KEY_ITEMNAME TEXT," +
                "$KEY_CATEGORY TEXT," +
                "$KEY_QUANTITY TEXT," +
                "$KEY_DATEADDED TEXT," +
                "FOREIGN KEY($KEY_INVENTORY_ID) REFERENCES $TABLE_USER($KEY_ID))")

        db?.execSQL(CREATE_USERS_TABLE)
        db?.execSQL(CREATE_INVENTORY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_INVENTORY")
        onCreate(db)
    }

    fun insertUser(username: String, email: String, password: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_USERNAME, username)
            put(KEY_EMAIL, email)
            put(KEY_PASSWORD, password)
        }

        val success = db.insert(TABLE_USER, null, values)


        return success
    }

    fun readUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val selection = "$KEY_USERNAME = ? AND $KEY_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_USER, null, selection, selectionArgs, null, null, null)

        val userExists = cursor.count > 0

        if (userExists) {
            cursor.moveToFirst()
            currentUserId = cursor.getLong(cursor.getColumnIndex(KEY_ID))
        }

        cursor.close()
        db.close()

        return userExists
    }

    fun setCurrentUserId(userId: Long) {
        currentUserId = userId
    }

    fun resetCurrentUserId() {
        currentUserId = -1
    }


    fun isEmailExists(email: String): Boolean {
        val db = this.readableDatabase
        val selection = "$KEY_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        return try {
            val cursor = db.query(TABLE_USER, null, selection, selectionArgs, null, null, null)
            val emailExists = cursor.count > 0
            cursor.close()
            emailExists
        } catch (e: SQLException) {
            e.printStackTrace()
            false
        } finally {
            db.close()
        }
    }

    fun updatePassword(email: String, newPassword: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_PASSWORD, newPassword)
        }

        val whereClause = "$KEY_EMAIL = ?"
        val whereArgs = arrayOf(email)

        val rowsAffected = db.update(TABLE_USER, values, whereClause, whereArgs)
        db.close()

        return rowsAffected
    }

    fun addInventory(
        userId: Long,
        barcode: Int,
        itemName: String,
        category: String,
        quantity: String,
        dateAdded: String
    ): Long {
        val db = this.writableDatabase

        return try {
            val values = ContentValues().apply {
                put(KEY_INVENTORY_ID, userId)  // Set the user ID as the inventory ID
                put(KEY_BARCODE, barcode)
                put(KEY_ITEMNAME, itemName)
                put(KEY_CATEGORY, category)
                put(KEY_QUANTITY, quantity)
                put(KEY_DATEADDED, dateAdded)
            }

            db.insert(TABLE_INVENTORY, null, values)
        } catch (e: Exception) {
            e.printStackTrace()
            -1 // Return -1 to indicate failure
        } finally {
            db.close()
        }
    }

    fun updateInventoryItem(barcode: Int, itemName: String, category: String, quantity: String, dateAdded: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_ITEMNAME, itemName)
            put(KEY_CATEGORY, category)
            put(KEY_QUANTITY, quantity)
            put(KEY_DATEADDED, dateAdded)
        }

        val selection = "$KEY_BARCODE = ?"
        val selectionArgs = arrayOf(barcode.toString())

        val rowsAffected = db.update(TABLE_INVENTORY, values, selection, selectionArgs)

        db.close()

        return rowsAffected
    }

    fun deleteInventoryItem(barcode: Int): Int {
        val db = this.writableDatabase

        return try {
            val whereClause = "$KEY_BARCODE = ?"
            val whereArgs = arrayOf(barcode.toString()) // Ensure that barcode is an Int

            db.delete(TABLE_INVENTORY, whereClause, whereArgs)
        } catch (e: SQLException) {
            e.printStackTrace()
            -1
        } finally {
            db.close()
        }
    }

    fun getBarcodeAtPosition(userId: Long, position: Int): String? {
        val db = this.readableDatabase
        val select = "SELECT $KEY_BARCODE FROM $TABLE_INVENTORY WHERE $KEY_INVENTORY_ID = ?"
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(select, arrayOf(userId.toString()))
        } catch (e: SQLException) {
            e.printStackTrace()
            return null
        }

        cursor?.use {
            if (it.moveToPosition(position)) {
                val barcodeIndex = it.getColumnIndex(KEY_BARCODE)
                return if (barcodeIndex >= 0) {
                    it.getString(barcodeIndex)
                } else {
                    null
                }
            }
        }

        return null
    }

    @SuppressLint("Range")
    fun viewInventory(userId: Long): List<InventoryItem> {
        Log.d("ViewInventory", "Inventory ID for SQL Query: $userId")
        val inventoryList = mutableListOf<InventoryItem>()

        val db = this.readableDatabase
        val select =
            "SELECT $KEY_BARCODE, $KEY_ITEMNAME, $KEY_CATEGORY, $KEY_QUANTITY, $KEY_DATEADDED FROM $TABLE_INVENTORY WHERE $KEY_INVENTORY_ID = ?"
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(select, arrayOf(userId.toString()))
            Log.d("ViewInventory", "SQL Query: $select")
        } catch (e: SQLException) {
            e.printStackTrace()
            return inventoryList
        }

        cursor?.use {
            if (it.moveToFirst()) {
                val barcodeIndex = it.getColumnIndex(KEY_BARCODE)
                val itemNameIndex = it.getColumnIndex(KEY_ITEMNAME)
                val categoryIndex = it.getColumnIndex(KEY_CATEGORY)
                val quantityIndex = it.getColumnIndex(KEY_QUANTITY)
                val dateAddedIndex = it.getColumnIndex(KEY_DATEADDED)

                if (barcodeIndex >= 0 && itemNameIndex >= 0 && categoryIndex >= 0 && quantityIndex >= 0 && dateAddedIndex >= 0) {
                    do {
                        val retrievedBarcode = it.getInt(barcodeIndex)
                        val retrievedItemName = it.getString(itemNameIndex)
                        val retrievedCategory = it.getString(categoryIndex)
                        val retrievedQuantity = it.getString(quantityIndex)
                        val retrievedDateAdded = it.getString(dateAddedIndex)

                        val inventoryItem = InventoryItem(
                            userId.toLong(),
                            retrievedBarcode,
                            retrievedItemName,
                            retrievedCategory,
                            retrievedQuantity,
                            retrievedDateAdded
                        )
                        inventoryList.add(inventoryItem)

                    } while (it.moveToNext())
                } else {

                    Log.e("ViewInventory", "Invalid column index")
                }
            }
        }
        Log.d("ViewInventory", "Retrieved items count: ${inventoryList.size}")
        return inventoryList
    }

    @SuppressLint("Range")
    fun getUserId(username: String): Long {
        val db = this.readableDatabase
        val selection = "$KEY_USERNAME = ?"
        val selectionArgs = arrayOf(username)
        val columns = arrayOf(KEY_ID)

        var userId: Long = -1

        try {
            val cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null)

            if (cursor.moveToFirst()) {
                userId = cursor.getLong(cursor.getColumnIndex(KEY_ID))
            }

            cursor.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return userId
    }
}
