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

        btnLogin.setOnClickListener {

            val i = Intent(this, HomePage::class.java)
            startActivity(i)
        }

        tvSignUp.setOnClickListener {

            val i = Intent(this, SignUpPage::class.java)
            startActivity(i)
        }

        tvForgotPassw.setOnClickListener {

        }
    }
}