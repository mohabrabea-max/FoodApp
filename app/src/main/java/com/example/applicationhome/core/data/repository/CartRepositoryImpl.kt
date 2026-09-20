package com.example.applicationhome.core.data.repository

import com.example.applicationhome.core.data.local.dao.CartDao
import com.example.applicationhome.core.data.local.dao.FoodAndRestaurantsDao
import com.example.applicationhome.core.data.local.entity.CartClass
import com.example.applicationhome.core.data.local.entity.CartItemWithDiscount
import com.example.applicationhome.core.data.local.entity.CartItemsClassEntity
import com.example.applicationhome.core.data.mapper.cartItemsClassEntityToCartItemsDomainClass
import com.example.applicationhome.core.data.mapper.cartItemsDomainClassToCartItemsClass
import com.example.applicationhome.core.data.remote.FoodAppAPIs
import com.example.applicationhome.core.data.remote.dto.Restaurants
import com.example.applicationhome.core.domain.model.CartItemsDomainClass
import com.example.applicationhome.core.domain.module.ApplicationScope
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class CartRepositoryImpl @Inject constructor(
    userRepository : UserRepository,
    private val cartdao : CartDao,
    private val foodAndRestaurantsDao : FoodAndRestaurantsDao,
    private val api : FoodAppAPIs,
    @ApplicationScope private val externalScope : CoroutineScope
): CartRepository {
    override val cartInformation: StateFlow<CartClass?> =
        userRepository.userData
            .flatMapLatest { user ->
                val id = user.id
                if (id.isNotEmpty()) {
                    getCartData(id)
                }else {
                    flowOf(null)
                }
            }.stateIn(
                scope = externalScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    override val cartItems : StateFlow<List<CartItemsDomainClass>> =
        userRepository.userData
            .flatMapLatest { user ->
                val id = user.id
                if (id.isNotEmpty()) {
                    getCartItems(id)
                } else {
                    flowOf(emptyList())
                }
            }.stateIn(
                scope = externalScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    override val totalNumber: StateFlow<Int> =
        cartItems
            .map { item -> item.sumOf { it.quantity ?: 0 } }
            .stateIn(
                scope = externalScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0
            )

    override val totalPrice : StateFlow<Double> =
        cartItems.map { cartList ->
            cartList.sumOf { it.finalPriceOfOne * it.quantity }
        }.stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )


    override fun getCartItems(id : String): Flow<List<CartItemsDomainClass>> =
        cartdao.getCartItems(id).map { items ->
            items.map { it.cartItemsClass.cartItemsClassEntityToCartItemsDomainClass(it.discount) }
        }

    override suspend fun getOneCartItem(mealKey : String, userId : String): CartItemWithDiscount? =
        cartdao.getOneCartItem(mealKey, userId)


    override fun getCartData(id : String) : Flow<CartClass?> = cartdao.getParentCart(id)

    override suspend fun getCartRestaurantData(resId : Int) : Restaurants {
        return try {
            val response = api.getCarRestaurant("\"id\"", resId)
            val resData = response.body()?.values?.first()
            if(response.isSuccessful && resData != null){
                resData
            }else{
                Restaurants()
            }
        } catch (e : Exception){
            Restaurants()
        }
    }

    override suspend fun createNewCart(
        userId : String,
        food : CartItemsDomainClass,
        size : String,
        type : String,
        priceOfOne : Double,
        res : Restaurants,
        number: Int
    ) : String {
        val cartObject = CartClass(userId, res.id, res.name, res.image)

        return try {
            val cartItemsObject = food.cartItemsDomainClassToCartItemsClass()
            cartdao.createParentCart(cartObject)
            cartdao.addCartItem(cartItemsObject)
            "Success"
        } catch (e : Exception){
            "Error"
        } finally {
            ""
        }
    }

    override suspend fun createNewCartForMoreThanOneItem(
        userId : String,
        foods : List<CartItemsDomainClass>,
        resId : Int,
        resName : String,
        resImage : String
    ): String {
        val cartObject = CartClass(userId, resId, resName, resImage)

        return try {
            cartdao.createParentCart(cartObject)
            val items = foods.map { it.cartItemsDomainClassToCartItemsClass() }
            cartdao.updateMoreThanOneCartItem(items)
            "Success"
        } catch (e : Exception){
            "Error"
        } finally {
            ""
        }
    }

    override suspend fun addMealToCart(
        userId : String,
        food : CartItemsDomainClass,
        size : String,
        type : String,
        priceOfOne : Double,
        number: Int
    ): String{
        val mealKey = "${food.mealId}_$size"
        val cartItemsObject = CartItemsClassEntity(
            userId,
            mealKey,
            food.mealId,
            food.name,
            type,
            size,
            number,
            priceOfOne,
            priceOfOne * number,
            food.image,
            food.restaurantId
        )
        return try {
            cartdao.addCartItem(cartItemsObject)
            "Success"
        }catch (e : Exception){
            "Error"
        }
    }

    override suspend fun updateQuantity(
        userId : String,
        mealKey : String,
        size : String,
        priceOfOne : Double,
        number: Int
    ): String {
        return try {
            cartdao.updateCartItem(
                number,
                priceOfOne * number,
                userId,
                mealKey
            )
            "Success"
        }catch (e : Exception){
            "Error"
        }
    }

    override suspend fun deleteFromCart(userId : String, foodId: Int, size : String): String{
        val mealKey = "${foodId}_${size}"
        return try {
            cartdao.deleteItemFromCart(mealKey, userId)
            "Success"
        }catch (e : Exception){
            "Error"
        }
    }

    override suspend fun deleteParentCart(userId : String): String{
        return try {
            cartdao.deleteParentCart(userId)
            "Success"
        }catch (e : Exception){
            "خطأ في الشبكة: ${e.message}"
        }
    }

    override suspend fun deleteAllCart(userId : String): String{
        return try {
            cartdao.deleteAllItemFromCart(userId)
            "Success"
        }catch (e : Exception){
            "خطأ في الشبكة: ${e.message}"
        }
    }
}