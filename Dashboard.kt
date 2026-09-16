package com.example.communityeventssa

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale()
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        // Quick Actions
        findViewById<Button>(R.id.button_discover).setOnClickListener {
            startActivity(Intent(this, Discover::class.java))
        }

        findViewById<Button>(R.id.button_bookmarks).setOnClickListener {
            startActivity(Intent(this, Bookmarks::class.java))
        }

        findViewById<Button>(R.id.button_create_event).setOnClickListener {
            startActivity(Intent(this, CreateEvent::class.java))
        }

        // Event View Buttons
        findViewById<Button>(R.id.button_view_event_1).setOnClickListener {
            startActivity(Intent(this, EventSharing::class.java))
        }

        findViewById<Button>(R.id.button_view_event_2).setOnClickListener {
            startActivity(Intent(this, EventSharing::class.java))
        }

        // Grid Bottom Navigation Bar Hooks
        findViewById<Button>(R.id.nav_home).setOnClickListener {
            Toast.makeText(this, getString(R.string.home), Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.nav_discover).setOnClickListener {
            startActivity(Intent(this, Discover::class.java))
        }

        findViewById<Button>(R.id.nav_bookmarks).setOnClickListener {
            startActivity(Intent(this, Bookmarks::class.java))
        }

        findViewById<Button>(R.id.nav_settings).setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }

        // Search Bar click listener
        findViewById<LinearLayout>(R.id.search_bar).setOnClickListener {
            startActivity(Intent(this, Discover::class.java))
        }
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

    override fun onResume() {
        super.onResume()
        // Check if language changed in settings
        val prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val langCode = prefs.getString("lang", "en") ?: "en"
        if (Locale.getDefault().language != langCode) {
            recreate()
        }
    }
}