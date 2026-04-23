package com.example.ipgeolookup.data.remote

import com.example.ipgeolookup.BuildConfig
import com.example.ipgeolookup.data.model.GeoLocation
import com.example.ipgeolookup.data.model.createGsonWithLocationParsing
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IpInfoApi {
    @GET("json")
    suspend fun getIpInfo(
        @Query("token") token: String? = null
    ): GeoLocation

    @GET("{ip}")
    suspend fun getIpInfo(
        @Path("ip") ip: String,
        @Query("token") token: String? = null
    ): GeoLocation
}

object ApiClient {
    private const val BASE_URL = "https://ipinfo.io/"
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(createGsonWithLocationParsing()))
        .build()
    
    val api: IpInfoApi = retrofit.create(IpInfoApi::class.java)
    
    fun getApiWithToken(): IpInfoApi {
        return retrofit.create(IpInfoApi::class.java)
    }
    
    fun getToken(): String? {
        return BuildConfig.API_TOKEN
    }
}