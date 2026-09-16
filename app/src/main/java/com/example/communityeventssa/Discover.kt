package com.example.communityeventssa

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import java.util.Locale

class Discover : AppCompatActivity() {
    private var selectedDateTime: String = ""

    private val eventList = listOf(
        "Community Fun Run",
        "Community Food Drive",
        "Community Clean-Up",
        "Youth Sports Day",
        "Career Workshop"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale()
        enableEdgeToEdge()
        setContentView(R.layout.activity_discover)

        val spinnerCategory = findViewById<Spinner>(R.id.filter_category)
        val spinnerLocation = findViewById<Spinner>(R.id.filter_location)
        val btnApply = findViewById<Button>(R.id.button_apply_filters)
        val btnDateFilter = findViewById<Button>(R.id.button_date_filter)
        val inputSearch = findViewById<EditText>(R.id.input_search)
        val btnSearch = findViewById<ImageButton>(R.id.button_search)

        val categories = arrayOf("All Categories", "Sports", "Education", "Music", "Business", "Volunteering")
        val locations = arrayOf("All Locations", "Durban", "Johannesburg", "Cape Town", "Pretoria")

        spinnerCategory.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinnerLocation.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locations)

        btnDateFilter.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                    selectedDateTime = String.format(Locale.getDefault(), "%02d/%02d/%04d %02d:%02d", selectedDay, selectedMonth + 1, selectedYear, selectedHour, selectedMinute)
                    btnDateFilter.text = selectedDateTime
                }, hour, minute, true).show()
            }, year, month, day).show()
        }

        btnSearch.setOnClickListener {
            val query = inputSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                val foundEvent = eventList.find { it.contains(query, ignoreCase = true) }
                if (foundEvent != null) {
                    Toast.makeText(this, getString(R.string.search_result_found, foundEvent), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, getString(R.string.search_no_result), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.search_hint), Toast.LENGTH_SHORT).show()
            }
        }

        btnApply.setOnClickListener {
            val category = spinnerCategory.selectedItem.toString()
            val location = spinnerLocation.selectedItem.toString()
            val filterMsg = "Filtering: $category in $location" + (if (selectedDateTime.isNotEmpty()) " on $selectedDateTime" else "")
            Toast.makeText(this, filterMsg, Toast.LENGTH_LONG).show()
        }

        // View Event Buttons - Using safe calls to prevent crashes if cards are missing
        findViewById<Button>(R.id.button_view_event_1)?.apply {
            setOnClickListener { startActivity(Intent(this@Discover, EventSharing::class.java)) }
            setOnLongClickListener { 
                saveBookmark("Community Fun Run")
                true
            }
        }

        findViewById<Button>(R.id.button_view_event_2)?.apply {
            setOnClickListener { startActivity(Intent(this@Discover, EventSharing::class.java)) }
            setOnLongClickListener { 
                saveBookmark("Community Food Drive")
                true
            }
        }

        findViewById<Button>(R.id.button_view_event_3)?.apply {
            setOnClickListener { startActivity(Intent(this@Discover, EventSharing::class.java)) }
            setOnLongClickListener { 
                saveBookmark("Community Clean-Up")
                true
            }
        }

        // Bottom Navigation
        findViewById<Button>(R.id.nav_home)?.setOnClickListener { 
            startActivity(Intent(this, Dashboard::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
            finish()
        }
        findViewById<Button>(R.id.nav_bookmarks)?.setOnClickListener {
            startActivity(Intent(this, Bookmarks::class.java))
        }
        findViewById<Button>(R.id.nav_settings)?.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }
    }

    private fun saveBookmark(eventName: String) {
        val prefs = getSharedPreferences("Bookmarks", Context.MODE_PRIVATE)
        val savedEvents = prefs.getStringSet("event_set", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        savedEvents.add(eventName)
        prefs.edit().putStringSet("event_set", savedEvents).apply()
        Toast.makeText(this, getString(R.string.event_bookmarked), Toast.LENGTH_SHORT).show()
    }

    private fun applyLocale() {
        val prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val langCode = prefs.getString("lang", "en") ?: "en"
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
