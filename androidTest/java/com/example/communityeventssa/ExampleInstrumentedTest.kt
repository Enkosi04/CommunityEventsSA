package com.example.communityeventssa

// Imports: Tools needed for instrumented tests
// InstrumentationRegistry = gives us access to the real app running on a phone/emulator
// AndroidJUnit4 = the test runner that runs tests on an Android device
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

// JUnit imports: @Test marks a function as a test, @RunWith tells JUnit how to run it
import org.junit.Test
import org.junit.runner.RunWith

// Assert imports: assertEquals checks if two values are the same
import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 *
 * Purpose: This is a basic sanity test for the CommunityEventsSA app.
 * It checks that the app installs correctly and that the package name is correct.
 * Unlike local unit tests, this test runs ON a phone / emulator, not on your laptop.
 * File location: app/src/androidTest/java/com/example/communityeventssa/ExampleInstrumentedTest.kt
 */

// @RunWith tells JUnit to use AndroidJUnit4 to run this test on a device
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    // @Test marks this function as a test case that Android Studio will run
    @Test
    fun useAppContext() {
        // Get the Context of the app under test
        // Context = gives access to app info like package name, resources, etc.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // Check: The installed app's package name should be "com.example.communityeventssa"
        // If this fails, it means the app ID in build.gradle was changed or the wrong app is installed
        assertEquals("com.example.communityeventssa", appContext.packageName)
    }
}
