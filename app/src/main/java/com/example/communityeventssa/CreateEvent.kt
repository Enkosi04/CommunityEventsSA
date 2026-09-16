package com.example.communityeventssa

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

class CreateEvent : AppCompatActivity() {
    private var selectedDateTimeString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyAppSettings()
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_event)

        val etTitle = findViewById<EditText>(R.id.input_title)
        val etDescription = findViewById<EditText>(R.id.input_description)
        val etLocation = findViewById<EditText>(R.id.input_location)
        val btnSave = findViewById<Button>(R.id.button_save)
        val btnDate = findViewById<Button>(R.id.button_date)

        btnDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                    selectedDateTimeString = String.format(Locale.getDefault(), "%02d/%02d/%04d %02d:%02d", selectedDay, selectedMonth + 1, selectedYear, selectedHour, selectedMinute)
                    btnDate.text = selectedDateTimeString
                    Toast.makeText(this, "Selected: $selectedDateTimeString", Toast.LENGTH_SHORT).show()
                }, hour, minute, true).show()
            }, year, month, day).show()
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val location = etLocation.text.toString().trim()

            if (title.isNotEmpty() && description.isNotEmpty() && location.isNotEmpty()) {
                val finalDate = selectedDateTimeString.ifEmpty { "12/09/2026 08:00" }
                val event = Event(title = title, description = description, date = finalDate, location = location)
                
                // Save to bookmarks locally as requested
                saveToBookmarks(title)

                RetrofitClient.instance.createEvent(event).enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@CreateEvent, "Event Saved and Bookmarked!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@CreateEvent, "Failed to save event on server", Toast.LENGTH_SHORT).show()
                            finish() 
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Toast.makeText(this@CreateEvent, "Event Bookmarked Locally (Server Offline)", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                })
            } else {
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveToBookmarks(eventName: String) {
        val prefs = getSharedPreferences("Bookmarks", MODE_PRIVATE)
        val savedEvents = prefs.getStringSet("event_set", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        savedEvents.add(eventName)
        prefs.edit {
            putStringSet("event_set", savedEvents)
        }
    }

    private fun applyAppSettings() {
        val prefs = getSharedPreferences("Settings", MODE_PRIVATE)
        val langCode = prefs.getString("lang", "en") ?: "en"
        val locale = Locale.forLanguageTag(langCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        val isDarkMode = prefs.getBoolean("pref_dark", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}