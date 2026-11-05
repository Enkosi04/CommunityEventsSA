package com.example.communityeventssa

// Imports
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

/**
 * File: Bookmarks.kt
 * Screen: Bookmarks / Saved screen
 * Purpose: Shows all events the user saved/bookmarked.
 * How it works: Saved events are stored in SharedPreferences called "Bookmarks"
 * under the key "event_set" as a Set of event names.
 * If the set is empty -> shows "No bookmarks yet" message.
 * If not empty -> inflates one item_bookmark.xml card per event.
 */
class Bookmarks : AppCompatActivity() {

    // container = the empty LinearLayout in activity_bookmarks.xml where cards are added
    private lateinit var container: LinearLayout
    // textEmpty = the "No bookmarks" TextView, shown only when list is empty
    private lateinit var textEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Apply saved language (en / af / zu) BEFORE showing the screen
        applyLocale()
        enableEdgeToEdge()
        setContentView(R.layout.activity_bookmarks)

        // Link Kotlin variables to views in XML by their IDs
        container = findViewById(R.id.bookmark_list_container)
        textEmpty = findViewById(R.id.text_empty_bookmarks)

        // Load saved bookmarks from storage and display them
        loadBookmarks()

        // --- Bottom Navigation ---
        // Home button -> Go to Dashboard, clear stack so Back doesn't loop
        findViewById<Button>(R.id.nav_home)?.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
        // Discover button -> Go to Discover screen
        findViewById<Button>(R.id.nav_discover)?.setOnClickListener {
            startActivity(Intent(this, Discover::class.java))
            finish()
        }
        // Settings button -> Go to Settings screen
        // NOTE: No listener for nav_bookmarks because we are ALREADY on this screen
        findViewById<Button>(R.id.nav_settings)?.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
            finish()
        }
    }

    /**
     * loadBookmarks(): Reads saved events and builds the list on screen.
     */
    private fun loadBookmarks() {
        // Clear old cards first, so we don't duplicate them on refresh
        container.removeAllViews()

        // Open SharedPreferences file "Bookmarks" in private mode (only this app can read it)
        val prefs = getSharedPreferences("Bookmarks", Context.MODE_PRIVATE)
        // FIXED: Copy to a new set with toMutableSet() - getStringSet() returns a live reference,
        // editing it directly can crash / corrupt data
        val savedEvents = prefs.getStringSet("event_set", emptySet())?.toSet() ?: emptySet()

        if (savedEvents.isEmpty()) {
            // No bookmarks -> show empty message
            textEmpty.visibility = View.VISIBLE
        } else {
            // Has bookmarks -> hide empty message
            textEmpty.visibility = View.GONE
            val inflater = LayoutInflater.from(this)

            // Loop through each saved event name and make a card for it
            savedEvents.forEach { eventName ->
                // Inflate = turn item_bookmark.xml into a real View we can add
                val itemView = inflater.inflate(R.layout.item_bookmark, container, false)
                val titleTv = itemView.findViewById<TextView>(R.id.text_bookmark_title)
                val dateTv = itemView.findViewById<TextView>(R.id.text_bookmark_date)
                val removeBtn = itemView.findViewById<Button>(R.id.button_remove_bookmark)

                // Fill the card with data
                titleTv.text = eventName
                dateTv.text = getString(R.string.saved)

                // Remove button -> delete this event, then reload list instantly
                removeBtn.setOnClickListener {
                    removeBookmark(eventName)
                    loadBookmarks() // Refresh list instantly
                }

                // Add the finished card to the container on screen
                container.addView(itemView)
            }
        }
    }

    /**
     * removeBookmark(): Deletes one event from SharedPreferences.
     */
    private fun removeBookmark(eventName: String) {
        val prefs = getSharedPreferences("Bookmarks", Context.MODE_PRIVATE)
        // Get current set, make a copy we can edit safely
        val savedEvents = prefs.getStringSet("event_set", emptySet())?.toMutableSet() ?: mutableSetOf()
        savedEvents.remove(eventName)
        // Save the updated set back to storage. apply() = saves in background, no freeze.
        prefs.edit().putStringSet("event_set", savedEvents).apply()
        Toast.makeText(this, getString(R.string.event_removed), Toast.LENGTH_SHORT).show()
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
}
