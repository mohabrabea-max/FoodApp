package com.example.applicationhome.ui.theme.model

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.NetworkObserver
import com.example.applicationhome.data.models.local.entity.FavoriteFoodDatabase
import com.example.applicationhome.data.models.local.entity.FavoriteRestaurantDatabase
import com.example.applicationhome.data.models.local.entity.FavoriteSnacksDatabase
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.repository.FavoriteRepository
import com.example.applicationhome.data.models.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteViewModel(
    userRepository: UserRepository,
    private val favoriteRepository : FavoriteRepository,
    application : Application
) : AndroidViewModel(application){
    private val networkObserver = NetworkObserver(application.applicationContext)
    var isNetworkAvailable by mutableStateOf(false)

    var selectedCategorieInFavoriteScreen by mutableIntStateOf(0)

    var userId by mutableStateOf("")

    val favoriteMeals : StateFlow<List<FavoriteFoodDatabase>> =
        userRepository.getActiveUserFromDatabase().flatMapLatest { user ->
            val id = user?.id ?: ""
            if(id.isNotEmpty()){
                userId = id
                favoriteRepository.getFoodFavoriteFromDatabase(id)
            }else{
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )


    val favoriteSnacks : StateFlow<List<FavoriteSnacksDatabase>> =
        userRepository.getActiveUserFromDatabase().flatMapLatest { user ->
            val id = user?.id ?: ""
            if (id.isNotEmpty()) {
                userId = id
                favoriteRepository.getSnacksFavoriteFromDatabase(id)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )


    val favoriteFoodCount : StateFlow<Int> = favoriteMeals
        .map { it.size }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0
        )

    val favoriteSnacksCount : StateFlow<Int> = favoriteSnacks
        .map { it.size }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0
        )


    val favoriteRestaurantsFromDatabase : StateFlow<List<FavoriteRestaurantDatabase>> =
        userRepository.getActiveUserFromDatabase().flatMapLatest { user ->
            val id = user?.id ?: ""
            if (id.isNotEmpty()){
                userId = id
                favoriteRepository.getRestaurantsFavoriteFromDatabase(id)
            }else{
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    val favoriteRestaurantsCount : StateFlow<Int> = favoriteRestaurantsFromDatabase
        .map { it.size }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0
        )


    init {
        viewModelScope.launch {
            userRepository.getActiveUserFromDatabase().collect { user ->
                val id = user?.id ?: ""

                if (id.isNotEmpty()) {
                    userId

                    networkObserver.isNetworkAvailable.collect { available ->
                        isNetworkAvailable = available

                        if (available) {
                            favoriteRepository.syncFavoritesInDatabase(id)
                        }
                    }
                }
            }
        }
    }


    fun addMealFavorite(food : FavoriteFoodDatabase){
        viewModelScope.launch {
            favoriteRepository.addFoodToFavorite(food.copy(userId = userId))
        }
    }
    fun addSnackFavorite(snack : FavoriteSnacksDatabase){
        viewModelScope.launch {
            favoriteRepository.addSnackToFavorite(snack.copy(userId = userId))
        }
    }
    fun addRestaurantsFavorite(restaurants: FavoriteRestaurantDatabase){
        viewModelScope.launch {
            favoriteRepository.addRestaurantToFavorite(restaurants.copy(userId = userId))
        }
    }

    fun removeMealFavorite(mealId : Int){
        viewModelScope.launch {
            favoriteRepository.deleteFoodFromFavorite(userId, mealId)
        }
    }
    fun removeSnackFavorite(snackId : Int){
        viewModelScope.launch {
            favoriteRepository.deleteSnackFromFavorite(userId, snackId)
        }
    }
    fun removeRestaurantsFavorite(resId : Int){
        viewModelScope.launch {
            favoriteRepository.deleteRestaurantFromFavorite(userId, resId)
        }
    }

    fun selectedFavoriteScreen(index: Int){
        selectedCategorieInFavoriteScreen = index
    }

    fun isMealInFavorite(foodId : Int): Flow<Boolean> {
        return favoriteMeals.map { list ->
            list.any{ it.mealId == foodId }
        }
    }

    fun isSnackInFavorite(snackId : Int): Flow<Boolean> {
        return favoriteSnacks.map { list ->
            list.any{ it.snackId == snackId }
        }
    }

    fun isRestaurantInFavorite(resId : Int): Flow<Boolean>{
        return favoriteRestaurantsFromDatabase.map { list ->
            list.any{ it.restaurantId == resId }
        }
    }

    suspend fun getRestaurantToView(resId : Int): Restaurants? =
        favoriteRepository.getRestaurantToView(resId)

    fun getMealToView(mealId : Int): FoodItem? =
        favoriteRepository.getMealToView("Meal_${mealId}")

    fun getSnackToView(snackId : Int): Snack? =
        favoriteRepository.getSnackToView("Snack_${snackId}")
}