package com.example.applicationhome.core.ui.theme.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.SyncAllDataRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinalScreenViewModel @Inject constructor(
    private val syncAllDataRepository : SyncAllDataRepository,
    private val favoriteRepository : FavoriteRepository,
    private val userRepository : UserRepository,
    private val networkObserver: NetworkObserver
) : ViewModel() {
    val isNetworkAvailable = MutableStateFlow(false)

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
            combine(
                networkObserver.isNetworkAvailable,
                userRepository.userData
            ) { network, user -> Pair(network, user) }
                .distinctUntilChanged()
                .collect { (network, user) ->
                    isNetworkAvailable.value = network

                    syncDataParallel()

                    if (network && user.id.isNotEmpty()) {
                        favoriteRepository.syncFavoritesInDatabase(user.id)
                    }
                }
        }
    }

    private fun syncDataParallel() {
        viewModelScope.launch {
            // ننتظر أول قيمة حقيقية تأتي من الـ DataStore بدلاً من استخدام .value الحالية مباشرة لتجنب قيم الـ 0L المبدئية
            val mealsTime = mealsLastSyncTime.first()
            val snacksTime = snacksLastSyncTime.first()
            val restaurantsTime = restaurantsLastSyncTime.first()

            // تشغيل الـ 3 عملية sync بالتوازي للأداء الأفضل
            launch { syncAllDataRepository.syncAllMealsToDatabase(mealsTime) }
            launch { syncAllDataRepository.syncAllSnacksToDatabase(snacksTime) }
            launch { syncAllDataRepository.syncAllRestaurantsToDatabase(restaurantsTime) }
        }
    }
}