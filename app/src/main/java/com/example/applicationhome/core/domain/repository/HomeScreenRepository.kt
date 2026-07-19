package com.example.applicationhome.core.domain.repository

import androidx.compose.runtime.mutableStateMapOf
import com.example.applicationhome.data.data.model.Categories
import com.example.applicationhome.data.data.model.Offers
import com.example.applicationhome.data.data.model.Restaurants
import com.example.applicationhome.data.data.model.RestaurantsCount
import com.example.applicationhome.data.remote.FoodAppAPIs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeScreenRepository @Inject constructor(
    private val api : FoodAppAPIs
) {
    private val _restaurantsMenuIsLoading = MutableStateFlow(true)
    val restaurantsMenuIsLoading : StateFlow<Boolean> = _restaurantsMenuIsLoading

    private val _categoriesIsLoading = MutableStateFlow(true)
    val categoriesIsLoading : StateFlow<Boolean> = _categoriesIsLoading

    private val _offersIsLoading = MutableStateFlow(true)
    val offersIsLoading : StateFlow<Boolean> = _offersIsLoading


    private val _restaurantCount = mutableStateMapOf<Int, RestaurantsCount>()
    val restaurantCount : Map<Int, RestaurantsCount> get() = _restaurantCount


    suspend fun restaurantCount(){
        try {
            val restaurants = api.getRestaurantCount()
            val countList = restaurants.body()
            if(restaurants.isSuccessful && countList != null){
                _restaurantCount += countList
            }else{
                null
            }
        }catch (e : Exception){
            null
        }
    }

    suspend fun getRestaurantsFromApi(): Map<String, Restaurants> {
        val restaurants = try {
            _restaurantsMenuIsLoading.value = true
            val response = api.restaurants()
            val restaurants = response.body()
            if(response.isSuccessful && restaurants != null){
                restaurants
            }else{
                emptyMap()
            }
        } catch (e: Exception) {
            emptyMap()
        } finally {
            _restaurantsMenuIsLoading.value = false
        }
        return restaurants
    }

    suspend fun getCategorieslistFromApi(): List<Categories> {
        val categoriesList = try {
            _categoriesIsLoading.value = true
            api.categorieslist()
        } catch (e: Exception) {
            emptyList()
        } finally {
            _categoriesIsLoading.value = false
        }
        return categoriesList
    }

    suspend fun getOffersFromApi(): List<Offers> {
        val offers = try {
            _offersIsLoading.value = true
            val response = api.offers()
            val offers = response.body()
            if(response.isSuccessful && offers != null){
                offers
            }else{
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        } finally {
            _offersIsLoading.value = false
        }
        return offers
    }
}