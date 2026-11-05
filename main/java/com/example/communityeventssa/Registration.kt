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
import com.example.communityeventssa.network.ApiResponse
import com.example.communityeventssa.network.RetrofitClient
import com.example.communityeventssa.network.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

/**
 * File: Registration.kt
 * Screen: Registration / Sign Up
 * Purpose: Lets a new user create an account.
 * Flow: User fills username + email + password -> taps Register
 * -> Password is hashed with SHA-256 -> User sent to server via Retrofit API
 * -> If success -> Go to Login screen
 * -> If server offline -> Still goes to Login for POE demo, shows warning
 */
class Registration : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration)

        // Link Kotlin variables to views in activity_registration.xml
        val etUsername = findViewById<EditText>(R.id.et_reg_username)
        val etEmail = findViewById<EditText>(R.id.et_reg_email)
        val etPassword = findViewById<EditText>(R.id.et_reg_password)
        val btnRegister = findViewById<Button>(R.id.btn_register)
        val tvGotoLogin = findViewById<TextView>(R.id.tv_goto_login)

        // Edge-to-edge: Makes content not hide behind status bar / nav bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- Register Button ---
        btnRegister.setOnClickListener {
            // .trim() removes spaces so "   " doesn't count as filled
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validation: All 3 fields must be filled
            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {

                // FIXED: Email format check - must look like name@example.com
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // FIXED: Password length check - at least 6 chars for security
                if (password.length < 6) {
                    Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Hash password with SHA-256 - never send / store plain password
                val encryptedPassword = hashPassword(password)
                // Create User object to send to API (username, email, hashed password)
                val user = User(username = username, email = email, password = encryptedPassword)

                // Prevent double-tap: Disable button while waiting for server
                btnRegister.isEnabled = false

                // Send User to server via Retrofit (async - runs in background)
                RetrofitClient.instance.registerUser(user).enqueue(object : Callback<ApiResponse> {
                    // Called when server replies (even if it says error)
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        btnRegister.isEnabled = true // Re-enable button
                        if (response.isSuccessful) {
                            Toast.makeText(this@Registration, "Registration Successful!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@Registration, Login::class.java))
                            finish() // Close Register so Back doesn't come back here
                        } else {
                            Toast.makeText(this@Registration, "Registration Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    // Called when NO internet / server is down
                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        btnRegister.isEnabled = true // Re-enable button
                        // For demonstration purposes, if it fails (e.g. no server), we still allow transition
                        Toast.makeText(this@Registration, "Network Error: ${t.message}. Continuing anyway for demo.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@Registration, Login::class.java))
                        finish()
                    }
                })
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // --- "Already have an account? Login" link ---
        tvGotoLogin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
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
