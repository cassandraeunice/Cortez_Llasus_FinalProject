package com.example.cortez_llasus_finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.*
import com.airbnb.lottie.LottieAnimationView
import android.content.Intent


class MainActivity : AppCompatActivity() {

    private val ANIMATION_DURATION: Long = 6000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val welcomeAnimationView: LottieAnimationView = findViewById(R.id.logoanimation)

        welcomeAnimationView.playAnimation()

        Handler(Looper.getMainLooper()).postDelayed({

            val intent = Intent(this@MainActivity, LoginPage::class.java)
            startActivity(intent)
        }, ANIMATION_DURATION)

    }
}