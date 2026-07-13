package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.data.local.entity.CartItemsClass
import com.example.applicationhome.data.data.local.entity.FavoriteFoodDatabase
import com.example.applicationhome.data.data.local.entity.FavoriteRestaurantDatabase
import com.example.applicationhome.data.data.local.entity.FavoriteSnacksDatabase
import com.example.applicationhome.data.data.remote.NetworkObserver
import com.example.applicationhome.data.data.repository.CartRepository
import com.example.applicationhome.data.data.repository.FavoriteRepository
import com.example.applicationhome.data.data.repository.UserRepository
import com.example.applicationhome.domain.CartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteViewModel @Inject constructor(
    private val userRepository: UserRepository,
    cartRepository: CartRepository,
    private val cartUseCase: CartUseCase,
    private val favoriteRepository : FavoriteRepository,
    private val networkObserver: NetworkObserver
) : ViewModel(){
    var isNetworkAvailable by mutableStateOf(false)


    val userData = userRepository.userData

    var errorInCart by mutableStateOf(false)


    val cartItems = cartRepository.cartItems


    var totalPrice by mutableDoubleStateOf(0.0)

    var newCount by mutableStateOf(0)

    var newFoodInCart by mutableStateOf<CartItemsClass?>(null)
    var newFoodInCartSize by mutableStateOf<String?>(null)


    var selectedCategorieInFavoriteScreen by mutableIntStateOf(0)

    var userId by mutableStateOf("")

    val favoriteMeals : StateFlow<List<FavoriteFoodDatabase>> =
        userRepository.userData.flatMapLatest { user ->
            val id = user.id
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
        userRepository.userData.flatMapLatest { user ->
            val id = user.id
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
        userRepository.userData.flatMapLatest { user ->
            val id = user.id
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
            userRepository.userData.collect { user ->
                val id = user.id
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

    fun plus(food: CartItemsClass, size : String){
        viewModelScope.launch(Dispatchers.IO) {
            val userId = userRepository.userData.value.id
            val state = cartUseCase.plus(userId, food, size)
            if(state != null){
                alertDialogTrue()
                newFoodInCartSize = state.first
                newFoodInCart = state.second
            }
        }
    }

    fun minus(food: CartItemsClass, size : String){
        viewModelScope.launch(Dispatchers.IO) {
            val userId = userRepository.userData.value.id
            cartUseCase.minus(userId, food, size)
        }
    }

    fun delete(foodId: Int, size : String){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            cartUseCase.delete(userId, foodId, size)
        }
    }

    fun quantity(snackKey : String) = cartItems.value.find { it?.mealKey == snackKey }?.quantity ?: 0

    fun deletenewCount(){
        newCount = 0
    }

    fun alertDialogTrue(){
        errorInCart = true
    }
}