package com.example.communityeventssa

import com.example.communityeventssa.utils.SecurityUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class SecurityUtilsTest {

    @Test
    fun hashPassword_isConsistent() {
        val password = "mySecurePassword123"
        val hash1 = SecurityUtils.hashPassword(password)
        val hash2 = SecurityUtils.hashPassword(password)
        
        // Hashing the same password should always produce the same result
        assertEquals(hash1, hash2)
    }

    @Test
    fun hashPassword_isDifferentForDifferentPasswords() {
        val password1 = "password123"
        val password2 = "password124"
        val hash1 = SecurityUtils.hashPassword(password1)
        val hash2 = SecurityUtils.hashPassword(password2)
        
        // Different passwords should produce different hashes
        assertNotEquals(hash1, hash2)
    }

    @Test
    fun hashPassword_isNotNullOrEmpty() {
        val hash = SecurityUtils.hashPassword("test")
        assertNotEquals("", hash)
        assertNotEquals(null, hash)
    }
}