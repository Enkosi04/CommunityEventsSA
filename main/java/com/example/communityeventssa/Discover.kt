package com.example.communityeventssa

// Imports: Date/Time pickers, UI widgets, AppCompat
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

/**
 * File: Discover.kt
 * Screen: Discover / Search events
 * Purpose: Lets user search + filter events by text, category, location and date.
 * Features:
 * 1. Search bar - searches the hardcoded event list
 * 2. Filters - Category + Location spinners + Date picker
 * 3. View buttons - Tap = open EventSharing details, Long-press = bookmark event
 * 4. Bottom nav - Home / Bookmarks / Settings
 */
class Discover : AppCompatActivity() {

    // Stores the date + time picked in the Date Filter, e.g. "12/09/2026 08:00"
    private var selectedDateTime: String = ""

    // Hardcoded demo events for POE - in real app this would come from API / Firebase
    private val eventList = listOf(
        "Community Fun Run",
        "Community Food Drive",
        "Community Clean-Up",
        "Youth Sports Day",
        "Career Workshop"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Apply saved language (en / af / zu) BEFORE showing the screen
        applyLocale()
        enableEdgeToEdge()
        setContentView(R.layout.activity_discover)

        // Link Kotlin variables to views in activity_discover.xml
        val spinnerCategory = findViewById<Spinner>(R.id.filter_category)
        val spinnerLocation = findViewById<Spinner>(R.id.filter_location)
        val btnApply = findViewById<Button>(R.id.button_apply_filters)
        val btnDateFilter = findViewById<Button>(R.id.button_date_filter)
        val inputSearch = findViewById<EditText>(R.id.input_search)
        val btnSearch = findViewById<ImageButton>(R.id.button_search)

        // Dropdown options for filters
        val categories = arrayOf("All Categories", "Sports", "Education", "Music", "Business", "Volunteering")
        val locations = arrayOf("All Locations", "Durban", "Johannesburg", "Cape Town", "Pretoria")

        // Set adapters = tells the Spinner what list to show + how to show it
        spinnerCategory.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinnerLocation.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locations)

        // --- Date Filter Button: DatePicker -> TimePicker -> save result ---
        btnDateFilter.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                    // Format as DD/MM/YYYY HH:MM. +1 because months start at 0
                    selectedDateTime = String.format(
                        Locale.getDefault(),
                        "%02d/%02d/%04d %02d:%02d",
                        selectedDay, selectedMonth + 1, selectedYear, selectedHour, selectedMinute
                    )
                    btnDateFilter.text = selectedDateTime
                }, hour, minute, true).show()
            }, year, month, day).show()
        }

        // --- Search Button: Search by event name ---
        btnSearch.setOnClickListener {
            val query = inputSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                // .find = returns first event containing the query, ignoreCase = "fun run" finds "Fun Run"
                val foundEvent = eventList.find { it.contains(query, ignoreCase = true) }
                if (foundEvent != null) {
                    Toast.makeText(this, getString(R.string.search_result_found, foundEvent), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, getString(R.string.search_no_result), Toast.LENGTH_SHORT).show()
                }
            } else {
                // Empty search box -> ask user to type something
                Toast.makeText(this, getString(R.string.search_hint), Toast.LENGTH_SHORT).show()
            }
        }

        // --- Apply Filters Button: Shows what filters user picked ---
        btnApply.setOnClickListener {
            val category = spinnerCategory.selectedItem.toString()
            val location = spinnerLocation.selectedItem.toString()
            // Add date to message only if user picked one
            val filterMsg = "Filtering: $category in $location" + (if (selectedDateTime.isNotEmpty()) " on $selectedDateTime" else "")
            Toast.makeText(this, filterMsg, Toast.LENGTH_LONG).show()
        }

        // --- View Event Buttons: Tap = View, Long-press = Bookmark ---
        // ?. = safe call - if the button is missing in XML, app won't crash
        findViewById<Button>(R.id.button_view_event_1)?.apply {
            setOnClickListener { startActivity(Intent(this@Discover, EventSharing::class.java)) }
            setOnLongClickListener {
                saveBookmark("Community Fun Run")
                true // true = we handled the long-press, don't also trigger click
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

        // --- Bottom Navigation ---
        // Home -> Go to Dashboard, CLEAR_TOP clears back stack so Back doesn't loop
        findViewById<Button>(R.id.nav_home)?.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
            finish()
        }
        // Bookmarks -> Go to Bookmarks
        // NOTE: No listener for nav_discover - we are ALREADY on Discover
        findViewById<Button>(R.id.nav_bookmarks)?.setOnClickListener {
            startActivity(Intent(this, Bookmarks::class.java))
        }
        // Settings -> Go to Settings
        findViewById<Button>(R.id.nav_settings)?.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }
    }

    /**
     * saveBookmark(): Adds event to "Bookmarks" SharedPreferences
     * Called on long-press of a View button.
     */
    private fun saveBookmark(eventName: String) {
        val prefs = getSharedPreferences("Bookmarks", Context.MODE_PRIVATE)
        // FIXED: Copy to new MutableSet - don't edit the set returned by getStringSet() directly
        val savedEvents = prefs.getStringSet("event_set", emptySet())?.toMutableSet() ?: mutableSetOf()
        savedEvents.add(eventName)
        prefs.edit().putStringSet("event_set", savedEvents).apply()
        Toast.makeText(this, getString(R.string.event_bookmarked), Toast.LENGTH_SHORT).show()
    }

    /**
     * applyLocale(): Applies saved language (en / af / zu) from Settings.
     */
    private fun applyLocale() {
        val prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val langCode = prefs.getString("lang", "en") ?: "en"
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
