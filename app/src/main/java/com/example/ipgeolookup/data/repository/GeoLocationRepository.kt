package com.example.ipgeolookup.data.repository

import com.example.ipgeolookup.data.model.GeoLocation
import com.example.ipgeolookup.data.model.Result
import com.example.ipgeolookup.data.remote.ApiClient
import com.example.ipgeolookup.data.remote.IpInfoApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeoLocationRepository {
    
    suspend fun getIpInfo(ip: String? = null): Result<GeoLocation> {
        return withContext(Dispatchers.IO) {
            try {
                val location = if (ip == null) {
                    ApiClient.api.getIpInfo(token = ApiClient.getToken())
                } else {
                    ApiClient.api.getIpInfo(ip, token = ApiClient.getToken())
                }
                Result.Success(location)
            } catch (e: Exception) {
                Result.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}