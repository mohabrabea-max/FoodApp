package com.example.applicationhome.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.applicationhome.core.data.local.dao.FoodAndRestaurantsDao
import com.example.applicationhome.core.data.local.entity.SearchHistory
import com.example.applicationhome.core.data.mapper.mealWithFavoriteStatusToMealDomain
import com.example.applicationhome.core.data.mapper.restaurantWithFavoriteStatusToRestaurantDomainClass
import com.example.applicationhome.core.domain.model.MealDomain
import com.example.applicationhome.core.domain.model.RestaurantDomainClass
import com.example.applicationhome.core.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val foodAndRestaurantsDao : FoodAndRestaurantsDao
): SearchRepository {
    override fun getSearchSuggestions(searchText: String): Flow<List<String>>{
        val trimmedSearchText = searchText.trim()

        if(trimmedSearchText.isEmpty()){
            return flowOf(emptyList())
        }

        val formattedSearchText = "$trimmedSearchText*"
        return foodAndRestaurantsDao.getSearchSuggestions(formattedSearchText)
    }


    override fun getRestaurantSearchResults(searchText: String): Flow<PagingData<RestaurantDomainClass>>{
        return Pager(
            config = PagingConfig(
                pageSize = 10,         // حجم الدفعة (كل مرة يجيب 20 مطعم)
                prefetchDistance = 5,  // يبدأ يحمل الصفحة الجاية لما يتبقي 5 كروت بس في السكرول
                enablePlaceholders = false
            ),
            pagingSourceFactory = { foodAndRestaurantsDao.getRestaurantSearchResults(searchText) }
        ).flow.map { pagingData ->
            pagingData.map {
                it.restaurantWithFavoriteStatusToRestaurantDomainClass()
            }
        }
    }

    override suspend fun getTopFiveMealsToView(mealIds: List<Int>): List<MealDomain> =
        foodAndRestaurantsDao.getTopFiveMealsToView(mealIds)
            .map { it.mealWithFavoriteStatusToMealDomain() }

    override fun getSearchHistory(userid : String): Flow<List<SearchHistory>> =
        foodAndRestaurantsDao.getSearchHistory(userid)

    override suspend fun addSearchTextToHistory(searchHistory : SearchHistory){
        foodAndRestaurantsDao.addSearchTextToHistory(searchHistory)
    }

    override suspend fun addGuestSearchHistoryToUser(userId: String){
        foodAndRestaurantsDao.addGuestSearchHistoryToUser(userId)
    }

    override suspend fun deleteFromSearchHistory(searchTitle : String){
        foodAndRestaurantsDao.deleteFromSearchHistory(searchTitle)
    }
}