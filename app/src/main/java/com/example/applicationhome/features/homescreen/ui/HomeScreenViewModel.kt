package com.example.applicationhome.features.homescreen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.applicationhome.core.domain.module.MainDispatcher
import com.example.applicationhome.core.domain.repository.SyncAllDataRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.GetFavoriteUseCase
import com.example.applicationhome.data.local.entity.CategoriesEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.OffersEntity
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val syncAllDataRepository : SyncAllDataRepository,
    userRepository : UserRepository,
    private val getFavoriteUseCase : GetFavoriteUseCase,
    networkObserver : NetworkObserver,
    @MainDispatcher private val dispatcher : CoroutineDispatcher
) : ViewModel(){

    val userData = userRepository.userData

    //       *** ---------------------------- \\***  Home Screen Items  ***// ---------------------------- ***

    private val _selected = MutableStateFlow(0)
    val selected = _selected.asStateFlow()

    val typ = MutableStateFlow("All")

    val filterRestaurants : Flow<PagingData<RestaurantWithFavoriteStatus>> =
        typ.flatMapLatest { type ->
            syncAllDataRepository.getRestaurantsFromDatabase(type)
        }.cachedIn(viewModelScope)


    val categories : StateFlow<List<CategoriesEntity>> =
        syncAllDataRepository.categoriesFromDatabase

    val offers : StateFlow<List<OffersEntity>> =
        syncAllDataRepository.getAllOffersFromDatabase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )


    val isNetworkAvailable =
        networkObserver.isNetworkAvailable
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = true
            )



    fun select(item : CategoriesEntity){
        _selected.value = item.id
        typ.value = item.type
    }

    fun unSelected(){
        _selected.value = 0
        typ.value = "All"
    }


//       *** ---------------------------- \\***  Favorite  ***// ---------------------------- ***

    fun addRestaurantsFavorite(restaurant: FavoriteRestaurantEntity){
        viewModelScope.launch(dispatcher) {
            getFavoriteUseCase.addRestaurantsFavorite(restaurant)
        }
    }

    fun removeRestaurantsFavorite(resId : Int){
        viewModelScope.launch(dispatcher) {
            getFavoriteUseCase.removeRestaurantsFavorite(resId)
        }
    }
}