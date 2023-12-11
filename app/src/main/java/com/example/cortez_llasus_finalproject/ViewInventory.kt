package com.example.cortez_llasus_finalproject

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.cortez_llasus_finalproject.databinding.ActivityHomePageBinding
import com.example.cortez_llasus_finalproject.databinding.ActivityViewInventoryBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class ViewInventory : AppCompatActivity(), OnItemDeletedListener {

    private lateinit var binding: ActivityViewInventoryBinding

    private lateinit var inventoryListAdapter: InventoryListAdapter
    private var userId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_inventory)

        val sessionManager = SessionManager(this)
        val userId = sessionManager.userId

        inventoryListAdapter = InventoryListAdapter(this, ArrayList())

        inventoryListAdapter.setOnItemClickListener { clickedItem ->
            val intent = Intent(this, UpdateProduct::class.java)
            intent.putExtra("SELECTED_ITEM", clickedItem)
            startActivity(intent)
        }

        inventoryListAdapter.setOnItemLongClickListener { longClickedItem ->
            showDeleteConfirmationDialog(longClickedItem)
            true
        }

        userId?.let { viewInventory(findViewById<View>(android.R.id.content), it) }

        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }

        val btnRefresh = findViewById<ImageButton>(R.id.refresh)

        btnRefresh.setOnClickListener {
            refreshView()
        }

        initViews()
    }

    private fun refreshView() {
        val databaseHelper: DatabaseHelper = DatabaseHelper(this)
        val items: List<InventoryItem> = databaseHelper.viewInventory(userId)
        updateTotalCount(items)

        inventoryListAdapter.updateData(items)

        inventoryListAdapter.notifyDataSetChanged()

        recreate()
    }

    private fun updateTotalCount(items: List<InventoryItem>) {
        val totalCount = items.size
        val textViewTotalCount = findViewById<TextView>(R.id.textViewTotalCount)
        textViewTotalCount.text = "Total Inventory Count: $totalCount"
    }

    private fun viewInventory(view: View, userId: Long) {
        val databaseHelper: DatabaseHelper = DatabaseHelper(this)
        val items: List<InventoryItem> = databaseHelper.viewInventory(userId)

        inventoryListAdapter.updateData(items)

        val listView = findViewById<ListView>(R.id.listInventory)
        listView.adapter = inventoryListAdapter

        listView.visibility = View.VISIBLE

        val totalCount = items.size
        val textViewTotalCount = findViewById<TextView>(R.id.textViewTotalCount)
        textViewTotalCount.text = "Total Inventory Count: $totalCount"
    }

    private fun showDeleteConfirmationDialog(itemToDelete: InventoryItem) {
        AlertDialog.Builder(this)
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Yes") { _, _ ->

                inventoryListAdapter.deleteItem(itemToDelete)

                val barcode = itemToDelete.barcode
                barcode?.let {
                    val databaseHelper: DatabaseHelper = DatabaseHelper(this)
                    val rowsAffected = databaseHelper.deleteInventoryItem(it)
                    if (rowsAffected > 0) {

                        Log.d("ViewInventory", "Item deleted from the database")

                        viewInventory(findViewById<View>(android.R.id.content), userId)
                    } else {

                        Log.d("ViewInventory", "Failed to delete item from the database")
                    }
                }
            }

            .setNegativeButton("No", null)
            .show()
    }

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
        val btnAdd = findViewById<ImageButton>(R.id.add)

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
        binding = ActivityViewInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        Log.d("ViewInventory", "onResume")
    }

    override fun onItemDeleted() {

    }


}