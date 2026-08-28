package com.example.applicationhome.core.domain.Implementations

import com.example.applicationhome.core.domain.model.ordersDatabaseClassToOrderUiClass
import com.example.applicationhome.core.domain.repository.OrderRepository
import com.example.applicationhome.data.data.model.HomeUiState
import com.example.applicationhome.data.data.model.OrderHistoryClass
import com.example.applicationhome.data.data.model.OrderStatesEnum
import com.example.applicationhome.data.data.model.OrderUiClass
import com.example.applicationhome.data.data.model.OrdersClass
import com.example.applicationhome.data.datastore.DataStoreManager
import com.example.applicationhome.data.local.dao.FoodAndRestaurantsDao
import com.example.applicationhome.data.local.dao.OrdersDao
import com.example.applicationhome.data.local.entity.OrdersDatabaseClass
import com.example.applicationhome.data.remote.FoodAppAPIs
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class OrderRepositoryImpl @Inject constructor(
    private val ordersDao : OrdersDao,
    private val foodAndRestaurantsDao : FoodAndRestaurantsDao,
    private val dataStoreManager : DataStoreManager,
    private val api : FoodAppAPIs
): OrderRepository {
    private suspend fun <T> retryLocally(
        times : Int = 3,
        initialDelay : Long = 1500,
        block : suspend  () -> T
    ): T {
        var currentDelay = initialDelay

        repeat(times - 1){
            try {
                return block()
            } catch (e: Exception) {
                if(e is CancellationException) throw e
                delay(currentDelay.milliseconds)
                currentDelay *= 2
            }
        }
        return block()
    }

    override fun getOrdersHistoryFromDatabase(userId : String, state : List<OrderStatesEnum>)
    : Flow<List<OrderUiClass>> =
        ordersDao.getOrders(userId, state.map { it.rawValue })
            .map { item ->
                item.map {
                    it.ordersDatabaseClassToOrderUiClass()
                }
            }


    override suspend fun uploadOrderRequest(orderClass : OrdersClass, userId: String): Result<Unit> {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val orderHistoryFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
        val date = current.format(formatter)
        val orderHistoryDate = current.format(orderHistoryFormatter)

        val orderId = System.currentTimeMillis()

        val result = retryLocally{
            val response = api.putNewOrder(
                userId,
                orderId,
                orderClass.copy(
                    date = date,
                    orderHistory = listOf(
                        OrderHistoryClass(
                            date = orderHistoryDate,
                            state = orderClass.state,
                            details = ""
                        )
                    ),
                    updatedAt = orderId
                )
            )
            if(response.isSuccessful){
                Result.success(Unit)
            }else{
                val errorCode = response.code()

                val errorMessage = when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }
                Result.failure(Exception(errorMessage))
            }
        }
        return result
    }

    override suspend fun cancelOrder(
        userId : String,
        orderId : Long,
        index : Int
    ): Result<Unit> = runCatching {
        val current = LocalDateTime.now()
        val orderHistoryFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
        val orderHistoryDate = current.format(orderHistoryFormatter)

        val newUpdateTime = System.currentTimeMillis()

        retryLocally {
            val cancelState = OrderHistoryClass(
                date = orderHistoryDate,
                state = OrderStatesEnum.CANCELLED.rawValue,
                details = "You cancelled your order on $orderHistoryDate"
            )
            val updates = mapOf(
                "state" to OrderStatesEnum.CANCELLED.rawValue,
                "orderHistory/$index" to cancelState,
                "updatedAt" to newUpdateTime
            )

            val response = api.cancelOrder(
                userId = userId,
                orderId = orderId,
                updates = updates
            )

            if(response.isSuccessful){
                Unit
            }else{
                throw IOException("Server Error code: ${response.code()}")
            }
        }
    }

    override suspend fun getOrders(userId: String) : HomeUiState {
        return try {
            val lastSyncTimestamp = dataStoreManager.ordersHistoryLastSyncTimeFlow.firstOrNull() ?: 0L

            val response = api.getLastOrders(
                userId = userId,
                lastSyncTimestamp = lastSyncTimestamp
            )
            val orders = response.body()

            if(response.isSuccessful && orders != null){
                val ordersHistory = orders.map { (key, value) ->
                    OrdersDatabaseClass(
                        orderId = key,
                        userId = userId,
                        date = value.date,
                        state = value.state,
                        subtotal = value.subtotal,
                        delivery = value.delivery,
                        service = value.service,
                        totalPrice = value.totalPrice,
                        restaurantName = value.restaurantName,
                        restaurantImage = value.restaurantImage,
                        restaurantId = value.restaurantId,
                        userInformation = value.userInformation,
                        orderItems = value.orderItems,
                        orderHistory = value.orderHistory,
                        updatedAt = value.updatedAt
                    )
                }

                ordersDao.addNewOrders(ordersHistory)

                val newUpdateTime = ordersHistory.maxOfOrNull { it.updatedAt }?: lastSyncTimestamp
                dataStoreManager.updateOrdersHistorySyncTime(newUpdateTime)

                HomeUiState.Success
            }else{
                val errorCode = response.code()

                when (errorCode) {
                    401 -> "Unauthorized error ($errorCode)"
                    404 -> "Not found ($errorCode)"
                    in 500..599 -> "Server down ($errorCode)"
                    else -> "HTTP Error: $errorCode"
                }
                HomeUiState.Offline
            }
        } catch (e : Exception){
            HomeUiState.Offline
        }
    }

    override suspend fun checkAreMealsDeleted(
        mealsIds : List<Int>,
        snacksIds : List<Int>
    ): Boolean {
        val distinctMeals = mealsIds.distinct()
        val distinctSnacks = snacksIds.distinct()

        val result = foodAndRestaurantsDao.checkAll(
            mealsIds = distinctMeals,
            snacksIds = distinctSnacks
        )
        val meals = result.first
        val snacks = result.second

        return meals.size != distinctMeals.size ||
                snacks.size != distinctSnacks.size
    }

    override suspend fun filterOrderItems(
        mealsIds : List<Int>,
        snacksIds : List<Int>
    ): List<Int> {
        val distinctMeals = mealsIds.distinct()
        val distinctSnacks = snacksIds.distinct()

        val result = foodAndRestaurantsDao.checkAll(
            mealsIds = distinctMeals,
            snacksIds = distinctSnacks
        )

        val currentList = result.first + result.second

        return currentList
    }

    override suspend fun isRestaurantExist(resId : Int): Boolean {
        return foodAndRestaurantsDao.isRestaurantExist(resId)
    }

    override suspend fun getRestaurantImage(resId : Int): String{
        return foodAndRestaurantsDao.getRestaurantImage(resId)
    }
    override suspend fun getMealsImages(ids : List<Int>): Map<Int, String?>{
        return foodAndRestaurantsDao.getMealsImages(ids)
    }
    override suspend fun getSnacksImages(ids : List<Int>): Map<Int, String?>{
        return foodAndRestaurantsDao.getSnacksImages(ids)
    }
}