package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.data.local.entity.SnackWithFavoriteStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemScreenRepository @Inject constructor() {
    private val _selectedMeal = MutableStateFlow<MealWithFavoriteStatus?>(null)
    val selectedMeal : StateFlow<MealWithFavoriteStatus?> = _selectedMeal.asStateFlow()
    private val _mealSize = MutableStateFlow("")
    val mealSize : StateFlow<String> = _mealSize.asStateFlow()

    private val _selectedSnack = MutableStateFlow<SnackWithFavoriteStatus?>(null)
    val selectedSnack : StateFlow<SnackWithFavoriteStatus?> = _selectedSnack.asStateFlow()
    private val _snackSize = MutableStateFlow("")
    val snackSize : StateFlow<String> = _snackSize.asStateFlow()


    fun selectMeal(meal : MealWithFavoriteStatus, size : String){
        _selectedMeal.value = meal
        _mealSize.value = size
    }

}