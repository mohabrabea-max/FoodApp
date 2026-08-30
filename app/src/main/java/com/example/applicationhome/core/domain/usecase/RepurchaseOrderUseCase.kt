package com.example.applicationhome.core.domain.usecase

import com.example.applicationhome.core.domain.model.orderItemsClassToCartItemsClass
import com.example.applicationhome.core.domain.repository.OrderRepository
import com.example.applicationhome.data.data.model.CategoryEnum
import com.example.applicationhome.data.data.model.OrderItemsClass
import com.example.applicationhome.data.data.model.OrderUiClass
import com.example.applicationhome.data.data.model.RepurchaseOrderStates
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class RepurchaseOrderUseCase @Inject constructor(
    private val orderRepository : OrderRepository,
    private val cartUseCase : CartUseCase
){
    suspend fun repurchaseOrder(order : OrderUiClass, orderItems : List<OrderItemsClass>): RepurchaseOrderStates = coroutineScope{
        val isRestaurantExist = orderRepository.isRestaurantExist(order.restaurantId)
        if(!isRestaurantExist){
           return@coroutineScope RepurchaseOrderStates.RestaurantIsDeleted
        }

        val ids = extractMealAndSnackIds(orderItems)
        val mealsIds = ids.first
        val snacksIds = ids.second

        val result = orderRepository.checkAreMealsDeleted(mealsIds, snacksIds)
        if(result){
            return@coroutineScope RepurchaseOrderStates.MealsAreDeleted
        }

        val resImageDeferred = async { orderRepository.getRestaurantImage(order.restaurantId) }
        val mealsImagesDeferred = async { orderRepository.getMealsImages(mealsIds) }
        val snacksImagesDeferred = async { orderRepository.getSnacksImages(snacksIds) }

        val resImage = resImageDeferred.await()
        val mealsImages = mealsImagesDeferred.await()
        val snacksImages = snacksImagesDeferred.await()

        val items = orderItems.map {
            it.orderItemsClassToCartItemsClass(
                userId = order.userId,
                resId = order.restaurantId,
                image = when{
                    it.type == CategoryEnum.SNACKS.rawValue -> {
                        snacksImages[it.mealId] ?: it.image
                    }
                    else -> {
                        mealsImages[it.mealId] ?: it.image
                    }
                }
            )
        }

        cartUseCase.clearAllCart(order.userId)

        cartUseCase.addMoreThanOneItem(
            userId = order.userId,
            foods = items,
            resId = order.restaurantId,
            resName = order.restaurantName,
            resImage = resImage
        )

        return@coroutineScope RepurchaseOrderStates.Success
    }


    suspend fun filterOrderItems(order : OrderUiClass): RepurchaseOrderStates {
        val orderItems = order.orderItems

        val ids = extractMealAndSnackIds(orderItems)
        val mealsIds = ids.first
        val snacksIds = ids.second

        val result = orderRepository.filterOrderItems(mealsIds, snacksIds)

        val newOrderItems = order.orderItems.filter { it.mealId in result }
        if(newOrderItems.isEmpty()){
            return RepurchaseOrderStates.ALLMealsAreDeleted
        }

        val repurchaseResult = repurchaseOrder(order, newOrderItems)

        return repurchaseResult
    }


    private fun extractMealAndSnackIds(items: List<OrderItemsClass>): Pair<List<Int>, List<Int>> {
        val mealsIds = items
            .filter {
                it.type != CategoryEnum.SNACKS.rawValue &&
                        it.type != CategoryEnum.DRINK.rawValue
            }.map { it.mealId }

        val snacksIds = items.filter { it.type == CategoryEnum.SNACKS.rawValue }.map { it.mealId }

        return Pair(mealsIds, snacksIds)
    }
}