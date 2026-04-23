package com.example.ipgeolookup.data.model

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
fun GeoLocation.getFormattedCoordinates(): String {
    return latitude?.let { lat ->
        longitude?.let { lon ->
            "${lat.toString().take(7)}, ${lon.toString().take(7)}"
        }
    } ?: "N/A"
}

// Helper function to get full location string
fun GeoLocation.getFullLocation(): String {
    val parts = mutableListOf<String>()
    city?.let { parts.add(it) }
    region?.let { parts.add(it) }
    countryCode?.let { parts.add(it) }
    return parts.joinToString(", ")
}

/**
 * Parse the "loc" field (format: "lat,lon") and update latitude/longitude fields.
 * This function is called after deserialization to populate latitude/longitude from the loc string.
 */
fun GeoLocation.parseLocationField(): GeoLocation {
    return this.location?.let { locString ->
        val parts = locString.split(",")
        if (parts.size == 2) {
            val lat = parts[0].trim().toDoubleOrNull()
            val lon = parts[1].trim().toDoubleOrNull()
            copy(latitude = lat, longitude = lon)
        } else {
            this
        }
    } ?: this
}

/**
 * Create a Gson instance that properly handles the "loc" field parsing.
 * After deserializing, call parseLocationField() on the result to populate latitude/longitude.
 */
fun createGsonWithLocationParsing(): Gson {
    return GsonBuilder()
        .registerTypeAdapter(GeoLocation::class.java, JsonDeserializer<GeoLocation> { json, type, jsonDeserializationContext ->
            val jsonObject = json.asJsonObject
            val ipAddress = jsonObject.get("ip")?.asString
            val hostname = jsonObject.get("hostname")?.asString
            val continent = jsonObject.get("continent")?.asString
            val country = jsonObject.get("country")?.asString
            val region = jsonObject.get("region")?.asString
            val city = jsonObject.get("city")?.asString
            val loc = jsonObject.get("loc")?.asString
            val org = jsonObject.get("org")?.asString
            val isp = jsonObject.get("isp")?.asString
            val currency = jsonObject.get("currency")?.asString
            val isEU = jsonObject.get("isEU")?.asBoolean
            val timezone = jsonObject.get("timezone")?.asString
            val postal = jsonObject.get("postal")?.asString
            val flag = jsonObject.get("flag")?.asString

            GeoLocation(
                ipAddress = ipAddress ?: "",
                hostname = hostname,
                continentCode = continent,
                countryCode = country,
                region = region,
                city = city,
                latitude = null, // Will be parsed from loc
                longitude = null, // Will be parsed from loc
                location = loc,
                organization = org,
                isp = isp,
                currency = currency,
                isEU = isEU,
                timezone = timezone,
                postalCode = postal,
                flag = flag
            ).parseLocationField()
        })
        .create()
}