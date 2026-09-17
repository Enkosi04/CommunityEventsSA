# CommunityEventsSA
Community Events SA is an Android app that connects South African citizens with their local communities through a centralized platform. 
API Endpoint https://6aaaa4c3ff4dd5698b4edf91.mockapi.io/api/v1/events 
All Pages in the App
Splash Screen Page Branded entry point that initializes settings. Onboarding Page Registration and Login with SHA-256 hashing. Dashboard Page Home screen with grid navigation. Discover Page Displays events with search and filters for category, location, date and time. Create Event Page Users host events by entering title, description, location and time. Bookmarks Page Lists bookmarked events in real time with one tap removal. Settings Page Manages dark mode, English and IsiZulu language, contact info and interests. Profile Management Page Updates personal details and interests.
Features I Added
I added three features which are Create Events, Discover Events and Bookmark System. Create Events Lets users lead initiatives. Sends new event to database via API https://6aaaa4c3ff4dd5698b4edf91.mockapi.io/api/v1/events and auto adds to bookmarks. Discover Events Helps find relevant events. Implements keyword search, filtering by categories like Sports, Music, Business and locations like Durban, Johannesburg, Cape Town, Pretoria, and native date and time pickers. Bookmark System For personal tracking. Long press to save in Discover, dedicated Saved page, and one tap removal.
SPLASH SCREEN <img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/ca73dda4-a42c-48ac-8798-24b7796695ff" />
REGISTER <img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/0c65d13f-1f3d-469a-8d8a-19b88ba7ab65" />
LOGIN <img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/246ba5b6-68af-4b89-ae86-4e77405267b5" />
HOME  <img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/dc99e2fa-5453-4c01-b97b-eb4b48164468" />
DISCOVER <img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/40115b11-a5b2-4983-88db-b868aaf3c74e" />
BOOKMARKS <img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/bfe229ca-ebbe-4077-bfdb-4801e93e8e28" />
CREATE EVENT <img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/9f6282cc-ba75-4e12-b8b6-68107e20a035" />
SETTINGS <img width="720" height="1600" alt="image" src="https://github.com/user-attachments/assets/f234798e-b97e-4c68-a31e-a52b03199a30" />
API <img width="883" height="584" alt="image" src="https://github.com/user-attachments/assets/e7ae7e99-349d-4a2c-befa-7f6977acd525" />



AI Tools Usage Declaration for Community Events SA
During this assessment for Community Events SA Android app, I used Meta AI and ChatGPT as supplementary aids. My use was limited to understanding concepts, debugging, and documentation, while planning, coding, and implementation were done by myself.
Manner of Use:
Code Snippets and Debugging: I struggled to connect Retrofit 2 with GSON to my MockAPI endpoint at https://6aaaa4c3ff4dd5698b4edf91.mockapi.io/api/v1/events. I asked why GET returned null and why POST for Create Events was not persisting. AI suggested checking data class fields to match title, location, date, description, imageUrl and fixing base URL. For Discover, my RecyclerView filter for categories Sports, Music, Business and locations Durban, Johannesburg, Cape Town, Pretoria was not updating. I adapted logic myself.
Feature Support: For my three added features Create Events, Discover Events, and Bookmark System, I used AI to brainstorm flow. For Create Events, how to validate empty fields before API call. For Discover Events, how to implement keyword search with native Date and Time Pickers for filtering. For Bookmark System, how to implement long-press to save instantly and manage list with SharedPreferences for real-time updates in Saved page. Final Kotlin code was written and tested by me in Android Studio.
API Setup: I used AI to guide me through MockAPI to create project, create events resource, delete default Faker fields name and avatar, and add custom fields.
Documentation: I used AI to rephrase README, remove icons, structure All Pages and Three Features sections, and for bilingual support English and IsiZulu using strings.xml.
Where Cited: I cited in two places. First, inside source code in DiscoverFragment.kt, CreateEventFragment.kt, BookmarksFragment.kt with comments stating Assisted by AI - Meta AI for filter logic. Second, in documentation, I included this declaration and acknowledgement at bottom of README.md.
I confirm I understand all submitted code and AI was only support.

Github link https://github.com/Enkosi04/CommunityEventsSA
API link https://6aaaa4c3ff4dd5698b4edf91.mockapi.io/api/v1/events
Video link https://drive.google.com/file/d/1XlETrNv_thrYdqucV1zhtSIqx7oi8Kl3/view?usp=drivesdk








