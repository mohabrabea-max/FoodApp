package com.example.applicationhome.core.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.SyncAllDataRepository
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinalScreenViewModel @Inject constructor(
    private val syncAllDataRepository : SyncAllDataRepository,
    private val networkObserver: NetworkObserver
) : ViewModel() {
    var isNetworkAvailable by mutableStateOf(false)

    val mealsLastSyncTime =
        syncAllDataRepository.mealsLastSyncTime
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0L
            )

    val snacksLastSyncTime =
        syncAllDataRepository.snacksLastSyncTime
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0L
            )

    val restaurantsLastSyncTime =
        syncAllDataRepository.restaurantsLastSyncTime
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0L
            )

    init {
        viewModelScope.launch {
            networkObserver.isNetworkAvailable.collect { available ->
                isNetworkAvailable = available
                if(available){
                    syncAllDataRepository.syncAllMealsToDatabase(mealsLastSyncTime.value)
                    syncAllDataRepository.syncAllSnacksToDatabase(snacksLastSyncTime.value)
                    syncAllDataRepository.syncAllRestaurantsToDatabase(restaurantsLastSyncTime.value)
                }
            }
        }
    }
}