package com.example.cortez_llasus_finalproject

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper (context: Context) : SQLiteOpenHelper (context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "supplymateDB"
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
                "$KEY_DATEADDED TEXT)")
        db?.execSQL(CREATE_USERS_TABLE)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun insertUser(username: String,email: String, password: String): Long {
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

    fun addInventory(barcode: String, itemName: String, category: String, quantity: String, dateAdded: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_BARCODE, barcode)
            put(KEY_ITEMNAME, itemName)
            put(KEY_CATEGORY, category)
            put(KEY_QUANTITY, quantity)
            put(KEY_DATEADDED, dateAdded)
        }
        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        return success
    }


    @SuppressLint("Range")
    fun viewInventory(): List<InventoryItem> {
        val inventoryList = mutableListOf<InventoryItem>()

        val db = this.readableDatabase
        val select = "SELECT $KEY_BARCODE, $KEY_ITEMNAME, $KEY_CATEGORY, $KEY_QUANTITY, $KEY_DATEADDED FROM $TABLE_NAME"
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(select, null)
        } catch (e: SQLException) {
            db.execSQL(select)
            return inventoryList
        }

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val retrievedBarcode = it.getString(it.getColumnIndex(KEY_BARCODE))
                    val retrievedItemName = it.getString(it.getColumnIndex(KEY_ITEMNAME))
                    val retrievedCategory = it.getString(it.getColumnIndex(KEY_CATEGORY))
                    val retrievedQuantity = it.getString(it.getColumnIndex(KEY_QUANTITY))
                    val retrievedDateAdded = it.getString(it.getColumnIndex(KEY_DATEADDED))

                    val inventoryItem = InventoryItem(
                        retrievedBarcode,
                        retrievedItemName,
                        retrievedCategory,
                        retrievedQuantity,
                        retrievedDateAdded
                    )
                    inventoryList.add(inventoryItem)

                } while (it.moveToNext())
            }
        }

        return inventoryList
    }


}