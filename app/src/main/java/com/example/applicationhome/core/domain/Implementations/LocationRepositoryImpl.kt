package com.example.applicationhome.core.domain.Implementations

import android.content.Context
import android.location.Geocoder
import android.os.Build
import com.example.applicationhome.BuildConfig
import com.example.applicationhome.core.domain.repository.LocationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): LocationRepository {
    override fun getAddressFromLocation(
        lat: Double,
        lng: Double,
        onAddressFound: (areaName: String, fullAddress: String) -> Unit
    ){
        val geocoder = Geocoder(context, Locale("en"))

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            geocoder.getFromLocation(lat, lng, 1){ addresses ->
                val address = addresses.firstOrNull()
                val area = address?.subLocality ?: address?.locality ?: "Undefined area"
                val full = address?.getAddressLine(0) ?: ""
                onAddressFound(area, full)
            }
        }else{
            // الإصدارات الأقدم من أندرويد
            try {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                val address = addresses?.firstOrNull()
                val area = address?.subLocality ?: address?.locality ?: "Undefined area"
                val full = address?.getAddressLine(0) ?: ""
                onAddressFound(area, full)
            } catch (e: Exception) {
                onAddressFound("تعذر جلب العنوان", "")
            }
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
}