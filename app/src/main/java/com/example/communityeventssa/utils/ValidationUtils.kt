package com.example.communityeventssa.utils

object ValidationUtils {
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isNotEmpty(vararg strings: String): Boolean {
        return strings.all { it.isNotBlank() }
    }
}