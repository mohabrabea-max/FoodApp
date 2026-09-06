package com.example.applicationhome.core.domain.Implementations

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Build
import com.example.applicationhome.BuildConfig
import com.example.applicationhome.core.domain.repository.LocationRepository
import com.example.applicationhome.data.data.model.LocationDataClass
import com.example.applicationhome.data.local.source.LocationDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume

class LocationRepositoryImpl @Inject constructor(
    private val locationDataSource : LocationDataSource,
    @ApplicationContext private val context: Context
): LocationRepository {
    override suspend fun getAddressFromLocation(
        lat: Double,
        lng: Double
    ): Result<LocationDataClass> = withContext(Dispatchers.IO){
        val geocoder = Geocoder(context, Locale("en"))

        return@withContext try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                suspendCancellableCoroutine { continuation ->
                    geocoder.getFromLocation(lat, lng, 1){ addresses ->
                        val address = addresses.firstOrNull()
                        val area = address?.subLocality ?: address?.locality ?: "Undefined area"
                        val full = address?.getAddressLine(0) ?: ""

                        if (continuation.isActive) continuation.resume(
                            Result.success(
                                LocationDataClass(
                                    latitude = lat,
                                    longitude = lng,
                                    locationName = area,
                                    locationFullName = full
                                )
                            )
                        )
                    }
                }
            }else{
                // الإصدارات الأقدم من أندرويد
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                val address = addresses?.firstOrNull()
                val area = address?.subLocality ?: address?.locality ?: "Undefined area"
                val full = address?.getAddressLine(0) ?: ""

                Result.success(
                    LocationDataClass(
                        latitude = lat,
                        longitude = lng,
                        locationName = area,
                        locationFullName = full
                    )
                )
            }
        } catch (e : Exception) {
            Result.failure(e)
        }
    }

    override fun buildStaticMapUrl(lat: Double, lon: Double): String {
        val zoom = 16
        val width = 600
        val height = 300
        val apiKey = BuildConfig.GEOAPIFY_MAP_API_KEY

        return "https://maps.geoapify.com/v1/staticmap?" +
                "style=osm-carto&" +
                "width=$width&height=$height&" +
                "center=lonlat:$lon,$lat&" +
                "zoom=$zoom&" +
                "marker=lonlat:$lon,$lat;color:%23ff0000;size:medium&" +
                "apiKey=$apiKey"
    }

    @SuppressLint("MissingPermission")
    override suspend fun fetchCurrentLocation(): Result<Location> {
        return try {
            val result = locationDataSource.fetchCurrentLocation()
            if(result != null){
                Result.success(result)
            }else{
                Result.failure(Exception("Unable to retrieve current location."))
            }
        } catch (e : Exception) {
            Result.failure(e)
        }
    }
}