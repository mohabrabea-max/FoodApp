package com.example.applicationhome.data.models.repository

import androidx.compose.runtime.mutableStateMapOf
import com.example.applicationhome.data.models.model.Categories
import com.example.applicationhome.data.models.model.Offers
import com.example.applicationhome.data.models.model.Restaurants
import com.example.applicationhome.data.models.model.RestaurantsCount
import com.example.applicationhome.data.models.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class HomeScreenRepository() {
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
            val restaurants = RetrofitInstance.api.getRestaurantCount()
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

    suspend fun uploadRestaurantsFromApi(): Map<String, Restaurants> {
        val restaurants = try {
            _restaurantsMenuIsLoading.value = true
            val response = RetrofitInstance.api.restaurants()
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

    suspend fun uploadCategorieslistFromApi(): List<Categories> {
        val categoriesList = try {
            _categoriesIsLoading.value = true
            RetrofitInstance.api.categorieslist()
        } catch (e: Exception) {
            emptyList()
        } finally {
            _categoriesIsLoading.value = false
        }
        return categoriesList
    }

    suspend fun uploadOffersFromApi(): List<Offers> {
        val offers = try {
            _offersIsLoading.value = true
            val response = RetrofitInstance.api.offers()
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