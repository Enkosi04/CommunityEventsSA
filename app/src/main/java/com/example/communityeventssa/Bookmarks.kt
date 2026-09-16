package com.example.communityeventssa

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

class Bookmarks : AppCompatActivity() {
    
    private lateinit var container: LinearLayout
    private lateinit var textEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale()
        enableEdgeToEdge()
        setContentView(R.layout.activity_bookmarks)

        container = findViewById(R.id.bookmark_list_container)
        textEmpty = findViewById(R.id.text_empty_bookmarks)

        loadBookmarks()
        
        // Navigation Hooks - ensure IDs match activity_bookmarks.xml
        findViewById<Button>(R.id.nav_home)?.setOnClickListener { 
            val intent = Intent(this, Dashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
        findViewById<Button>(R.id.nav_discover)?.setOnClickListener {
            val intent = Intent(this, Discover::class.java)
            startActivity(intent)
            finish()
        }
        findViewById<Button>(R.id.nav_settings)?.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadBookmarks() {
        container.removeAllViews()
        val prefs = getSharedPreferences("Bookmarks", Context.MODE_PRIVATE)
        val savedEvents = prefs.getStringSet("event_set", mutableSetOf()) ?: mutableSetOf()

        if (savedEvents.isEmpty()) {
            textEmpty.visibility = View.VISIBLE
        } else {
            textEmpty.visibility = View.GONE
            val inflater = LayoutInflater.from(this)
            
            savedEvents.forEach { eventName ->
                val itemView = inflater.inflate(R.layout.item_bookmark, container, false)
                val titleTv = itemView.findViewById<TextView>(R.id.text_bookmark_title)
                val dateTv = itemView.findViewById<TextView>(R.id.text_bookmark_date)
                val removeBtn = itemView.findViewById<Button>(R.id.button_remove_bookmark)
                
                titleTv.text = eventName
                dateTv.text = getString(R.string.saved) 
                
                removeBtn.setOnClickListener {
                    removeBookmark(eventName)
                    loadBookmarks() // Refresh list instantly
                }
                
                container.addView(itemView)
            }
        }
    }

    private fun removeBookmark(eventName: String) {
        val prefs = getSharedPreferences("Bookmarks", Context.MODE_PRIVATE)
        val savedEvents = prefs.getStringSet("event_set", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        savedEvents.remove(eventName)
        prefs.edit().putStringSet("event_set", savedEvents).apply()
        Toast.makeText(this, getString(R.string.event_removed), Toast.LENGTH_SHORT).show()
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
