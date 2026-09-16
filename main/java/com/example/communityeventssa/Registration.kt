package com.example.communityeventssa

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

class Registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration)
        
        val etUsername = findViewById<EditText>(R.id.et_reg_username)
        val etEmail = findViewById<EditText>(R.id.et_reg_email)
        val etPassword = findViewById<EditText>(R.id.et_reg_password)
        val btnRegister = findViewById<Button>(R.id.btn_register)
        val tvGotoLogin = findViewById<TextView>(R.id.tv_goto_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val encryptedPassword = hashPassword(password)
                val user = User(username = username, email = email, password = encryptedPassword)
                
                RetrofitClient.instance.registerUser(user).enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@Registration, "Registration Successful!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@Registration, Login::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@Registration, "Registration Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
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

        tvGotoLogin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}