# Android Project Structure

## Complete File Listing

### Root Files
- `README.md` - Project documentation
- `build.gradle` - Root build configuration
- `settings.gradle` - Project settings
- `gradle.properties` - Gradle configuration
- `gradle/wrapper/gradle-wrapper.properties` - Gradle wrapper configuration

### App Module
#### Java/Kotlin Source Files
- `app/src/main/java/com/example/ipgeolookup/MainActivity.kt` - Main activity with UI and logic
- `app/src/main/java/com/example/ipgeolookup/data/model/GeoLocation.kt` - Data model for location info
- `app/src/main/java/com/example/ipgeolookup/data/model/GeoLocationExtensions.kt` - Extension functions
- `app/src/main/java/com/example/ipgeolookup/data/model/Result.kt` - Result sealed class
- `app/src/main/java/com/example/ipgeolookup/data/remote/ApiService.kt` - API client and interface
- `app/src/main/java/com/example/ipgeolookup/data/repository/GeoLocationRepository.kt` - Repository layer
- `app/src/main/java/com/example/ipgeolookup/ui/viewmodel/GeoLocationViewModel.kt` - ViewModel for UI state

#### Resource Files
- `app/src/main/res/layout/activity_main.xml` - Main UI layout
- `app/src/main/res/values/colors.xml` - Color definitions
- `app/src/main/res/values/strings.xml` - String resources
- `app/src/main/res/values/themes.xml` - Theme definitions
- `app/src/main/res/xml/backup_rules.xml` - Backup rules
- `app/src/main/res/xml/data_extraction_rules.xml` - Data extraction rules
- `app/src/main/res/drawable/` - All drawable resources (icons, backgrounds)
- `app/src/main/res/mipmap/` - Launcher icons

#### Configuration Files
- `app/build.gradle` - App module build configuration
- `app/proguard-rules.pro` - ProGuard rules
- `app/src/main/AndroidManifest.xml` - Android manifest with permissions

## Key Features Implemented

### 1. Auto-detect IP on Start
When the app launches, it automatically detects the user's IP address and displays location information.

### 2. Manual IP Lookup
Users can enter any IP address in the search bar to get location details.

### 3. Location Details Display
- IP Address
- Country
- Region
- City
- Coordinates (latitude/longitude)
- Organization
- ISP

### 4. Interactive Map
- OpenStreetMap integration via OSMDroid
- Shows marker at detected location
- "View on Map" button to open in Google Maps

### 5. Copy to Clipboard
One-tap copy of all location information.

### 6. Loading States
- Loading indicator while fetching data
- Content container visibility management

### 7. Error Handling
- Network errors
- Invalid IP format validation
- Unknown errors

### 8. Toast Notifications
User-friendly feedback for actions.

## Architecture

### Layered Architecture
1. **UI Layer** (MainActivity): Handles UI events, displays data
2. **ViewModel Layer** (GeoLocationViewModel): Manages UI state, communicates with repository
3. **Repository Layer** (GeoLocationRepository): Handles data operations
4. **Data Layer** (ApiService): Network API calls using Retrofit

### State Management
Uses `StateFlow` for reactive UI updates with `GeoLocationUiState` sealed class.

### Threading
All network operations run on IO dispatcher using coroutines.

## Permissions Required
- INTERNET
- ACCESS_NETWORK_STATE
- ACCESS_COARSE_LOCATION
- ACCESS_FINE_LOCATION

## API Integration
Uses IPinfo.io API for geolocation data (free tier available).

## Map Integration
Uses OSMDroid library for OpenStreetMap integration.

## Building the Project
```bash
./gradlew assembleDebug
./gradlew assembleRelease
```

## Running the App
Open in Android Studio and click Run, or use:
```bash
./gradlew installDebug