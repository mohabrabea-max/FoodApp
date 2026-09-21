package com.example.applicationhome.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.applicationhome.core.data.local.dao.FoodAndRestaurantsDao
import com.example.applicationhome.core.data.local.entity.OffersEntity
import com.example.applicationhome.core.data.mapper.mealWithFavoriteStatusToMealDomain
import com.example.applicationhome.core.data.mapper.restaurantWithFavoriteStatusToRestaurantDomainClass
import com.example.applicationhome.core.data.mapper.snackWithFavoriteStatusToSnackDomain
import com.example.applicationhome.core.domain.model.MealDomain
import com.example.applicationhome.core.domain.model.RestaurantDomainClass
import com.example.applicationhome.core.domain.model.SnackDomain
import com.example.applicationhome.core.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val foodAndRestaurantsDao : FoodAndRestaurantsDao
): RestaurantRepository {
    override suspend fun getRestaurantByIdFromDatabase(resId : Int): RestaurantDomainClass? =
        foodAndRestaurantsDao.getOneRestaurantFromDatabase(resId)?.restaurantWithFavoriteStatusToRestaurantDomainClass()

    override suspend fun getMealByIdFromDatabase(mealId : Int): MealDomain? =
        foodAndRestaurantsDao.getOneMealFromDatabase(mealId)?.mealWithFavoriteStatusToMealDomain()

    override suspend fun getSnackByIdFromDatabase(snackId : Int): SnackDomain? =
        foodAndRestaurantsDao.getOneSnackFromDatabase(snackId)?.snackWithFavoriteStatusToSnackDomain()

    override fun getMealsFromDatabase(resId : Int, type : String): Flow<PagingData<MealDomain>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 3,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                foodAndRestaurantsDao.getMealsFromDatabase(resId, type)
            }
        ).flow.map { pagingData ->
            pagingData.map {
                it.mealWithFavoriteStatusToMealDomain()
            }
        }

    override fun getSnacksFromDatabase(resId : Int): Flow<PagingData<SnackDomain>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 3,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                foodAndRestaurantsDao.getSnacksFromDatabase(resId)
            }
        ).flow.map { pagingData ->
            pagingData.map {
                it.snackWithFavoriteStatusToSnackDomain()
            }
        }

    override fun getRestaurantOffersFromDatabase(resId : Int): Flow<List<OffersEntity>> =
        foodAndRestaurantsDao.getRestaurantOffersFromDatabase(resId)


    override fun getDiscountMeals(resId: Int): Flow<List<MealDomain>> =
        foodAndRestaurantsDao.getDiscountMeals(resId)
            .map { item ->
                item.map { it.mealWithFavoriteStatusToMealDomain() }
            }

    override fun getDiscountSnacks(resId: Int): Flow<List<SnackDomain>> =
        foodAndRestaurantsDao.getDiscountSnacks(resId)
            .map { item ->
                item.map { it.snackWithFavoriteStatusToSnackDomain() }
            }
}