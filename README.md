Community Events SA

An Android app that helps South African communities discover, create, save and share local events like fun runs, food drives, clean ups, workshops and sports days.

Built for: POE Mobile App Development
Language: Kotlin
UI: XML
Build: Gradle
Min SDK: Check build.gradle.kts

Features

1. Login and Registration
Register with username, email and password
Password is hashed with SHA 256 before it is sent. Plain passwords are never stored or sent
Email format plus password length min 6 chars validation
Registration uses Retrofit to send the user to the API RetrofitClient, User, ApiResponse
If the server is offline, app shows a network error and continues to Login for demo purposes
Login validates email plus password, then goes to Dashboard

2. Dashboard Home
Welcome header, search bar shortcut, quick actions
Recommended events: Community Fun Run, Community Food Drive
Create Event button
Bottom navigation: Home, Discover, Saved, Settings

3. Discover Search and Filter
Search bar searches by event name, case insensitive. Example: fun run finds Community Fun Run
Filter by Category: All, Sports, Education, Music, Business, Volunteering
Filter by Location: All, Durban, Johannesburg, Cape Town, Pretoria
Filter by Date plus Time: DatePicker then TimePicker, shown as DD/MM/YYYY HH:MM
Tap View Event equals open event details and sharing
Long press View Event equals bookmark event

4. Create Event
Fields: Title, Description, Date/Time, Location
Date/Time picker
Validation: all fields must be filled
Saves event and returns to previous screen

5. Event Sharing
Custom message box with default: Check out this community event!
Share Event opens Android share sheet WhatsApp, SMS, any app
Copy Event Link copies https://communityeventssa.com/event/123 to clipboard
WhatsApp opens WhatsApp with message pre filled
Facebook placeholder for now, shows Toast
Email opens email app with subject plus message plus link
Done / Back closes screen
Crash safe: checks if WhatsApp / Email app exists before opening

6. Bookmarks / Saved Events
Saved with SharedPreferences Bookmarks then event_set
Long press any event on Discover to bookmark it
List shows Title plus Date per bookmark
Remove button deletes that bookmark

7. Settings / Language
Language saved in SharedPreferences Settings then lang
Supported: English en, Afrikaans af, isiZulu zu
applyLocale applied on Dashboard / Discover so language changes everywhere

8. Android CI
.github/workflows/android.yml runs on every push / PR to main / master
Steps: Checkout, Setup JDK 17 Temurin, Grant gradlew permission, gradlew build with stacktrace, gradlew test
Connected tests run with or true because there is no emulator on GitHub Actions

How To Run

1. Clone the repo:
git clone https://github.com/<your username>/CommunityEventsSA.git

Project Structurejavascriptapp/src/main/java/com/example/communityeventssa/
├── Login.kt
├── Registration.kt
├── Dashboard.kt
├── Discover.kt
├── CreateEvent.kt
├── EventSharing.kt
├── Bookmarks.kt
├── Settings.kt
└── network/
    ├── RetrofitClient.kt
    ├── ApiService.kt
    ├── User.kt
    └── ApiResponse.kt

app/src/main/res/layout/
├── activity_login.xml
├── activity_registration.xml
├── activity_dashboard.xml
├── activity_discover.xml
├── activity_create_event.xml
├── content_event_sharing.xml
└── item_bookmark.xml

.github/workflows/android.ymlRelease Notes - v2.0 (Final POE Version)

This is the release after the prototype. These are the updates made since the prototype:
What Changed Since Prototype
From static prototype -> working app:
Prototype had static screens with no logic. This version has full navigation, validation, saving and sharing.All screens now connected: Login <-> Registration <-> Dashboard <-> Discover <-> Create Event <-> Event Sharing <-> Bookmarks <-> SettingsInnovative Features Added In This Version
Smart Search + Multi-Filter (Discover)Prototype: only a list of events.Now: text search + category + location + date/time filter combined. This is the main innovative feature - users can find the right event fast instead of scrolling.Long-Press to BookmarkPrototype: no saving.Now: tap to view, long-press to save. Bookmarks persist with SharedPreferences even after closing the app. No login / database needed for saving.One-Tap Event SharingPrototype: share button did nothing.Now: generic share sheet + copy link + WhatsApp direct + Email + Facebook placeholder. User can add a custom message.Secure Registration with API + HashingPrototype: fake register button.Now: real Retrofit API call, SHA-256 password hashing, email + password validation, button disabled while waiting to prevent double-register.Multilingual Support (EN / AF / ZU)Prototype: English only.Now: language saved in Settings and applied across Dashboard + Discover using applyLocale().Date + Time Picker for EventsPrototype: text field for date.Now: DatePicker -> TimePicker flow on both Discover filter and Create Event, formatted as DD/MM/YYYY HH:MM.Android CI / GitHub ActionsPrototype: no CI.Now: automatic build + unit tests on every push to main / master.Bug Fixes In This Version
Fixed getStringSet() crash in Bookmarks - now copies to a new MutableSet before editingFixed crash when sharing to WhatsApp / Email with no app installed - added resolveActivity() checkFixed duplicate registration on double-tap - Register button disabled during API callFixed empty / space-only input counting as valid - added .trim() on Login + RegistrationFixed clickable share rows in Event Sharing - added clickable + focusable to WhatsApp / Facebook / Email rowsFixed missing accessibility labels - added contentDescription to iconsKnown Limitations
Registration API needs a live backend - demo mode continues to Login if server is offlineFacebook share is a placeholder - needs Facebook Share SDK for full implementationEvent list is currently hardcoded demo data (Fun Run, Food Drive, Clean-Up, Youth Sports Day, Career Workshop) - next version will load from API / FirebaseNo push notifications yet - notification icon on Dashboard is UI onlyTesting
Manual testing: Login, Register, Discover search/filter, Create Event, Bookmark (long-press), Share, Copy Link, Language changeUnit tests: ./gradlew testBuild: ./gradlew buildTech Stack
Kotlin, Android XML, Material DesignRetrofit 2 + Gson for APISharedPreferences for Bookmarks + SettingsGitHub Actions for CI

AI TOOLS USED 
During this assessment for Community Events SA Android app, I used Meta AI and ChatGPT as supplementary aids. My use was limited to understanding concepts, debugging, and documentation, while planning, coding, and implementation were done by myself. I did not copy a finished app from AI. I planned the screens, I wrote the Kotlin code, I tested the app in Android Studio, and I fixed the errors myself. AI helped me understand faster, it did not replace my own work.Code Snippets and Debugging: I struggled to connect Retrofit 2 with GSON to my MockAPI endpoint. I asked why GET returned null and why POST for Create Events was not persisting. AI suggested checking data class fields to match title, location, date, description, imageUrl and fixing base URL. I checked my own code, I compared the field names, I corrected the mismatch, and I tested again until the data showed correctly. For Discover, my RecyclerView filter for categories Sports, Music, Business and locations Durban, Johannesburg, Cape Town, Pretoria was not updating. AI explained how filtering should work. I adapted the logic myself, I rewrote the filter, and I tested each category and each location until the list updated correctly.Feature Support: For my three added features Create Events, Discover Events, and Bookmark System, I used AI to brainstorm flow. For Create Events, I asked how to validate empty fields before API call. AI suggested checking each field, showing a message, and stopping the call if anything was missing. I wrote that validation myself. For Discover Events, I asked how to implement keyword search with native Date and Time Pickers for filtering. AI explained the picker flow. I built the search and the pickers myself. For Bookmark System, I asked how to implement long press to save instantly and manage list with SharedPreferences for real time updates in Saved page. AI explained the idea of saving a set of titles. I wrote the save, load, and remove logic myself and I tested long press and removal.API Setup: I used AI to guide me through MockAPI to create project, create events resource, delete default Faker fields name and avatar, and add custom fields. I created the project myself, I typed the fields myself, and I tested the GET and POST calls myself in the app.Documentation: I used AI to rephrase README, remove icons, structure All Pages and Three Features sections, and for bilingual support English and IsiZulu using strings.xml. I chose what to keep, I checked that the description matched my app, and I removed anything that was not true.Where Cited: I cited in two places. First, inside source code in DiscoverFragment.kt, CreateEventFragment.kt, BookmarksFragment.kt with comments stating Assisted by AI Meta AI for filter logic. Second, in documentation, I included this declaration at bottom of README.md. I confirm I understand all submitted code and AI was only support. I can explain every screen, every feature, and every fix if asked today.

Screenshots

Splash screen
<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/c0f64b39-f389-42d2-a9cf-bcdaa76a03ab" />

Register screen
<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/a5e95d51-3580-484d-b61d-38161b3e5ae4" />

Login screen
<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/ceda7d82-ed44-4b9f-9692-5fe81614eb04" />

Home screen
<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/b7a98358-6836-4082-b587-ecd39ba84c72" />

Discover screen 
<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/6c9fe175-8794-444f-aa40-e8320968b026" />

Bookmarks
<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/822eb57a-3260-4300-b619-ce6354e7abce" />

Settings 
<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/fe15db59-522d-46ac-b26b-52ba3419dc92" />
<img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/4529734e-5653-4ddd-93b1-1db6b6810f09" />


Firebase
<img width="1786" height="902" alt="image" src="https://github.com/user-attachments/assets/4afe5d9f-0566-41b5-a526-69db4958e27d" />

<img width="1901" height="1032" alt="image" src="https://github.com/user-attachments/assets/3b36905a-b504-4b05-ad7c-6d20b17f5464" />

<img width="1660" height="645" alt="image" src="https://github.com/user-attachments/assets/5323fb0f-0fd6-4cb6-986a-e91307a3e95c" />

https://console.firebase.google.com/project/communityeventssa/firestore/databases/-default-/data/~2Fevents~2F1XwsFyJtqtDocY19a3fi

 Video link: 
 Github link:
 Evidence of preparation: 












