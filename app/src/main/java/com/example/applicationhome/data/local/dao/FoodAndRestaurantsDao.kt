package com.example.applicationhome.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.applicationhome.data.local.entity.CategoriesEntity
import com.example.applicationhome.data.local.entity.MealWithFavoriteStatus
import com.example.applicationhome.data.local.entity.MealsEntity
import com.example.applicationhome.data.local.entity.OffersEntity
import com.example.applicationhome.data.local.entity.RestaurantCategoryCrossRef
import com.example.applicationhome.data.local.entity.RestaurantWithFavoriteStatus
import com.example.applicationhome.data.local.entity.RestaurantsEntity
import com.example.applicationhome.data.local.entity.SearchHistory
import com.example.applicationhome.data.local.entity.SnackWithFavoriteStatus
import com.example.applicationhome.data.local.entity.SnacksEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodAndRestaurantsDao {

    //----------------------------------------------------------------\\ Sync Data //----------------------------------------------------------------

    @Upsert
    suspend fun syncMealsToDatabase(meals : List<MealsEntity>)

    @Upsert
    suspend fun syncSnacksToDatabase(snacks : List<SnacksEntity>)

    @Upsert
    suspend fun syncRestaurantsToDatabase(restaurants : List<RestaurantsEntity>)

    @Upsert
    suspend fun syncRestaurantCategoryCrossRef(categories : List<RestaurantCategoryCrossRef>)

    @Transaction
    suspend fun syncRestaurantsAndCategoriesTransaction(
        restaurants: List<RestaurantsEntity>,
        categories: List<RestaurantCategoryCrossRef>
    ) {
        syncRestaurantsToDatabase(restaurants)
        syncRestaurantCategoryCrossRef(categories)
    }

    @Upsert
    suspend fun syncCategoriesToDatabase(categories : List<CategoriesEntity>)

    @Upsert
    suspend fun syncOffersToDatabase(offers : List<OffersEntity>)


    //----------------------------------------------------------------\\ Get Data //----------------------------------------------------------------

    @Query("SELECT id FROM meals_entity")
    suspend fun getMealsIdsFromDatabase(): List<Int>

    @Query("SELECT id FROM snacks_entity")
    suspend fun getSnacksIdsFromDatabase(): List<Int>

    @Query("SELECT id FROM restaurants_entity")
    suspend fun getRestaurantsIdsFromDatabase(): List<Int>


    @Transaction
    @Query("SELECT * FROM meals_entity WHERE restaurantId = :restaurantId AND category =:type")
    fun getMealsFromDatabase(restaurantId : Int, type : String): PagingSource<Int, MealWithFavoriteStatus>

    @Transaction
    @Query("SELECT * FROM meals_entity WHERE id = :mealId")
    fun getOneMealFromDatabase(mealId : Int) : Flow<MealWithFavoriteStatus?>

    @Transaction
    @Query("SELECT * FROM snacks_entity WHERE restaurantId = :restaurantId")
    fun getSnacksFromDatabase(restaurantId : Int): PagingSource<Int, SnackWithFavoriteStatus>

    @Transaction
    @Query("SELECT * FROM snacks_entity WHERE id = :snackId")
    fun getOneSnackFromDatabase(snackId : Int) : Flow<SnackWithFavoriteStatus?>

    @Transaction
    @Query("""
            SELECT DISTINCT restaurants_entity.* FROM restaurants_entity
            LEFT JOIN restaurant_category_cross_ref ON restaurants_entity.id = restaurant_category_cross_ref.restaurantId
            LEFT JOIN categories_entity ON categories_entity.id = restaurant_category_cross_ref.categoryId
            WHERE :type = 'All' OR categories_entity.type =:type
            """)
    fun getRestaurantsFromDatabaseByCategories(type: String): PagingSource<Int, RestaurantWithFavoriteStatus>

    @Transaction
    @Query("SELECT * FROM restaurants_entity WHERE id IN (:resIds)")
    fun getRestaurantsFromDatabaseByIds(resIds : List<Int>): PagingSource<Int, RestaurantWithFavoriteStatus>

    @Transaction
    @Query("SELECT * FROM restaurants_entity WHERE id = :restaurantId")
    fun getOneRestaurantFromDatabase(restaurantId : Int): Flow<RestaurantWithFavoriteStatus?>

    @Query("SELECT * FROM categories_entity")
    fun getAllCategoriesFromDatabase(): Flow<List<CategoriesEntity>>

    @Query("SELECT * FROM offers_entity")
    fun getAllOffersFromDatabase(): Flow<List<OffersEntity>>

    @Query("SELECT * FROM offers_entity WHERE restaurantId = :resId")
    fun getRestaurantOffersFromDatabase(resId : Int): Flow<List<OffersEntity>>


    //----------------------------------------------------------------\\ Search //----------------------------------------------------------------

    @Query("""
            SELECT r.searchKeywords FROM restaurants_entity r
            INNER JOIN search_fts fts ON r.id = fts.rowid
            WHERE search_fts MATCH :searchText LIMIT 10
            """)
    fun getSearchSuggestions(searchText: String): Flow<List<String>>

    @Transaction
    @Query("""
        SELECT restaurants_entity.* FROM restaurants_entity 
        INNER JOIN search_fts ON restaurants_entity.id = search_fts.rowid 
        WHERE search_fts MATCH :searchText || '*'
    """)
    fun getRestaurantSearchResults(searchText: String): PagingSource<Int, RestaurantWithFavoriteStatus>

    @Transaction
    @Query("SELECT * FROM meals_entity WHERE id IN (:mealIds)")
    suspend fun getTopFiveMealsToView(mealIds: List<Int>): List<MealWithFavoriteStatus>


    //----------------------------------------------------------------\\ Search History //----------------------------------------------------------------

    @Query("SELECT * FROM search_history WHERE userId = :userid")
    fun getSearchHistory(userid : String): Flow<List<SearchHistory>>

    @Upsert
    suspend fun addSearchTextToHistory(searchHistory : SearchHistory)

    @Query("UPDATE OR REPLACE search_history SET userId = :userId WHERE userId = ''")
    suspend fun addGuestSearchHistoryToUser(userId: String)

    @Query("DELETE FROM search_history WHERE title = :searchTitle")
    suspend fun deleteFromSearchHistory(searchTitle : String)
}