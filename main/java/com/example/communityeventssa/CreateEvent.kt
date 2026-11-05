package com.example.communityeventssa

// Imports: Date/Time pickers, UI widgets, AppCompat, Retrofit for API calls
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.example.communityeventssa.network.ApiResponse
import com.example.communityeventssa.network.Event
import com.example.communityeventssa.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Locale

/**
 * File: CreateEvent.kt
 * Screen: Create Event
 * Purpose: Lets the user create a new event.
 * Flow: User fills title + description + location + picks date/time -> taps Save
 * -> Event is saved to Bookmarks locally AND sent to the server via Retrofit API.
 * If server is offline, event is still kept locally so it is not lost.
 */
class CreateEvent : AppCompatActivity() {

    // Stores the date + time the user picks, e.g. "12/09/2026 08:00"
    // Empty until user picks a date
    private var selectedDateTimeString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Apply saved language + dark mode BEFORE showing the screen
        applyAppSettings()
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_event)

        // Link Kotlin variables to views in activity_create_event.xml
        val etTitle = findViewById<EditText>(R.id.input_title)
        val etDescription = findViewById<EditText>(R.id.input_description)
        val etLocation = findViewById<EditText>(R.id.input_location)
        val btnSave = findViewById<Button>(R.id.button_save)
        val btnDate = findViewById<Button>(R.id.button_date)

        // --- Date Picker Button ---
        // Opens DatePicker first, then TimePicker, then saves the result
        btnDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Show calendar dialog. _ = unused view parameter
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                // After date is picked, show clock dialog (24-hour format = true)
                TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                    // Format as DD/MM/YYYY HH:MM, e.g. 12/09/2026 08:00
                    // +1 because Calendar months start at 0 (Jan = 0)
                    selectedDateTimeString = String.format(
                        Locale.getDefault(),
                        "%02d/%02d/%04d %02d:%02d",
                        selectedDay, selectedMonth + 1, selectedYear, selectedHour, selectedMinute
                    )
                    // Show picked date on the button itself
                    btnDate.text = selectedDateTimeString
                    Toast.makeText(this, "Selected: $selectedDateTimeString", Toast.LENGTH_SHORT).show()
                }, hour, minute, true).show()
            }, year, month, day).show()
        }

        // --- Save Button ---
        btnSave.setOnClickListener {
            // .trim() removes spaces at start/end so "   " doesn't count as filled
            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val location = etLocation.text.toString().trim()

            // Validation: All 3 fields must be filled
            if (title.isNotEmpty() && description.isNotEmpty() && location.isNotEmpty()) {
                // If user didn't pick a date, use a default date so API doesn't crash
                val finalDate = selectedDateTimeString.ifEmpty { "12/09/2026 08:00" }
                val event = Event(title = title, description = description, date = finalDate, location = location)

                // Save to Bookmarks locally first, so event is kept even if server is offline
                saveToBookmarks(title)

                // Send event to server via Retrofit (async - runs in background)
                RetrofitClient.instance.createEvent(event).enqueue(object : Callback<ApiResponse> {
                    // Called when server replies (even if it says error)
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@CreateEvent, "Event Saved and Bookmarked!", Toast.LENGTH_SHORT).show()
                            finish() // Close this screen, go back
                        } else {
                            Toast.makeText(this@CreateEvent, "Failed to save event on server", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                    // Called when there is NO internet / server is down
                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Toast.makeText(this@CreateEvent, "Event Bookmarked Locally (Server Offline)", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                })
            } else {
                // One or more fields empty -> tell user to fill all fields
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * saveToBookmarks(): Adds this event title to the "Bookmarks" SharedPreferences
     * so it shows up on the Bookmarks screen even if the server is offline.
     */
    private fun saveToBookmarks(eventName: String) {
        val prefs = getSharedPreferences("Bookmarks", MODE_PRIVATE)
        // FIXED: Copy to new MutableSet - getStringSet() returns a live reference, don't edit it directly
        val savedEvents = prefs.getStringSet("event_set", emptySet())?.toMutableSet() ?: mutableSetOf()
        savedEvents.add(eventName)
        // prefs.edit { } = Kotlin extension, saves automatically with apply()
        prefs.edit {
            putStringSet("event_set", savedEvents)
        }
    }

    /**
     * applyAppSettings(): Applies saved language + dark mode from Settings.
     */
    private fun applyAppSettings() {
        val prefs = getSharedPreferences("Settings", MODE_PRIVATE)
        val langCode = prefs.getString("lang", "en") ?: "en"
        val locale = Locale.forLanguageTag(langCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)

        // Dark mode: pref_dark = true -> dark, false -> light
        val isDarkMode = prefs.getBoolean("pref_dark", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
