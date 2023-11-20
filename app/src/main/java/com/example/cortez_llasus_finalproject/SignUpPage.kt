package com.example.cortez_llasus_finalproject

import android.app.Activity
import android.content.Intent
import android.os.Build.VERSION_CODES
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class SignUpPage : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_page)

        dbHelper = DatabaseHelper(this)
    }

    fun saveRecord(view: View) {
        val unameEditText = findViewById<EditText>(R.id.uname)
        val emailEditText = findViewById<EditText>(R.id.email)
        val passEditText = findViewById<EditText>(R.id.password)
        val confPassEditText = findViewById<EditText>(R.id.confpass)

        val username = unameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passEditText.text.toString()
        val confirmPassword = confPassEditText.text.toString()

        if (username.trim().isNotEmpty() && email.trim().isNotEmpty() &&
            password.trim().isNotEmpty() && confirmPassword.trim().isNotEmpty())
        {
            if (password == confirmPassword) {
                val success = dbHelper.insertUser(username, email, password)

                if (success != -1L) {

                    Toast.makeText(this, "Signed up Successfully", Toast.LENGTH_LONG).show()

                    val i = Intent(this, LoginPage::class.java)
                    startActivity(i)

                    unameEditText.text.clear()
                    emailEditText.text.clear()
                    passEditText.text.clear()
                    confPassEditText.text.clear()

                    finish()

                }

                else {

                    Toast.makeText(this, "Sign Up Unsuccessfully. Try again.", Toast.LENGTH_LONG).show()
                }
            }

            else {

                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show()
            }
        }

        else {

            Toast.makeText(this, "Username, Email, Password, or Confirm Password cannot be blank", Toast.LENGTH_LONG).show()
        }
    }
}