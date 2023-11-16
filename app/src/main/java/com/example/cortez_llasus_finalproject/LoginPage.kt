package com.example.cortez_llasus_finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

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

                    Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()

                    val i = Intent(this, HomePage::class.java)
                    startActivity(i)
                    i.putExtra("USERNAME", username)
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

        }
    }
}