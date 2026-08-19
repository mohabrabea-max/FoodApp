package com.example.applicationhome.core.domain.repository

interface LocationRepository {
    fun getAddressFromLocation(
        lat: Double,
        lng: Double,
        onAddressFound: (areaName: String, fullAddress: String) -> Unit
    )
}