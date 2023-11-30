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
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "supplymateDB.db"
        private const val TABLE_NAME = "UserInfo"
        private const val KEY_ID = "id"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
        private const val KEY_ITEMNAME = "itemName"
        private const val KEY_BARCODE = "barcode"
        private const val KEY_CATEGORY = "category"
        private const val KEY_QUANTITY = "quantity"
        private const val KEY_DATEADDED = "dateAdded"
        private const val KEY_USER_ID = "user_id"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USERS_TABLE = ("CREATE TABLE $TABLE_NAME (" +
                "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$KEY_USERNAME TEXT," +
                "$KEY_EMAIL TEXT," +
                "$KEY_PASSWORD TEXT," +
                "$KEY_BARCODE TEXT," +
                "$KEY_ITEMNAME TEXT," +
                "$KEY_CATEGORY TEXT," +
                "$KEY_QUANTITY TEXT," +
                "$KEY_DATEADDED TEXT," +
                "$KEY_USER_ID INTEGER)")
        db?.execSQL(CREATE_USERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertUser(username: String, email: String, password: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_USERNAME, username)
            put(KEY_EMAIL, email)
            put(KEY_PASSWORD, password)
        }

        val success = db.insert(TABLE_NAME, null, values)
        db.close()

        return success
    }

    fun readUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val selection = "$KEY_USERNAME = ? AND $KEY_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)

        val userExists = cursor.count > 0
        cursor.close()

        return userExists
    }

    // Function to check if the email exists in the database
    fun isEmailExists(email: String): Boolean {
        val db = this.readableDatabase
        val selection = "$KEY_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        return try {
            val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)
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

        val rowsAffected = db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()

        return rowsAffected
    }

    fun addInventory(
        user_id: Int,
        barcode: String,
        itemName: String,
        category: String,
        quantity: String,
        dateAdded: String
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_BARCODE, barcode)
            put(KEY_ITEMNAME, itemName)
            put(KEY_CATEGORY, category)
            put(KEY_QUANTITY, quantity)
            put(KEY_DATEADDED, dateAdded)
            put(KEY_USER_ID, user_id)
        }
        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        return success
    }

    private val inventoryList = mutableListOf<InventoryItem>()

    @SuppressLint("Range")
    fun viewInventory(userId: Int): List<InventoryItem> {
        val inventoryList = mutableListOf<InventoryItem>()

        val db = this.readableDatabase
        val select =
            "SELECT $KEY_BARCODE, $KEY_ITEMNAME, $KEY_CATEGORY, $KEY_QUANTITY, $KEY_DATEADDED FROM $TABLE_NAME WHERE $KEY_USER_ID = ?"
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

                // Check if column indices are valid
                if (barcodeIndex >= 0 && itemNameIndex >= 0 && categoryIndex >= 0 && quantityIndex >= 0 && dateAddedIndex >= 0) {
                    do {
                        val retrievedBarcode = it.getString(barcodeIndex)
                        val retrievedItemName = it.getString(itemNameIndex)
                        val retrievedCategory = it.getString(categoryIndex)
                        val retrievedQuantity = it.getString(quantityIndex)
                        val retrievedDateAdded = it.getString(dateAddedIndex)

                        val inventoryItem = InventoryItem(
                            retrievedBarcode,
                            retrievedItemName,
                            retrievedCategory,
                            retrievedQuantity,
                            retrievedDateAdded
                        )
                        inventoryList.add(inventoryItem)

                    } while (it.moveToNext())
                } else {
                    // Log an error or handle the case where a column index is invalid
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

        var userId: Long = -1 // Default value in case of failure

        try {
            val cursor =
                db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null)

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