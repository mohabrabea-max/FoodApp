package com.example.applicationhome.ui.theme.model

import android.app.Application
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.NetworkObserver
import com.example.applicationhome.data.models.model.Drink
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Offers
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.repository.FavoriteRepository
import com.example.applicationhome.data.models.repository.HomeScreenRepository
import com.example.applicationhome.data.models.repository.RestaurantScreenRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RestaurantViewModel (
    application : Application,
    private val restaurantScreenRepository : RestaurantScreenRepository,
    homeScreenRepository: HomeScreenRepository,
    private val favoriteRepository: FavoriteRepository
) : AndroidViewModel(application){
    private val networkObserver = NetworkObserver(application.applicationContext)
    var isNetworkAvailable by mutableStateOf(false)


    var resid by mutableStateOf(0)
    var selectedTypeIndex by mutableStateOf(0)
    var typeInRestaurantScreen by mutableStateOf("")



    val restaurantCount = homeScreenRepository.restaurantCount


    private val _foodMenuMap = mutableStateMapOf<String, FoodItem>()
    val foodMenuList = derivedStateOf {
        _foodMenuMap.filter { it.value.restaurantId == resid && it.value.category == typeInRestaurantScreen }.values.toList()
    }
    val foodMenuListIsLoading : StateFlow<Boolean> = restaurantScreenRepository.foodMenuListIsLoading


    private val _snackMenuMap = mutableStateMapOf<String, Snack>()
    val snackMenuList = derivedStateOf {
        _snackMenuMap.filter { it.value.restaurantId == resid }.values.toList()
    }
    val snacksIsLoading : StateFlow<Boolean> = restaurantScreenRepository.snacksIsLoading


    private val _drinkMenuMap = mutableStateMapOf<String, Drink>()
    val drinkMenuList = derivedStateOf {
        _drinkMenuMap.filter { it.value.restaurantId == resid }.values
    }
    val drinkMenuIsLoading : StateFlow<Boolean> = restaurantScreenRepository.drinkMenuIsLoading


    val _restaurantOffersMenuList = mutableStateMapOf<String, Offers>()
    val restaurantOffersMenuList = derivedStateOf {
        _restaurantOffersMenuList.filter { it.value.restaurantId == resid }.values.toList()
    }
    val restaurantOffersLoading : StateFlow<Boolean> = restaurantScreenRepository.restaurantOffersLoading


    init {
        viewModelScope.launch {
            networkObserver.isNetworkAvailable.collect { available ->
                isNetworkAvailable = available
            }
        }
    }

    fun loadRestaurantId(resId : Int){
        resid = resId
    }

    fun restaurantData(){
        val restaurantscount = restaurantCount.get(resid)
        if(restaurantscount != null){
            viewModelScope.launch {
                if(foodMenuList.value.size < restaurantscount.meals){
                    val foodMenu = restaurantScreenRepository.uploadFoodMenuFromApi(resid)
                    _foodMenuMap += foodMenu
                }
                if(snackMenuList.value.size < restaurantscount.snacks){
                    val snackMenu = restaurantScreenRepository.uploadSnacksMenuFromApi(resid)
                    _snackMenuMap += snackMenu
                }
                if(restaurantOffersMenuList.value.size < restaurantscount.offers){
                    _restaurantOffersMenuList += restaurantScreenRepository.uploadRestaurantOffersFromApi(resid)
                }
            }
        }
    }

    fun selectedTypeInFavoriteScreen(index : Int, restaurantId : Int){
        viewModelScope.launch {
            val restaurant = favoriteRepository.getRestaurantToView(restaurantId)
            selectedTypeIndex = index
            typeInRestaurantScreen = restaurant?.typ?.toList()?.first() ?: ""
        }
    }

    fun selectedtype(index : Int, type : String){
        selectedTypeIndex = index
        typeInRestaurantScreen = type
    }
}