# IP Geo Lookup - Android Application

A complete, production-ready Android Kotlin application for IP geolocation lookup.

## Summary

This project demonstrates modern Android development practices with:
- **Kotlin** as the primary language
- **Jetpack Architecture Components** (ViewModel, Lifecycle)
- **StateFlow** for reactive state management
- **Coroutines** for asynchronous operations
- **Retrofit** for API calls
- **OSMDroid** for OpenStreetMap integration
- **Material Design 3** components

## Complete Project Structure

### Root Files
- `README.md` - Project documentation
- `PROJECT_STRUCTURE.md` - Detailed structure documentation
- `build.gradle` - Root build configuration
- `settings.gradle` - Project settings
- `gradle.properties` - Gradle configuration
- `gradle/wrapper/gradle-wrapper.properties` - Gradle wrapper configuration

### App Module (app/)

#### Java/Kotlin Source Files (7 files)
1. `MainActivity.kt` - Main activity with UI and logic
2. `data/model/GeoLocation.kt` - Data model for location info
3. `data/model/GeoLocationExtensions.kt` - Extension functions
4. `data/model/Result.kt` - Result sealed class for state management
5. `data/remote/ApiService.kt` - API client and interface
6. `data/repository/GeoLocationRepository.kt` - Repository layer
7. `ui/viewmodel/GeoLocationViewModel.kt` - ViewModel for UI state

#### Resource Files (18 XML files)
**Layouts (1 file)**
- `activity_main.xml` - Main UI layout

**Values (3 files)**
- `colors.xml` - Color definitions
- `strings.xml` - String resources (29 strings)
- `themes.xml` - Theme definitions

**XML (2 files)**
- `backup_rules.xml` - Backup rules
- `data_extraction_rules.xml` - Data extraction rules

**Drawables (9 files)**
- `ic_search.xml` - Search icon
- `ic_copy.xml` - Copy icon
- `ic_refresh.xml` - Refresh/detect icon
- `ic_map.xml` - Map icon
- `ic_location.xml` - Location icon
- `ic_error.xml` - Error icon
- `ic_launcher.xml` - Launcher icon
- `map_background.xml` - Map container background
- `error_background.xml` - Error message background

**Mipmap (2 files)**
- `ic_launcher.xml` - Launcher icon
- `ic_launcher_round.xml` - Round launcher icon

#### Configuration Files
- `build.gradle` - App module build configuration (dependencies)
- `proguard-rules.pro` - ProGuard rules
- `AndroidManifest.xml` - Android manifest with 4 permissions

## Key Features

### 1. Auto-detect IP on Start
- Automatically detects and displays location information
- Uses IPinfo.io API for geolocation

### 2. Manual IP Lookup
- Search for any IP address
- Input validation for IP address format

### 3. Location Information Display
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
- One-tap copy of all location information

### 6. Loading States
- Loading indicator while fetching data
- Content container visibility management

### 7. Error Handling
- Network errors with user-friendly messages
- Invalid IP format validation
- Unknown errors handling

### 8. Toast Notifications
- User-friendly feedback for actions

## Architecture

### Layered Architecture
```
┌─────────────────────────────────────┐
│         UI Layer (MainActivity)     │
│  - Handles UI events                │
│  - Displays data                    │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│     ViewModel Layer (GeoLocationVM) │
│  - Manages UI state                 │
│  - Uses StateFlow                   │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│    Repository Layer (GeoLocationRepo)│
│  - Handles data operations          │
│  - Uses coroutines for threading    │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│       Data Layer (ApiService)       │
│  - Network API calls (Retrofit)     │
└─────────────────────────────────────┘
```

### State Management
```kotlin
sealed class GeoLocationUiState {
    object Idle : GeoLocationUiState()
    object Loading : GeoLocationUiState()
    data class Success(val location: GeoLocation) : GeoLocationUiState()
    data class Error(val message: String) : GeoLocationUiState()
}
```

### Threading
All network operations run on IO dispatcher using coroutines:
```kotlin
viewModelScope.launch {
    val result = repository.getIpInfo(ip)
    // UI state updates happen on main thread
}
```

## Dependencies

```kotlin
// Core Android
androidx.core:core-ktx:1.12.0
androidx.appcompat:appcompat:1.6.1
com.google.android.material:material:1.11.0
androidx.constraintlayout:constraintlayout:2.1.4

// API & JSON
com.squareup.retrofit2:retrofit:2.9.0
com.squareup.retrofit2:converter-gson:2.9.0

// Coroutines
org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3
org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3

// OSMDroid (OpenStreetMap)
org.osmdroid:osmdroid-android:6.1.16
```

## Permissions

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

## API Integration

Uses IPinfo.io API for geolocation data:
- Free tier available with 50,000 requests per month
- No API key required for basic usage
- Base URL: https://ipinfo.io/

## Map Integration

Uses OSMDroid library for OpenStreetMap integration:
- Supports offline maps
- Customizable tile sources
- Marker support
- Multi-touch controls

## Building the Project

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 34 (Android 14)
- Gradle 8.2

### Build Commands
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Clean build
./gradlew clean
```

### Running the App
1. Open in Android Studio
2. Click Run (green play icon)
3. App deploys to connected device or emulator

## File Statistics

- **Total Files**: 33
- **Kotlin Files**: 7
- **XML Files**: 18
- **Gradle Files**: 2
- **String Resources**: 29
- **Drawables**: 9

## Key Design Patterns

1. **Repository Pattern**: Separates data sources from business logic
2. **ViewModel Pattern**: Manages UI state and lifecycle
3. **StateFlow Pattern**: Reactive state management
4. **Dependency Injection**: Minimal DI for simplicity
5. **Separation of Concerns**: Clear layer separation

## Error Handling

- Network errors: Displays user-friendly message
- Invalid IP format: Shows validation error
- IP not found: Displays appropriate message
- Permission denied: Shows message about required permissions
- Unknown errors: Generic error handling

## Testing

The project includes:
- Unit test structure ready for testing
- Mockable API for unit tests
- ViewModel state testing capabilities

## Future Enhancements

Potential improvements:
1. Unit tests for ViewModel
2. Integration tests for MainActivity
3. Dark mode support
4. Location history
5. Multiple location searches
6. Export to file functionality
7. Widget support
8. Background service for continuous monitoring

## License

This project is open source and available under the MIT License.

## Acknowledgments

- IPinfo.io for providing the geolocation API
- OpenStreetMap contributors for the map data
- OSMDroid team for the mapping library
- Android Jetpack team for the architecture components