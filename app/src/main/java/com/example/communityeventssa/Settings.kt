package com.example.communityeventssa

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyAppSettings()
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        // Profile Fields
        val inputName = findViewById<EditText>(R.id.input_name)
        val inputSurname = findViewById<EditText>(R.id.input_surname)
        val inputUsername = findViewById<EditText>(R.id.input_username_settings)
        val inputEmail = findViewById<EditText>(R.id.input_email)
        val inputPhone = findViewById<EditText>(R.id.input_phone)
        val inputAddress = findViewById<EditText>(R.id.input_address)
        val inputBio = findViewById<EditText>(R.id.input_bio)
        val spinnerGender = findViewById<Spinner>(R.id.spinner_gender)

        // Interests CheckBoxes
        val checkSports = findViewById<CheckBox>(R.id.check_sports)
        val checkEducation = findViewById<CheckBox>(R.id.check_education)
        val checkMusic = findViewById<CheckBox>(R.id.check_music)
        val checkBusiness = findViewById<CheckBox>(R.id.check_business)
        val checkVolunteering = findViewById<CheckBox>(R.id.check_volunteering)
        val checkSocial = findViewById<CheckBox>(R.id.check_social)
        val checkTech = findViewById<CheckBox>(R.id.check_technology)
        val checkHealth = findViewById<CheckBox>(R.id.check_health)
        val checkEnvironment = findViewById<CheckBox>(R.id.check_environment)

        // App Preferences Switches
        val switchNotifications = findViewById<Switch>(R.id.switch_notifications)
        val switchDarkMode = findViewById<Switch>(R.id.switch_dark_mode)
        val switchPrivate = findViewById<Switch>(R.id.switch_private_profile)
        val switchDataSaver = findViewById<Switch>(R.id.switch_data_saver)

        // Language Spinner
        val spinnerLanguage = findViewById<Spinner>(R.id.spinner_language)

        // Setup Genders
        val genders = arrayOf("Prefer not to say", "Male", "Female", "Other")
        spinnerGender.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genders)

        // Setup Languages
        val languages = arrayOf("English", "IsiZulu")
        spinnerLanguage.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)

        // Load Saved Data
        val prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        inputName.setText(prefs.getString("name", ""))
        inputSurname.setText(prefs.getString("surname", ""))
        inputUsername.setText(prefs.getString("username", ""))
        inputEmail.setText(prefs.getString("email", ""))
        inputPhone.setText(prefs.getString("phone", ""))
        inputAddress.setText(prefs.getString("address", ""))
        inputBio.setText(prefs.getString("bio", ""))
        spinnerGender.setSelection(prefs.getInt("gender", 0))

        checkSports.isChecked = prefs.getBoolean("interest_sports", false)
        checkEducation.isChecked = prefs.getBoolean("interest_education", false)
        checkMusic.isChecked = prefs.getBoolean("interest_music", false)
        checkBusiness.isChecked = prefs.getBoolean("interest_business", false)
        checkVolunteering.isChecked = prefs.getBoolean("interest_volunteering", false)
        checkSocial.isChecked = prefs.getBoolean("interest_social", false)
        checkTech.isChecked = prefs.getBoolean("interest_tech", false)
        checkHealth.isChecked = prefs.getBoolean("interest_health", false)
        checkEnvironment.isChecked = prefs.getBoolean("interest_environment", false)

        switchNotifications.isChecked = prefs.getBoolean("pref_notif", true)
        switchDarkMode.isChecked = prefs.getBoolean("pref_dark", false)
        switchPrivate.isChecked = prefs.getBoolean("pref_private", false)
        switchDataSaver.isChecked = prefs.getBoolean("pref_data", false)

        val currentLang = prefs.getString("lang", "en")
        spinnerLanguage.setSelection(if (currentLang == "zu") 1 else 0)

        // Help & Feedback Functionality
        findViewById<TextView>(R.id.text_help_center)?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://support.google.com/"))
            startActivity(intent)
        }

        findViewById<TextView>(R.id.text_report_problem)?.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:support@communityeventssa.co.za")
                putExtra(Intent.EXTRA_SUBJECT, "Report a Problem")
            }
            try {
                startActivity(Intent.createChooser(emailIntent, "Send email..."))
            } catch (ex: Exception) {
                Toast.makeText(this, "No email client found", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<TextView>(R.id.text_send_feedback)?.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:feedback@communityeventssa.co.za")
                putExtra(Intent.EXTRA_SUBJECT, "App Feedback")
            }
            try {
                startActivity(Intent.createChooser(emailIntent, "Send feedback..."))
            } catch (ex: Exception) {
                Toast.makeText(this, "No email client found", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<TextView>(R.id.text_privacy_policy)?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/policies/privacy/"))
            startActivity(intent)
        }

        // Dark Mode Logic - Apply immediately when toggled
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            prefs.edit().putBoolean("pref_dark", isChecked).apply()
        }

        val btnSave = findViewById<Button>(R.id.button_save)
        val btnLogout = findViewById<Button>(R.id.button_logout)

        btnSave.setOnClickListener {
            val selectedLang = if (spinnerLanguage.selectedItemPosition == 1) "zu" else "en"
            
            prefs.edit().apply {
                putString("name", inputName.text.toString())
                putString("surname", inputSurname.text.toString())
                putString("username", inputUsername.text.toString())
                putString("email", inputEmail.text.toString())
                putString("phone", inputPhone.text.toString())
                putString("address", inputAddress.text.toString())
                putString("bio", inputBio.text.toString())
                putInt("gender", spinnerGender.selectedItemPosition)

                putBoolean("interest_sports", checkSports.isChecked)
                putBoolean("interest_education", checkEducation.isChecked)
                putBoolean("interest_music", checkMusic.isChecked)
                putBoolean("interest_business", checkBusiness.isChecked)
                putBoolean("interest_volunteering", checkVolunteering.isChecked)
                putBoolean("interest_social", checkSocial.isChecked)
                putBoolean("interest_tech", checkTech.isChecked)
                putBoolean("interest_health", checkHealth.isChecked)
                putBoolean("interest_environment", checkEnvironment.isChecked)

                putBoolean("pref_notif", switchNotifications.isChecked)
                putBoolean("pref_dark", switchDarkMode.isChecked)
                putBoolean("pref_private", switchPrivate.isChecked)
                putBoolean("pref_data", switchDataSaver.isChecked)

                putString("lang", selectedLang)
            }.apply()
            
            setLocale(selectedLang)
            Toast.makeText(this, getString(R.string.save), Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setLocale(langCode: String) {
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        
        val intent = Intent(this, Dashboard::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finishAffinity() 
    }

    private fun applyAppSettings() {
        val prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val langCode = prefs.getString("lang", "en") ?: "en"
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        val isDarkMode = prefs.getBoolean("pref_dark", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}