package com.example.communityeventssa

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        // Delay for 3 seconds then go to Registration safely without any potential view crashes
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, Registration::class.java))
            finish()
        }, 3000)
    }
}