package com.example.applicationhome.core.domain.Implementations

import android.content.Context
import android.location.Geocoder
import android.os.Build
import com.example.applicationhome.core.domain.repository.LocationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

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
}