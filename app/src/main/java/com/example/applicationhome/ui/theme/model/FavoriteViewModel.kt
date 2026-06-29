package com.example.applicationhome.ui.theme.model

import android.app.Application
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.NetworkObserver
import com.example.applicationhome.data.models.local.FavoriteFoodDatabase
import com.example.applicationhome.data.models.local.FavoriteRestaurantDatabase
import com.example.applicationhome.data.models.model.FoodItem
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.model.Snack
import com.example.applicationhome.data.models.repository.FavoriteRepository
import com.example.applicationhome.data.models.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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

    var userId : String = ""

    private val _favoriteFoodFromDatabase : StateFlow<List<FavoriteFoodDatabase>> =
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
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val favoriteCount : Int by derivedStateOf{
        _favoriteFoodFromDatabase.value.size
    }

    val favoriteMeals : List<FavoriteFoodDatabase> by derivedStateOf{
        _favoriteFoodFromDatabase.value.filter { it.type == "Meal" }
    }

    val favoriteSnacks : List<FavoriteFoodDatabase> by derivedStateOf{
        _favoriteFoodFromDatabase.value.filter { it.type == "Snack" }
    }

    val favoriteRestaurantsFromDatabase : StateFlow<List<FavoriteRestaurantDatabase>> =
        userRepository.getActiveUserFromDatabase().flatMapLatest { user ->
            val id = user?.id ?: ""
            if (id.isNotEmpty()){
                favoriteRepository.getRestaurantsFavoriteFromDatabase(id)
            }else{
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    init {
        viewModelScope.launch {
            networkObserver.isNetworkAvailable.collect { available ->
                isNetworkAvailable = available
                favoriteRepository.syncFavoritesInDatabase(userId)
            }
        }
    }


    fun addFavorite(food : FavoriteFoodDatabase){
        viewModelScope.launch {
            favoriteRepository.addFoodToFavorite(food)
        }
    }
    fun addRestaurantsFavorite(restaurants: FavoriteRestaurantDatabase){
        viewModelScope.launch {
            favoriteRepository.addRestaurantToFavorite(restaurants)
        }
    }

    fun removeFavorite(mealId : Int){
        viewModelScope.launch {
            favoriteRepository.deleteFoodFromFavorite(userId, mealId)
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

    fun isMealInFavorite(foodId : Int): Boolean{
        return _favoriteFoodFromDatabase.value.any{ it.mealId == foodId }
    }

    fun getRestaurantToView(resId : Int): Restaurants? =
        favoriteRepository.getRestaurantToView("Restaurant_${resId}")

    fun getMealToView(mealId : Int): FoodItem? =
        favoriteRepository.getMealToView("Meal_${mealId}")

    fun getSnackToView(snackId : Int): Snack? =
        favoriteRepository.getSnackToView("Snack_${snackId}")
}