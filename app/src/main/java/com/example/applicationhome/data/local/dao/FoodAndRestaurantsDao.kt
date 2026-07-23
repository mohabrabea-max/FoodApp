package com.example.applicationhome.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.applicationhome.data.local.entity.MealsEntity
import com.example.applicationhome.data.local.entity.RestaurantsEntity
import com.example.applicationhome.data.local.entity.SearchHistory
import com.example.applicationhome.data.local.entity.SnacksEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodAndRestaurantsDao {

    //----------------------------------------------------------------\\ Sync Data //----------------------------------------------------------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun syncMealsToDatabase(meals : List<MealsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun syncSnacksToDatabase(snacks : List<SnacksEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun syncRestaurantsToDatabase(restaurants : List<RestaurantsEntity>)


    //----------------------------------------------------------------\\ Get Data //----------------------------------------------------------------

    @Query("SELECT * FROM meals_entity WHERE restaurantId = :restaurantId")
    fun getMealsFromDatabase(restaurantId : Int): Flow<List<MealsEntity>>

    @Query("SELECT * FROM snacks_entity WHERE restaurantId = :restaurantId")
    fun getSnacksFromDatabase(restaurantId : Int): Flow<List<SnacksEntity>>

    @Query("SELECT * FROM restaurants_entity")
    fun getAllRestaurantsFromDatabase(): Flow<List<RestaurantsEntity>>

    @Query("SELECT * FROM restaurants_entity WHERE id IN (:resIds)")
    fun getRestaurantsFromDatabaseByIds(resIds : List<Int>): Flow<List<RestaurantsEntity>>

    @Query("SELECT * FROM restaurants_entity WHERE id = :restaurantId")
    suspend fun getOneRestaurantFromDatabase(restaurantId : Int): RestaurantsEntity


    //----------------------------------------------------------------\\ Search //----------------------------------------------------------------

    @Query("SELECT name FROM search_fts WHERE search_fts MATCH :searchText || '*' LIMIT 10")
    fun getSearchSuggestions(searchText: String): Flow<List<String>>

    @Query("""
        SELECT restaurants_entity.* FROM restaurants_entity 
        JOIN search_fts ON restaurants_entity.id = search_fts.rowid 
        WHERE search_fts MATCH :searchText || '*'
    """)
    fun getRestaurantSearchResults(searchText: String): PagingSource<Int, RestaurantsEntity>

    @Query("SELECT * FROM meals_entity WHERE id IN (:mealIds)")
    suspend fun getTopFiveMealsToView(mealIds: List<Int>): List<MealsEntity>


    //----------------------------------------------------------------\\ Search History //----------------------------------------------------------------

    @Query("SELECT * FROM search_history WHERE userId = :userid")
    fun getSearchHistory(userid : String): Flow<List<SearchHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSearchTextToHistory(searchHistory : SearchHistory)
}