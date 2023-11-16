package com.example.cortez_llasus_finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val username = intent.getStringExtra("USERNAME")

        val welcomeTextView = findViewById<TextView>(R.id.welcome_use)

        val welcomeText = "Welcome, $username!"
        welcomeTextView.text = welcomeText

        val logoutButton = findViewById<Button>(R.id.button)

        logoutButton.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
            finish()
        }
    }
}