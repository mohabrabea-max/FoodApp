package com.example.applicationhome.features.homescreen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.FavoriteRepository
import com.example.applicationhome.core.domain.repository.HomeScreenRepository
import com.example.applicationhome.core.domain.repository.ItemScreenRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.GetFavoriteUseCase
import com.example.applicationhome.data.local.entity.CategoriesEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.OffersEntity
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.data.local.entity.RestaurantsEntity
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val itemScreenRepository : ItemScreenRepository,
    private val favoriteRepository : FavoriteRepository,
    private val homeScreenRepository : HomeScreenRepository,
    userRepository: UserRepository,
    private val getFavoriteUseCase : GetFavoriteUseCase,
    private val networkObserver : NetworkObserver
) : ViewModel(){

    val userData = userRepository.userData

    //       *** ---------------------------- \\***  Home Screen Items  ***// ---------------------------- ***

    val selected = MutableStateFlow(0)

    private val typ = MutableStateFlow("All")

    val filterRestaurants : StateFlow<List<RestaurantWithFavoriteStatus>> =
        typ.flatMapLatest { type ->
            homeScreenRepository.getRestaurantsFromDatabase(type)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    val categories : StateFlow<List<CategoriesEntity>> =
        homeScreenRepository.categoriesFromDatabase

    val offers : StateFlow<List<OffersEntity>> =
        homeScreenRepository.offersFromDatabase


    val isNetworkAvailable = MutableStateFlow(true)


    init {
        viewModelScope.launch {
            networkObserver.isNetworkAvailable.collect { available ->
                isNetworkAvailable.value = available
            }
        }
    }

    fun select(item : CategoriesEntity){
        selected.value = item.id
        typ.value = item.type
    }

    fun unSelected(){
        selected.value = 0
        typ.value = "All"
    }


    //       *** ---------------------------- \\***  Item Screen  ***// ---------------------------- ***

    fun selectRestaurant(item : RestaurantsEntity, navigation : () -> Unit){
        viewModelScope.launch {
            try {
                val newData = favoriteRepository.getRestaurantToView(item.id)
                itemScreenRepository.selectRestaurant(newData)
            }catch (e : Exception){
                null
            }

            selectedtype(0, item.typ.toList().first())

            navigation()
        }
    }

    private fun selectedtype(index : Int, type : String){
        itemScreenRepository.selectedTypeInRestaurant(index, type)
    }


//       *** ---------------------------- \\***  Favorite  ***// ---------------------------- ***

    fun addRestaurantsFavorite(restaurants: FavoriteRestaurantEntity){
        viewModelScope.launch {
            getFavoriteUseCase.addRestaurantsFavorite(restaurants)
        }
    }

    fun removeRestaurantsFavorite(resId : Int){
        viewModelScope.launch {
            getFavoriteUseCase.removeRestaurantsFavorite(resId)
        }
    }
}