package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.data.data.local.entity.CartItemsClass
import com.example.applicationhome.data.data.local.entity.FavoriteFoodDatabase
import com.example.applicationhome.data.data.local.entity.FavoriteRestaurantDatabase
import com.example.applicationhome.data.data.local.entity.FavoriteSnacksDatabase
import com.example.applicationhome.data.data.model.Drink
import com.example.applicationhome.data.data.model.FoodItem
import com.example.applicationhome.data.data.model.Offers
import com.example.applicationhome.data.data.model.Snack
import com.example.applicationhome.data.data.remote.NetworkObserver
import com.example.applicationhome.data.data.repository.CartRepository
import com.example.applicationhome.data.data.repository.FavoriteRepository
import com.example.applicationhome.data.data.repository.HomeScreenRepository
import com.example.applicationhome.data.data.repository.ItemScreenRepository
import com.example.applicationhome.data.data.repository.RestaurantScreenRepository
import com.example.applicationhome.data.data.repository.UserRepository
import com.example.applicationhome.domain.CartUseCase
import com.example.applicationhome.domain.GetFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val networkObserver: NetworkObserver,
    homeScreenRepository: HomeScreenRepository,
    cartRepository: CartRepository,
    private val restaurantScreenRepository : RestaurantScreenRepository,
    private val favoriteRepository: FavoriteRepository,
    private val userRepository: UserRepository,
    private val itemScreenRepository : ItemScreenRepository,
    private val cartUseCase : CartUseCase,
    private val getFavoriteUseCase : GetFavoriteUseCase
): ViewModel(){

    var isNetworkAvailable by mutableStateOf(false)

    val userData = userRepository.userData


//    *** ---------------------------- \\***  Restaurant Items  ***// ---------------------------- ***

    val resid = itemScreenRepository.resId

    val selectedTypeIndex = itemScreenRepository.selectedTypeIndex
    val typeInRestaurantScreen = itemScreenRepository.typeInRestaurantScreen

    val restaurantCount = homeScreenRepository.restaurantCount


    private val _foodMenuMap = mutableStateMapOf<String, FoodItem>()

    val foodMenuList : StateFlow<List<FoodItem>> = combine(
        snapshotFlow{ _foodMenuMap.toMap() },
        resid,
        typeInRestaurantScreen
    ){ menuMap, resId, type ->
        menuMap.filter {
            it.value.restaurantId == resId
                    && it.value.category == type
        }.values.toList()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val foodMenuListIsLoading : StateFlow<Boolean> = restaurantScreenRepository.foodMenuListIsLoading


    private val _snackMenuMap = mutableStateMapOf<String, Snack>()

    val snackMenuList = combine(
        snapshotFlow { _snackMenuMap.toMap() },
        resid
    ) { menuMap, resId ->
        menuMap.filter { it.value.restaurantId == resId }.values.toList()
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
    )

    val snacksIsLoading : StateFlow<Boolean> = restaurantScreenRepository.snacksIsLoading


    private val _drinkMenuMap = mutableStateMapOf<String, Drink>()

    val drinkMenuList = combine(
        snapshotFlow { _drinkMenuMap.toMap() },
        resid
    ){ menuMap, resId ->
        menuMap.filter { it.value.restaurantId == resId }.values
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val drinkMenuIsLoading : StateFlow<Boolean> = restaurantScreenRepository.drinkMenuIsLoading


    val _restaurantOffersMenuMap = mutableStateMapOf<String, Offers>()

    val restaurantOffersMenuList = combine(
        snapshotFlow { _restaurantOffersMenuMap.toMap() },
        resid
    ){ menuMap, resId ->
        menuMap.filter { it.value.restaurantId == resId }.values.toList()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val restaurantOffersLoading : StateFlow<Boolean> = restaurantScreenRepository.restaurantOffersLoading



    fun restaurantData(){
        val restaurantscount = restaurantCount[resid.value]
        if(restaurantscount != null){
            viewModelScope.launch {
                val foodMenu = async {
                    if(foodMenuList.value.size < restaurantscount.meals){
                      restaurantScreenRepository.uploadFoodMenuFromApi(resid.value)
                    } else { emptyMap() }
                }

                val snackMenu = async {
                    if(snackMenuList.value.size < restaurantscount.snacks){
                        restaurantScreenRepository.uploadSnacksMenuFromApi(resid.value)
                    } else { emptyMap() }
                }

                val offersMenu = async {
                    if(restaurantOffersMenuList.value.size < restaurantscount.offers){
                        restaurantScreenRepository.uploadRestaurantOffersFromApi(resid.value)
                    } else { emptyMap() }
                }

                _foodMenuMap += foodMenu.await()
                _snackMenuMap += snackMenu.await()
                _restaurantOffersMenuMap += offersMenu.await()
            }
        }
    }

    fun selectedtype(index : Int, type : String){
        itemScreenRepository.selectedTypeInRestaurant(index, type)
    }


//       *** ---------------------------- \\***  Cart  ***// ---------------------------- ***

    var errorInCart by mutableStateOf(false)

    val cartInformation = cartRepository.cartInformation

    val cartItems = cartRepository.cartItems

    val totalNumber = cartRepository.totalNumber

    var totalPrice by mutableDoubleStateOf(0.0)

    var newCount by mutableStateOf(0)

    var newFoodInCart by mutableStateOf<CartItemsClass?>(null)
    var newFoodInCartSize by mutableStateOf<String?>(null)



    init {
        viewModelScope.launch {
            networkObserver.isNetworkAvailable.collect { available ->
                isNetworkAvailable = available
            }
        }
        viewModelScope.launch{
            cartItems.collect { cartList ->
                updateTotals(cartList)
            }
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

    fun clearAndStartNewCart(count : Int) {
        viewModelScope.launch {
            totalPrice = 0.0
            val newFood = newFoodInCart
            val newSize = newFoodInCartSize
            val userId = userRepository.userData.value.id
            val finally = cartUseCase.clearAndStartNewCart(userId, newFoodInCart, newFoodInCartSize)

            if(finally && newFood != null && newSize != null){
                cartUseCase.updateCount(userId, newFood, newSize, count)
                deletenewCount()
                newFoodInCart = null
                newFoodInCartSize = null
            }
        }
    }

    fun delete(foodId: Int, size : String){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            cartUseCase.delete(userId, foodId, size)
        }
    }

    fun updateTotals(cartItems : List<CartItemsClass?>) {
        totalPrice = 0.0
        cartItems.forEach { item ->
            totalPrice += item?.totalPrice ?: 0.0
        }
    }

    fun quantity(snackKey : String) = cartItems.value.find { it?.mealKey == snackKey }?.quantity ?: 0

    fun deletenewCount(){
        newCount = 0
    }

    fun alertDialogTrue(){
        errorInCart = true
    }

    fun alertDialogFalse(){
        errorInCart = false
    }


    //       *** ---------------------------- \\***  Favorite  ***// ---------------------------- ***

    fun addMealFavorite(food : FavoriteFoodDatabase){
        viewModelScope.launch {
            getFavoriteUseCase.addMealFavorite(food)
        }
    }
    fun addSnackFavorite(snack : FavoriteSnacksDatabase){
        viewModelScope.launch {
            getFavoriteUseCase.addSnackFavorite(snack)
        }
    }
    fun addRestaurantsFavorite(restaurants: FavoriteRestaurantDatabase){
        viewModelScope.launch {
            getFavoriteUseCase.addRestaurantsFavorite(restaurants)
        }
    }


    fun removeMealFavorite(mealId : Int){
        viewModelScope.launch {
            getFavoriteUseCase.removeMealFavorite(mealId)
        }
    }
    fun removeSnackFavorite(snackId : Int){
        viewModelScope.launch {
            getFavoriteUseCase.removeSnackFavorite(snackId)
        }
    }
    fun removeRestaurantsFavorite(resId : Int){
        viewModelScope.launch {
            getFavoriteUseCase.removeRestaurantsFavorite(resId)
        }
    }


    fun isMealInFavorite(foodId : Int): Flow<Boolean> {
        return getFavoriteUseCase.isMealInFavorite(foodId)
    }
    fun isSnackInFavorite(snackId : Int): Flow<Boolean> {
        return getFavoriteUseCase.isSnackInFavorite(snackId)
    }
    fun isRestaurantInFavorite(resId : Int): Flow<Boolean> {
        return getFavoriteUseCase.isRestaurantInFavorite(resId)
    }


    //       *** ---------------------------- \\***  Item Screen  ***// ---------------------------- ***

    val selectedRestaurant = itemScreenRepository.selectedRestaurant


    fun selectMeal(item: FavoriteFoodDatabase, size : String) {
        itemScreenRepository.selectMeal(item, size)
    }
    fun selectSnack(item: FavoriteSnacksDatabase, size : String){
        itemScreenRepository.selectSnack(item, size)
    }
}