package com.example.applicationhome.data.data.repository

import com.example.applicationhome.data.data.model.FoodItem
import com.example.applicationhome.data.data.model.Offers
import com.example.applicationhome.data.data.model.Snack
import com.example.applicationhome.data.data.remote.FoodAppAPIs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantScreenRepository @Inject constructor(
    private val api : FoodAppAPIs
) {
    private val _foodMenuListIsLoading = MutableStateFlow(true)
    val foodMenuListIsLoading : StateFlow<Boolean> = _foodMenuListIsLoading


    private val _snacksIsLoading = MutableStateFlow(true)
    val snacksIsLoading : StateFlow<Boolean> = _snacksIsLoading


    private val _drinkMenuIsLoading = MutableStateFlow(true)
    val drinkMenuIsLoading : StateFlow<Boolean> = _drinkMenuIsLoading


    private val _restaurantOffersLoading = MutableStateFlow(true)
    val restaurantOffersLoading : StateFlow<Boolean> = _restaurantOffersLoading


    suspend fun uploadFoodMenuFromApi(resId : Int): Map<String, FoodItem> {
        val foodMenu = try {
            _foodMenuListIsLoading.value = true
            val response = api.foodmenu("\"restaurantId\"", resId)
            val foodMenu = response.body()
            if(response.isSuccessful && foodMenu != null){
                foodMenu
            }else{
                emptyMap()
            }
        } catch (e: Exception) {
            emptyMap()
        } finally {
            _foodMenuListIsLoading.value = false
        }
        return foodMenu
    }

    suspend fun uploadSnacksMenuFromApi(resId : Int): Map<String, Snack> {
        val snacksMenu = try {
            _snacksIsLoading.value = true
            val response = api.snacksMenu("\"restaurantId\"", resId)
            val snacksmenu = response.body()
            if(response.isSuccessful && snacksmenu != null){
                snacksmenu
            }else{
                emptyMap()
            }
        } catch (e: Exception) {
            emptyMap()
        } finally {
            _snacksIsLoading.value = false
        }
        return snacksMenu
    }

    suspend fun uploadRestaurantOffersFromApi(resId : Int): Map<String, Offers> {
        val restaurantOffers = try {
            _restaurantOffersLoading.value = true
            val response = api.restaurantOffers("\"restaurantId\"", resId)
            val offers = response.body()
            if(response.isSuccessful && offers != null){
                offers
            }else{
                emptyMap()
            }
        } catch (e: Exception) {
            emptyMap()
        } finally {
            _restaurantOffersLoading.value = false
        }
        return restaurantOffers
    }
}