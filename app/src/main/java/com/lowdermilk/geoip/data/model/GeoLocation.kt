package com.lowdermilk.geoip.data.model

import com.google.gson.annotations.SerializedName

data class GeoLocation(
    @SerializedName("ip") val ipAddress: String,
    val hostname: String? = null,
    @SerializedName("continent") val continentCode: String? = null,
    @SerializedName("country") val countryCode: String? = null,
    val region: String? = null,
    val city: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @SerializedName("loc") val location: String? = null,
    @SerializedName("org") val organization: String? = null,
    val isp: String? = null,
    val currency: String? = null,
    @SerializedName("isEU") val isEU: Boolean? = null,
    val timezone: String? = null,
    @SerializedName("postal") val postalCode: String? = null,
    val flag: String? = null
)