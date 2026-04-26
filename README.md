# GeoIP - IP Address Geolocation

![Android](https://img.shields.io/badge/Android-6.0%2B-green.svg?logo=android)
![Platform](https://img.shields.io/badge/Platform-Android-brightgreen.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-2.3.20-blue.svg?logo=kotlin)
![Gradle](https://img.shields.io/badge/Gradle-9.4.1-red.svg?logo=gradle)
![Version](https://img.shields.io/badge/Version-1.0.0-blue.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)
![Tests](https://img.shields.io/badge/Tests-JUnit%204-4285F4.svg)
![Powered by IPinfo.io](https://img.shields.io/badge/Powered%20by-IPinfo.io-orange.svg)

A complete Android Kotlin application for IP geolocation lookup using IPinfo.io API.

## Features

- **Auto-detect User's IP**: Automatically detects and displays location on app start
- **Manual IP Lookup**: Search for any IP address to get location details
- **Location Information Display**: IP Address, Country, Region, City, Coordinates, Organization, ISP
- **Interactive Map**: OpenStreetMap integration via OSMDroid
- **Copy to Clipboard**: One-tap copy of all location information
- **Loading States**: Loading indicator while fetching data
- **Error Handling**: Network errors with user-friendly messages

## Installation

### From Source

```bash
git clone https://github.com/jlowder/GeoIP.git
cd GeoIP
./gradlew build
```

The APK will be generated at `app/build/outputs/apk/debug/app-debug.apk`

## API Integration

Uses IPinfo.io API for geolocation data:
- Free tier available with 50,000 requests per month
- No API key required for basic usage

## Map Integration

Uses OSMDroid library for OpenStreetMap integration:
- Supports offline maps
- Customizable tile sources
- Marker support

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

# Build debug and release in one step
./gradlew build

# Run tests
./gradlew test
```

## Dependencies

### Core Android
- androidx.core:core-ktx:1.18.0
- androidx.appcompat:appcompat:1.7.1
- com.google.android.material:material:1.13.0
- androidx.constraintlayout:constraintlayout:2.2.1

### API & JSON
- com.squareup.retrofit2:retrofit:3.0.0
- com.squareup.retrofit2:converter-gson:3.0.0

### Coroutines
- org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2
- org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2

### Lifecycle Extensions
- androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.0
- androidx.lifecycle:lifecycle-livedata-ktx:2.10.0

### OpenStreetMap
- org.osmdroid:osmdroid-android:6.1.20

## Architecture

### Layered Architecture
1. **UI Layer** (MainActivity): Handles UI events, displays data
2. **ViewModel Layer** (GeoLocationViewModel): Manages UI state
3. **Repository Layer** (GeoLocationRepository): Handles data operations
4. **Data Layer** (ApiService): Network API calls using Retrofit

### State Management
Uses `StateFlow` for reactive UI updates with `GeoLocationUiState` sealed class.

### Threading
All network operations run on IO dispatcher using coroutines.

## Permissions Required

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

## License

This project is open source and available under the [MIT License](LICENSE).

## Acknowledgments

- IPinfo.io for providing the geolocation API
- OpenStreetMap contributors for the map data
- OSMDroid team for the mapping library

## Support

- [GitHub Issues](https://github.com/jlowder/GeoIP/issues)
- [API Documentation](https://ipinfo.io/developers)
