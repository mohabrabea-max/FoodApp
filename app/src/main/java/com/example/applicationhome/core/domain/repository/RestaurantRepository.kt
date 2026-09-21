package com.example.applicationhome.core.domain.repository

import androidx.paging.PagingData
import com.example.applicationhome.core.data.local.entity.OffersEntity
import com.example.applicationhome.core.domain.model.MealDomain
import com.example.applicationhome.core.domain.model.RestaurantDomainClass
import com.example.applicationhome.core.domain.model.SnackDomain
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {
    suspend fun getRestaurantByIdFromDatabase(resId : Int): RestaurantDomainClass?
    suspend fun getMealByIdFromDatabase(mealId : Int): MealDomain?
    suspend fun getSnackByIdFromDatabase(snackId : Int): SnackDomain?
    fun getMealsFromDatabase(resId : Int, type : String): Flow<PagingData<MealDomain>>
    fun getSnacksFromDatabase(resId : Int): Flow<PagingData<SnackDomain>>
    fun getRestaurantOffersFromDatabase(resId : Int): Flow<List<OffersEntity>>

    fun getDiscountMeals(resId : Int): Flow<List<MealDomain>>
    fun getDiscountSnacks(resId : Int): Flow<List<SnackDomain>>
}