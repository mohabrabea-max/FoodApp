package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.data.data.model.FoodItem
import com.example.applicationhome.data.data.model.Restaurants
import com.example.applicationhome.data.data.model.Snack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemScreenRepository @Inject constructor() {
    private val _selectedMeal = MutableStateFlow(FoodItem())
    val selectedMeal : StateFlow<FoodItem> = _selectedMeal.asStateFlow()
    private val _mealSize = MutableStateFlow("")
    val mealSize : StateFlow<String> = _mealSize.asStateFlow()

    private val _selectedSnack = MutableStateFlow(Snack())
    val selectedSnack : StateFlow<Snack> = _selectedSnack.asStateFlow()
    private val _snackSize = MutableStateFlow("")
    val snackSize : StateFlow<String> = _snackSize.asStateFlow()

    private val _selectedRestaurant = MutableStateFlow(Restaurants())
    val selectedRestaurant : StateFlow<Restaurants?> = _selectedRestaurant.asStateFlow()
    private val _resId = MutableStateFlow(0)
    val resId : StateFlow<Int> = _resId.asStateFlow()

    private val _selectedTypeIndex = MutableStateFlow(0)
    val selectedTypeIndex : StateFlow<Int> = _selectedTypeIndex.asStateFlow()

    private val _typeInRestaurantScreen = MutableStateFlow("")
    val typeInRestaurantScreen : StateFlow<String> = _typeInRestaurantScreen.asStateFlow()


    fun selectMeal(meal : FoodItem, size : String){
        _selectedMeal.value = meal
        _mealSize.value = size
    }

    fun selectSnack(snack : Snack, size : String){
        _selectedSnack.value = snack
        _snackSize.value = size
    }

    fun selectRestaurant(restaurant : Restaurants){
        _selectedRestaurant.value = restaurant
        _resId.value = restaurant.id
    }

    fun selectedTypeInRestaurant(index : Int, type : String){
        _selectedTypeIndex.value = index
        _typeInRestaurantScreen.value = type
    }
}