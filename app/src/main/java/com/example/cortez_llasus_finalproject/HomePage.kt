package com.example.cortez_llasus_finalproject

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
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

    private lateinit var binding: ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        initBinding()

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("USERNAME", "")

        val welcomeTextView = binding.root.findViewById<TextView>(R.id.welcome_use)


        val welcomeText = "Welcome, \n$username!"
        welcomeTextView.text = welcomeText

        val logoutButton = binding.root.findViewById<ImageButton>(R.id.btnLogout)

        logoutButton.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
            finish()
        }

        val btnView = binding.btnView

        btnView.setOnClickListener {
            val intent = Intent(this, ViewInventory::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }


        val mbtnView = findViewById<ImageButton>(R.id.miniBtnView)

        mbtnView.setOnClickListener {
            val intent = Intent(this, ViewInventory::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }

        initViews()
    }

    // Scanner
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showCamera()
            } else {
                Toast.makeText(this, "Camera is required to access this feature. Check your settings.", Toast.LENGTH_LONG).show()
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
        val btnAdd = findViewById<ImageButton>(R.id.miniBtnAdd)

        btnScan.setOnClickListener {
            checkPermissionCamera(this)
        }

        btnAdd.setOnClickListener {
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
