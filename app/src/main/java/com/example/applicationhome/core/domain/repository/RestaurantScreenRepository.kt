package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.data.data.model.Offers
import com.example.applicationhome.data.local.dao.FoodAndRestaurantsDao
import com.example.applicationhome.data.local.entity.MealsEntity
import com.example.applicationhome.data.local.entity.SnacksEntity
import com.example.applicationhome.data.remote.FoodAppAPIs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantScreenRepository @Inject constructor(
    private val foodAndRestaurantsDao : FoodAndRestaurantsDao,
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


    fun getMealsFromDatabase(resId : Int): Flow<List<MealsEntity>> =
        foodAndRestaurantsDao.getMealsFromDatabase(resId)

    fun getSnacksFromDatabase(resId : Int): Flow<List<SnacksEntity>> =
        foodAndRestaurantsDao.getSnacksFromDatabase(resId)

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