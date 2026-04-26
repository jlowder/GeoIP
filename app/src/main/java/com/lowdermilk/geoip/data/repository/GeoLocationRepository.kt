package com.lowdermilk.geoip.data.repository

import com.lowdermilk.geoip.data.model.GeoLocation
import com.lowdermilk.geoip.data.model.Result
import com.lowdermilk.geoip.data.remote.ApiClient
import com.lowdermilk.geoip.data.remote.IpInfoApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.UnknownHostException

class GeoLocationRepository {
    
    suspend fun getIpInfo(ip: String? = null): Result<GeoLocation> {
        return withContext(Dispatchers.IO) {
            try {
                val resolvedIp = ip?.let {
                    try {
                        InetAddress.getByName(it).hostAddress
                    } catch (e: UnknownHostException) {
                        return@withContext Result.Error("Could not resolve hostname: $it")
                    }
                }

                val location = if (resolvedIp == null) {
                    ApiClient.api.getIpInfo(token = ApiClient.getToken())
                } else {
                    ApiClient.api.getIpInfo(resolvedIp, token = ApiClient.getToken())
                }
                Result.Success(location)
            } catch (e: Exception) {
                Result.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}