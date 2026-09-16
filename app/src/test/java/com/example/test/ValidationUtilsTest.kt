package com.example.communityeventssa

import com.example.communityeventssa.utils.ValidationUtils
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationUtilsTest {

    @Test
    fun isNotEmpty_withAllFilled_returnsTrue() {
        assertTrue(ValidationUtils.isNotEmpty("test", "user", "pass"))
    }

    @Test
    fun isNotEmpty_withOneEmpty_returnsFalse() {
        assertFalse(ValidationUtils.isNotEmpty("test", "", "pass"))
    }

    @Test
    fun isNotEmpty_withOnlySpaces_returnsFalse() {
        assertFalse(ValidationUtils.isNotEmpty("   "))
    }
}