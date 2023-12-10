package com.example.cortez_llasus_finalproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*

class LoginPage : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        sessionManager = SessionManager(this)

        var unameEditText = findViewById<EditText>(R.id.uname)
        var passwordEditText = findViewById<EditText>(R.id.password)
        var btnLogin = findViewById<Button>(R.id.btnLogin)
        var tvSignUp = findViewById<TextView>(R.id.tvSignUp)
        var tvForgotPassw = findViewById<TextView>(R.id.tvForgotPassword)

        // Database helper instance
        val dbHelper = DatabaseHelper(this)

        btnLogin.setOnClickListener {
            val username = unameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.trim().isNotEmpty() && password.trim().isNotEmpty()) {
                val success = dbHelper.readUser(username, password)

                if (success) {
                    // Get the user identifier from your database (replace with your logic)
                    val userId = dbHelper.getUserId(username)

                    // Store the user identifier in SharedPreferences
                    val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putLong("USER_ID", userId.toInt().toLong())
                    editor.putString("USERNAME", username)
                    editor.apply()

                    Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()

                    val i = Intent(this, HomePage::class.java)
                    startActivity(i)

                    unameEditText.text.clear()
                    passwordEditText.text.clear()

                    finish()
                } else {
                    Toast.makeText(this, "Login Unsuccessful. Check your credentials.", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Username or Password cannot be blank", Toast.LENGTH_LONG).show()
            }
        }

        tvSignUp.setOnClickListener {
            val i = Intent(this, SignUpPage::class.java)
            startActivity(i)
        }

        tvForgotPassw.setOnClickListener {
            val i = Intent(this, ForgotPassword::class.java)
            startActivity(i)
        }
    }
}