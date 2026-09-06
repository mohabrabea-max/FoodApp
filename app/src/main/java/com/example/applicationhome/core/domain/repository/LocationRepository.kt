package com.example.applicationhome.core.domain.repository

import android.location.Location
import com.example.applicationhome.data.data.model.LocationDataClass

interface LocationRepository {
    suspend fun getAddressFromLocation(
        lat: Double,
        lng: Double
    ): Result<LocationDataClass>
    fun buildStaticMapUrl(lat: Double, lon: Double): String
    suspend fun fetchCurrentLocation(): Result<Location>
}