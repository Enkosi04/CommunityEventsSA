package com.example.communityeventssa

// Imports
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.security.MessageDigest

/**
 * File: Login.kt
 * Screen: Login - first screen user sees (after Splash if you have one)
 * Purpose: Lets user log in with email + password, then goes to Dashboard.
 * POE Note: This is a DEMO login - it does NOT check against a real database yet.
 * It only checks that fields are not empty, hashes the password, then lets user in.
 * In a real app, you would send email + hashed password to a REST API / Firebase.
 */
class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Link Kotlin variables to views in activity_login.xml
        val etEmail = findViewById<EditText>(R.id.et_login_email)
        val etPassword = findViewById<EditText>(R.id.et_login_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val tvGotoRegister = findViewById<TextView>(R.id.tv_goto_register)

        // Edge-to-edge: Makes content not hide behind status bar / nav bar
        // Adds padding equal to system bars (top status bar, bottom nav bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- Login Button ---
        btnLogin.setOnClickListener {
            // .trim() removes spaces so "   " doesn't count as filled
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validation: Both fields must be filled
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // FIXED: Simple email check - must contain @ and .
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener // Stop here, don't log in
                }

                // Hash password with SHA-256 before using it - never store plain password
                // NOTE: Variable is kept for POE demo, in real app you'd send it to server
                val encryptedPassword = hashPassword(password)

                // In a real app, you'd check these credentials against a database via REST API
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                // Navigate to Dashboard + finish() so Back button doesn't go back to Login
                startActivity(Intent(this, Dashboard::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        // --- "Don't have an account? Register" link ---
        tvGotoRegister.setOnClickListener {
            startActivity(Intent(this, Registration::class.java))
        }
    }

    /**
     * hashPassword(): Turns password into a SHA-256 hash (one-way, can't be reversed).
     * Example: "1234" -> "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4"
     * Used so we never store / send the plain password.
     */
    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        // fold = loops through each byte, converts to 2-digit hex, joins into one string
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}
