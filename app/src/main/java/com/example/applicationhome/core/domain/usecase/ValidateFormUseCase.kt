package com.example.applicationhome.core.domain.usecase

import com.example.applicationhome.data.data.model.ProfileEditResult
import javax.inject.Inject

class ValidateFormUseCase @Inject constructor() {
    operator fun invoke(
        phoneNumber : String,
        house : String,
        street : String
    ): ProfileEditResult{
        val validPrefixes = listOf("010", "011", "012", "015")

        if(
            phoneNumber.isNotEmpty() &&
            (phoneNumber.length != 11 || !validPrefixes.any { phoneNumber.startsWith(it) })
        ){
            return ProfileEditResult.PhoneNumberIncomplete
        }

        if(house.isEmpty() || street.isEmpty()){
            return ProfileEditResult.DataIncomplete
        }

        return ProfileEditResult.Success
    }
}