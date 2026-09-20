package com.example.applicationhome.core.domain.usecase

import com.example.applicationhome.core.domain.repository.SyncAllDataRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.ui.model.UiStates
import javax.inject.Inject

class SyncAddressesUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val syncAllDataRepository: SyncAllDataRepository
){
    suspend operator fun invoke(): UiStates{
        val id = userRepository.userData.value.id
        if(id.isNotEmpty()){
            val result = syncAllDataRepository.syncAddresses(id)
            return result
        }else{
            return UiStates.Success
        }
    }
}