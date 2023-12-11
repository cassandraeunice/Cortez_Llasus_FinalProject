package com.example.cortez_llasus_finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        var newpassEditText = findViewById<EditText>(R.id.newpass)
        var confnewpassEditText = findViewById<EditText>(R.id.confnewpass)
        var emailEditText = findViewById<EditText>(R.id.emailForgot)
        var btnSubmit = findViewById<Button>(R.id.btnSubmit)
        var tvLogin = findViewById<TextView>(R.id.tvLogin)

        val dbHelper = DatabaseHelper(this)

        btnSubmit.setOnClickListener {
            fun isValidPassword(password: String): Boolean {
                val passwordRegex =
                    Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
                return passwordRegex.matches(password)
            }

            val email = emailEditText.text.toString()
            val newPassword = newpassEditText.text.toString()
            val confPassword = confnewpassEditText.text.toString()

            if (email.trim().isNotEmpty() && newPassword.isNotEmpty() && newPassword == confPassword) {
                val success = dbHelper.isEmailExists(email)
                if (isValidPassword(newPassword)) {
                    if (success) {

                        val rowsAffected = dbHelper.updatePassword(email, newPassword)

                        if (rowsAffected > 0) {
                            Toast.makeText(this, "Password updated successfully", Toast.LENGTH_LONG).show()

                            val i = Intent(this, LoginPage::class.java)
                            startActivity(i)
                            finish()
                        } else {
                            Toast.makeText(this, "Failed to update password", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this, "Email Not Found", Toast.LENGTH_LONG).show()
                    }
                }
                else {
                    Toast.makeText(
                        this,
                        "Password must contain a small letter, capital letter, number, and special character. Minimum of 8 characters.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else {
                Toast.makeText(this, "Please enter a valid email and matching passwords", Toast.LENGTH_LONG).show()
            }
        }

        tvLogin.setOnClickListener {
            val i = Intent(this, LoginPage::class.java)
            startActivity(i)
            finish()
        }
    }
}