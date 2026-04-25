package com.example.ipgeolookup.data.repository

import com.example.ipgeolookup.data.model.GeoLocation
import com.example.ipgeolookup.data.model.Result
import com.example.ipgeolookup.data.remote.ApiClient
import com.example.ipgeolookup.data.remote.IpInfoApi
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