package com.example.communityeventssa

// Imports
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

/**
 * File: Dashboard.kt
 * Screen: Dashboard / Home screen - the first screen after Login
 * Purpose: Home hub for CommunityEventsSA.
 * From here user can go to: Discover, Bookmarks, Create Event, Event Details, Settings.
 * Also checks if language was changed in Settings and refreshes if needed.
 */
class Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Apply saved language (en / af / zu) BEFORE showing the screen
        applyLocale()
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        // --- Quick Actions: Big buttons in the middle of the Dashboard ---
        // Discover button -> Go to Discover / Search events screen
        findViewById<Button>(R.id.button_discover).setOnClickListener {
            startActivity(Intent(this, Discover::class.java))
        }

        // Bookmarks button -> Go to Saved / Bookmarks screen
        findViewById<Button>(R.id.button_bookmarks).setOnClickListener {
            startActivity(Intent(this, Bookmarks::class.java))
        }

        // Create Event button -> Go to Create Event screen
        findViewById<Button>(R.id.button_create_event).setOnClickListener {
            startActivity(Intent(this, CreateEvent::class.java))
        }

        // --- Event View Buttons: "View" on the featured event cards ---
        // Event 1 -> Go to EventSharing (details / share screen)
        // TODO: Later pass the real event ID with intent.putExtra("event_id", ...)
        findViewById<Button>(R.id.button_view_event_1).setOnClickListener {
            startActivity(Intent(this, EventSharing::class.java))
        }

        // Event 2 -> Go to EventSharing (details / share screen)
        findViewById<Button>(R.id.button_view_event_2).setOnClickListener {
            startActivity(Intent(this, EventSharing::class.java))
        }

        // --- Bottom Navigation Bar: 4 buttons at the bottom ---
        // Home button -> We are ALREADY on Home, so just show a Toast
        // FIXED: Before it did nothing clear, now it tells user "Home"
        findViewById<Button>(R.id.nav_home).setOnClickListener {
            Toast.makeText(this, getString(R.string.home), Toast.LENGTH_SHORT).show()
        }

        // Discover button -> Go to Discover
        findViewById<Button>(R.id.nav_discover).setOnClickListener {
            startActivity(Intent(this, Discover::class.java))
        }

        // Bookmarks button -> Go to Bookmarks
        findViewById<Button>(R.id.nav_bookmarks).setOnClickListener {
            startActivity(Intent(this, Bookmarks::class.java))
        }

        // Settings button -> Go to Settings
        findViewById<Button>(R.id.nav_settings).setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }

        // --- Search Bar: Tapping the search bar opens Discover ---
        // The search bar on Dashboard is just a shortcut, real search is in Discover
        findViewById<LinearLayout>(R.id.search_bar).setOnClickListener {
            startActivity(Intent(this, Discover::class.java))
        }
    }

    /**
     * applyLocale(): Applies the language saved in Settings (en / af / zu).
     * Called in onCreate() so the screen shows in the correct language.
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

    /**
     * onResume(): Called every time user comes BACK to this screen.
     * Checks if language was changed in Settings.
     * If yes, recreate() = reloads the screen in the new language.
     */
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
