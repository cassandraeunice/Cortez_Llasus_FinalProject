package com.example.cortez_llasus_finalproject

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val PREF_NAME = "UserSession"
        private const val KEY_USER_ID = "user_id"
    }

    var userId: Long
        get() = sharedPreferences.getLong(KEY_USER_ID, -1)
        set(value) {
            editor.putLong(KEY_USER_ID, value)
            editor.apply()
        }

    fun logout() {
        editor.clear()
        editor.apply()
    }
}
