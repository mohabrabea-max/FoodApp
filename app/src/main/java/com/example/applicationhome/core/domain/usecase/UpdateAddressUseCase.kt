package com.example.applicationhome.core.domain.usecase

import com.example.applicationhome.core.domain.repository.AddressesRepository
import com.example.applicationhome.data.data.model.Address
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(
    private val addressesRepository : AddressesRepository
){
    operator suspend fun invoke(
        userId : String,
        addressId : Long,
        title : String,
        house : String,
        street : String,
        phoneNumber : String,
        additionalDirectionsState : String,
        addressLabelState : String,
        latLocation : String,
        lngLocation : String,
        locationName : String,
        locationFullName : String,
    ){
        addressesRepository.updateAddresses(
            userId = userId,
            addressId = addressId,
            address = Address(
                title = title,
                house = house,
                street = street,
                phoneNumber = phoneNumber,
                additionalDirectionsState = additionalDirectionsState,
                addressLabelState = addressLabelState,
                latLocation = latLocation,
                lngLocation = lngLocation,
                locationName = locationName,
                locationFullName = locationFullName,
                lastUse = 0L
            )
        )
    }
}