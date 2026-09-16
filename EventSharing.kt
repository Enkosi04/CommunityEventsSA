package com.example.communityeventssa

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

class EventSharing : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.content_event_sharing)

        val btnBack = findViewById<ImageButton>(R.id.button_back)
        val btnShareEvent = findViewById<Button>(R.id.button_share_event)
        val btnCopyLink = findViewById<Button>(R.id.button_copy_link)
        val btnDone = findViewById<Button>(R.id.button_done)
        val inputMessage = findViewById<EditText>(R.id.input_share_message)

        val shareWhatsapp = findViewById<LinearLayout>(R.id.share_whatsapp)
        val shareFacebook = findViewById<LinearLayout>(R.id.share_facebook)
        val shareEmail = findViewById<LinearLayout>(R.id.share_email)

        btnBack?.setOnClickListener {
            finish()
        }

        btnDone?.setOnClickListener {
            finish()
        }

        btnShareEvent?.setOnClickListener {
            val message = inputMessage?.text?.toString() ?: ""
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "$message\nCheck out this event: https://communityeventssa.com/event/123")
            startActivity(Intent.createChooser(shareIntent, "Share event via"))
        }

        btnCopyLink?.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Event Link", "https://communityeventssa.com/event/123")
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        shareWhatsapp?.setOnClickListener {
            val message = inputMessage?.text?.toString() ?: ""
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://api.whatsapp.com/send?text=" + Uri.encode(message))
            startActivity(intent)
        }

        shareFacebook?.setOnClickListener {
            Toast.makeText(this, "Opening Facebook...", Toast.LENGTH_SHORT).show()
        }

        shareEmail?.setOnClickListener {
            val message = inputMessage?.text?.toString() ?: ""
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_SUBJECT, "Check out this Community Event")
                putExtra(Intent.EXTRA_TEXT, message)
            }
            startActivity(intent)
        }
    }
}