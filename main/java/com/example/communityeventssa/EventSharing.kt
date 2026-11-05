package com.example.communityeventssa

// Imports: Clipboard for copy link, Intent/Uri for sharing, UI widgets
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

/**
 * File: EventSharing.kt
 * Screen: Event Sharing / Event Details - Share screen
 * Purpose: Lets user share an event with friends via:
 * 1. Generic Share (any app - WhatsApp, SMS, etc.)
 * 2. Copy Link (copies event URL to clipboard)
 * 3. WhatsApp direct, Facebook, Email
 * User can also type a custom message to send with the event.
 */
class EventSharing : AppCompatActivity() {

    // Event link - hardcoded for POE demo. In real app this would come from Intent extras / API.
    private val eventLink = "https://communityeventssa.com/event/123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.content_event_sharing)

        // Link Kotlin variables to views in content_event_sharing.xml
        // ?. used later = safe call, won't crash if view is missing
        val btnBack = findViewById<ImageButton>(R.id.button_back)
        val btnShareEvent = findViewById<Button>(R.id.button_share_event)
        val btnCopyLink = findViewById<Button>(R.id.button_copy_link)
        val btnDone = findViewById<Button>(R.id.button_done)
        val inputMessage = findViewById<EditText>(R.id.input_share_message)

        // Share option rows (whole row is clickable)
        val shareWhatsapp = findViewById<LinearLayout>(R.id.share_whatsapp)
        val shareFacebook = findViewById<LinearLayout>(R.id.share_facebook)
        val shareEmail = findViewById<LinearLayout>(R.id.share_email)

        // Back button (top left arrow) -> Close this screen, go back
        btnBack?.setOnClickListener {
            finish()
        }

        // Done button -> Close this screen, go back
        btnDone?.setOnClickListener {
            finish()
        }

        // --- Main Share Button: Opens Android share sheet ---
        btnShareEvent?.setOnClickListener {
            // Get custom message user typed, or "" if empty. ?. = safe if EditText missing
            val message = inputMessage?.text?.toString()?.trim() ?: ""
            // If user typed nothing, use a default message
            val finalMessage = message.ifEmpty { "Check out this Community Event!" }
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain" // We are sharing text only
            shareIntent.putExtra(Intent.EXTRA_TEXT, "$finalMessage\nCheck out this event: $eventLink")
            // createChooser = shows "Share event via..." popup with all apps
            startActivity(Intent.createChooser(shareIntent, "Share event via"))
        }

        // --- Copy Link Button: Copies event URL to clipboard ---
        btnCopyLink?.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Event Link", eventLink)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        // --- WhatsApp Row: Opens WhatsApp with message pre-filled ---
        shareWhatsapp?.setOnClickListener {
            val message = inputMessage?.text?.toString()?.trim() ?: ""
            val finalMessage = message.ifEmpty { "Check out this Community Event! $eventLink" }
            val intent = Intent(Intent.ACTION_VIEW)
            // Uri.encode() = converts spaces / special chars so link doesn't break
            intent.data = Uri.parse("https://api.whatsapp.com/send?text=" + Uri.encode(finalMessage))
            // FIXED: Check if WhatsApp / browser exists before opening, else app would crash
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "WhatsApp / Browser not found", Toast.LENGTH_SHORT).show()
            }
        }

        // --- Facebook Row: Placeholder for POE ---
        shareFacebook?.setOnClickListener {
            Toast.makeText(this, "Opening Facebook...", Toast.LENGTH_SHORT).show()
            // TODO: Add Facebook Share SDK / open facebook.com/sharer link later
        }

        // --- Email Row: Opens email app with subject + message ---
        shareEmail?.setOnClickListener {
            val message = inputMessage?.text?.toString()?.trim() ?: ""
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:") // mailto: = only email apps will show
                putExtra(Intent.EXTRA_SUBJECT, "Check out this Community Event")
                putExtra(Intent.EXTRA_TEXT, "$message\n$eventLink")
            }
            // FIXED: Check if email app exists before opening
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
