package com.cs388group6.packer

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

import java.util.*

class MyItemListAdd : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private var auth = Firebase.auth
    private val user = auth.currentUser?.uid ?: ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_add_screen)

        database = FirebaseDatabase.getInstance().reference

        //Set default selection
        val bottomNavigationView1: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView1.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_login -> { true }
                R.id.nav_logout -> {true }
                R.id.nav_home -> { true }
                R.id.nav_list -> { true }
            }
            true
        }
        bottomNavigationView1.selectedItemId = R.id.nav_list

        // Bottom Navigation Selection
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_login -> {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                R.id.nav_logout -> {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                R.id.nav_home -> {
                    startActivity(Intent(this, TripList::class.java))
                }
                R.id.nav_list -> {
                    startActivity(Intent(this, MyItemList::class.java))
                }
            }
            true
        }

        val saveButton = findViewById<Button>(R.id.newItemSaveButton)
        saveButton.setOnClickListener {

            val itemName: String = findViewById<EditText>(R.id.newItemNameInput).text.toString()
            val itemWeight = findViewById<EditText>(R.id.newItemWeightInput).text.toString()
            val itemWeightUnit = findViewById<Spinner>(R.id.newItemChooseWeightUnitView).selectedItem.toString()
            val itemCategory = findViewById<EditText>(R.id.editItemCategoryInput).text.toString()
            val image = findViewById<ImageButton>(R.id.newItemImageDisplay)

            // Create image bitmap
            val bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
            val imageString = ImageConverter.bitmapToString(bitmap)

            val key = database.child("items").push().key.toString()
            val item = Item(
                name = itemName,
                userID = user,
                weight = "$itemWeight $itemWeightUnit",
                image = imageString,
                category = itemCategory,
                itemID = key
            )
            database.child("items").child(key).setValue(item)
            finish()
        }
    }
}