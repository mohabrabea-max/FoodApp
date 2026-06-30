package com.example.applicationhome.ui.theme.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.models.model.FoodItemToCalculate
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.repository.FavoriteRepository
import kotlinx.coroutines.launch

class ItemScreenViewModel(
    private val favoriteRepository : FavoriteRepository
) : ViewModel() {
    var selectedSnak by mutableStateOf<FoodItemToCalculate?>(null)
    var selectedSnackSize by mutableStateOf("Small")
    var selectedItem by mutableStateOf<FoodItemToCalculate?>(null)
    var selectedSize by mutableStateOf("Small")
    var selectedRestaurant by mutableStateOf<Restaurants?>(null)


    fun selectItem(item: FoodItemToCalculate, size : String) {
        selectedItem = item
        selectedSize = size
    }
    fun selectSnak(item: FoodItemToCalculate?, size : String){
        selectedSnak = item
        selectedSnackSize = size
    }
    fun selectRestaurant(item : Restaurants){
        selectedRestaurant = item
        viewModelScope.launch {
            try {
                val newData = favoriteRepository.getRestaurantToView(item.id)
                selectedRestaurant = newData
            }catch (e : Exception){
                Log.e("SelectRestaurant", "Error fetching fresh data", e)
            }
        }
    }
}