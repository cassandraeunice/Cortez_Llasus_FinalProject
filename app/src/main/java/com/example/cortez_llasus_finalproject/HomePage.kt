package com.example.cortez_llasus_finalproject

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.cortez_llasus_finalproject.databinding.ActivityHomePageBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class HomePage : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding // Assuming you have HomePageBinding class

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

        val btnView = findViewById<ImageButton>(R.id.btnView)

        btnView.setOnClickListener{
            val intent = Intent(this, ViewInventory:: class.java)
            startActivity(intent)
        }


        // Initialize binding
        initBinding()

        // Initialize scanner views
        initViews()
    }

    // Scanner
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showCamera()
            } else {
                // Explain why permission is needed (e.g., toast)
            }
        }

    private val scanLauncher =
        registerForActivityResult(ScanContract()) { result ->
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, AddProduct::class.java)
                intent.putExtra("SCAN_RESULT", result.contents)
                startActivity(intent)
            }
        }

    private fun showCamera() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
        options.setPrompt("Scan Barcode")
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        options.setOrientationLocked(false)

        scanLauncher.launch(options)
    }

    private fun initViews() {
        val btnScan = findViewById<ImageButton>(R.id.btnScan)

        btnScan.setOnClickListener {
            checkPermissionCamera(this)
        }
    }


    private fun checkPermissionCamera(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            showCamera()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Toast.makeText(context, "CAMERA permission required!", Toast.LENGTH_SHORT).show()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun initBinding() {
       binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
