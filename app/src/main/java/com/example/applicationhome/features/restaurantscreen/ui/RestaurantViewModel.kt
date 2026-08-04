package com.example.applicationhome.features.restaurantscreen.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.RestaurantScreenRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.domain.usecase.CartUseCase
import com.example.applicationhome.core.domain.usecase.GetFavoriteUseCase
import com.example.applicationhome.data.data.model.BottomSheetItem
import com.example.applicationhome.data.data.model.Drink
import com.example.applicationhome.data.data.model.RestaurantUiState
import com.example.applicationhome.data.local.entity.CartItemsClass
import com.example.applicationhome.data.local.entity.FavoriteMealEntity
import com.example.applicationhome.data.local.entity.FavoriteRestaurantEntity
import com.example.applicationhome.data.local.entity.FavoriteSnackEntity
import com.example.applicationhome.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.data.local.entity.OffersEntity
import com.example.applicationhome.data.local.entity.SnackWithFavoriteStatus
import com.example.applicationhome.data.remote.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RestaurantViewModel @Inject constructor(
    savedStateHandle : SavedStateHandle,
    private val networkObserver : NetworkObserver,
    cartRepository : CartRepository,
    private val restaurantScreenRepository : RestaurantScreenRepository,
    private val userRepository : UserRepository,
    private val cartUseCase : CartUseCase,
    private val getFavoriteUseCase : GetFavoriteUseCase
): ViewModel(){

    val userData = userRepository.userData


//    *** ---------------------------- \\***  Restaurant Items  ***// ---------------------------- ***

    private val _resId = MutableStateFlow(0)

    private val _selectedTypeIndex = MutableStateFlow(0)
    val selectedTypeIndex : StateFlow<Int> = _selectedTypeIndex.asStateFlow()
    private val _typeInRestaurantScreen = MutableStateFlow("")
    val typeInRestaurantScreen : StateFlow<String> = _typeInRestaurantScreen.asStateFlow()

    private val _mealSize = MutableStateFlow("")
    val mealSize : StateFlow<String> = _mealSize.asStateFlow()

    val foodMenuList : Flow<PagingData<MealWithFavoriteStatus>> = combine(
        _resId,
        typeInRestaurantScreen
    ) { resId, type ->
        Pair(resId, type)
    }.flatMapLatest { (resId, type) ->
        restaurantScreenRepository.getMealsFromDatabase(resId, type)
    }.cachedIn(viewModelScope)

    val snackMenuList : Flow<PagingData<SnackWithFavoriteStatus>> =
        _resId.flatMapLatest { resId ->
            restaurantScreenRepository.getSnacksFromDatabase(resId)
        }.cachedIn(viewModelScope)

    private val _drinkMenuMap = MutableStateFlow<Map<String, Drink>>(emptyMap())

    val drinkMenuList = combine(
        _drinkMenuMap,
        _resId
    ) { menuMap, resId ->
        menuMap.filter { it.value.restaurantId == resId }.values
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    val restaurantOffersMenuList : StateFlow<List<OffersEntity>> =
        _resId.flatMapLatest{ id ->
            restaurantScreenRepository.getRestaurantOffersFromDatabase(id)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    val isNetworkAvailable = MutableStateFlow(false)

    private val _uiState = MutableStateFlow(RestaurantUiState())
    val uiState = _uiState.asStateFlow()


    init {
        _resId.value = checkNotNull(savedStateHandle["restaurantId"])
        val mealIdString : String? = savedStateHandle["mealId"]
        val mealId : Int? = mealIdString?.toIntOrNull()

        loadRestaurantDetails(_resId.value, mealId)

        viewModelScope.launch {
            networkObserver.isNetworkAvailable.collect { available ->
                isNetworkAvailable.value = available
            }
        }
    }


    private fun loadRestaurantDetails(restaurantId : Int, mealId : Int?){
        viewModelScope.launch {
            val restaurant = restaurantScreenRepository.getRestaurantByIdFromDatabase(restaurantId)

            val food = mealId?.let {
                restaurantScreenRepository.getMealByIdFromDatabase(it)
            }

            _uiState.update {
                it.copy(
                    restaurantData = restaurant,
                    bottomSheetItem = food?.let { meal -> BottomSheetItem.MealItem(meal) }
                )
            }

            restaurant.restaurant.typ.firstOrNull()?.let { firstType ->
                selectedtype(0, firstType)
            }
        }
    }

    fun selectedtype(index : Int, type : String){
        _selectedTypeIndex.value = index
        _typeInRestaurantScreen.value = type
    }

    fun selectMeal(item : MealWithFavoriteStatus, size : String){
        _uiState.update {
            it.copy(
                bottomSheetItem = BottomSheetItem.MealItem(item)
            )
        }
        _mealSize.value = size
    }

    fun selectSnack(snack : BottomSheetItem.SnackItem, size : String){
        _uiState.update {
            it.copy(
                bottomSheetItem = snack
            )
        }
        _mealSize.value = size
    }

    fun selectSize(size : String){
        _mealSize.value = size
    }

    fun closeBottomSheet(){
        _uiState.update {
            it.copy(
                bottomSheetItem = null
            )
        }

        _mealSize.value = ""

        deletenewCount()
    }


//       *** ---------------------------- \\***  Cart  ***// ---------------------------- ***

    var errorInCart = MutableStateFlow(Pair(false,""))

    val cartInformation = cartRepository.cartInformation

    val cartItems = cartRepository.cartItems

    val totalNumber = cartRepository.totalNumber

    val totalPrice = cartRepository.totalPrice

    val newFoodInCart = MutableStateFlow<CartItemsClass?>(null)
    val newFoodInCartSize = MutableStateFlow<String?>(null)

    val newCount = MutableStateFlow(0)


    fun plus(food: CartItemsClass, size : String){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            val state = cartUseCase.plus(userId, food, size)
            if(state != null){
                if(state.first == "User Id Is Empty"){
                    alertDialogTrue(Pair(true, "User Id Is Empty"))
                }else{
                    alertDialogTrue(Pair(true, "Error In Restaurant"))
                    newFoodInCartSize.value = state.first
                    newFoodInCart.value = state.second
                }
            }
        }
    }

    fun updateCount(food : CartItemsClass, size : String, newCount : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = userRepository.userData.value.id
            val state = cartUseCase.updateCount(userId, food, size, newCount)
            if(state != null){
                if(state.first == "User Id Is Empty"){
                    alertDialogTrue(Pair(true, "User Id Is Empty"))
                }else{
                    alertDialogTrue(Pair(true, "Error In Restaurant"))
                    newFoodInCartSize.value = state.first
                    newFoodInCart.value = state.second
                }
            }else{
                closeBottomSheet()
                deletenewCount()
            }
        }
    }

    fun minus(food: CartItemsClass, size : String){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            cartUseCase.minus(userId, food, size)
        }
    }

    fun clearAndStartNewCart(count : Int) {
        viewModelScope.launch {
            val newFood = newFoodInCart.value
            val newSize = newFoodInCartSize.value
            val userId = userRepository.userData.value.id
            val finally = cartUseCase.clearAndStartNewCart(userId, newFoodInCart.value, newFoodInCartSize.value)

            if(finally && newFood != null && newSize != null){
                cartUseCase.updateCount(userId, newFood, newSize, count)
                closeBottomSheet()
                deletenewCount()
                newFoodInCart.value = null
                newFoodInCartSize.value = null
            }
        }
    }

    fun delete(foodId: Int, size : String){
        viewModelScope.launch {
            val userId = userRepository.userData.value.id
            cartUseCase.delete(userId, foodId, size)
        }
    }

    private fun alertDialogTrue(error : Pair<Boolean, String>){
        errorInCart.value = error
    }

    fun alertDialogFalse(){
        errorInCart.value = Pair(false,"")
    }

    fun plusnewCount(){
        newCount.value += 1
    }

    fun minusnewCount(){
        newCount.value -= 1
    }

    fun deletenewCount(){
        newCount.value = 0
    }


    //       *** ---------------------------- \\***  Favorite  ***// ---------------------------- ***

    fun addItemInBottomSheetToFavorite(){
        viewModelScope.launch {
            when(_uiState.value.bottomSheetItem){
                is BottomSheetItem.MealItem -> {
                    val favoriteMealEntity = FavoriteMealEntity(
                        _uiState.value.bottomSheetItem?.id ?: 0,
                        userData.value.id,
                        _uiState.value.bottomSheetItem?.restaurantId ?: 0,
                        false,
                        false
                    )
                    addMealFavorite(favoriteMealEntity)
                }

                is BottomSheetItem.SnackItem -> {
                    val favoriteSnackEntity = FavoriteSnackEntity(
                        _uiState.value.bottomSheetItem?.id ?: 0,
                        userData.value.id,
                        _uiState.value.bottomSheetItem?.restaurantId ?: 0,
                        false,
                        false
                    )
                    addSnackFavorite(favoriteSnackEntity)
                }

                else -> {}
            }
        }
    }

    fun removeItemInBottomSheetToFavorite(){
        viewModelScope.launch {
            when(_uiState.value.bottomSheetItem){
                is BottomSheetItem.MealItem -> {
                    removeMealFavorite(_uiState.value.bottomSheetItem?.id ?: 0)
                }

                is BottomSheetItem.SnackItem -> {
                    removeSnackFavorite(_uiState.value.bottomSheetItem?.id ?: 0)
                }

                else -> {}
            }
        }
    }


    fun addMealFavorite(food : FavoriteMealEntity){
        viewModelScope.launch {
            getFavoriteUseCase.addMealFavorite(food)
        }
    }
    fun addSnackFavorite(snack : FavoriteSnackEntity){
        viewModelScope.launch {
            getFavoriteUseCase.addSnackFavorite(snack)
        }
    }
    fun addRestaurantsFavorite(restaurants: FavoriteRestaurantEntity){
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
}