package com.example.applicationhome.core.domain.usecase

import com.example.applicationhome.core.data.local.entity.CartClass
import com.example.applicationhome.core.data.remote.dto.Restaurants
import com.example.applicationhome.core.domain.Implementations.FakeCartData
import com.example.applicationhome.core.domain.model.AddToCartStates
import com.example.applicationhome.core.domain.model.CartItemsDomainClass
import com.example.applicationhome.core.domain.model.CategoryEnum
import com.example.applicationhome.core.domain.model.MealDomain
import com.example.applicationhome.core.domain.model.SnackDomain
import com.example.applicationhome.core.domain.repository.CartRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartUseCaseTest {
    private val cartRepository = mockk<CartRepository>(relaxed = true)

    // *** ---------------------- \\***  Plus Test  ***// ---------------------- ***
    @Test
    fun plus_whenUserIdIsEmpty_shouldReturnErrorInLoginState() = runTest {
        val cartItem = FakeCartData.fakeCartItemsDomainClass()
        val emptyUserId = ""
        val emptySize = ""

        val cartUseCase = getCartUseCase(cartItem)
        val result = cartUseCase.plus(
            userId = emptyUserId,
            mealId = cartItem.mealId,
            size = emptySize,
            type = CategoryEnum.fromString(cartItem.type)
        )

        assertThat(result).isEqualTo(AddToCartStates.ErrorInLoginState())
    }

    @Test
    fun plus_whenRestaurantsIdsAreDifferent_shouldReturnErrorInCartRestaurant() = runTest {
        val cartItem = FakeCartData.fakeCartItemsDomainClass().copy(quantity = 5, restaurantId = 10)
        val userId = "aaaaa"
        val size = ""

        val cartUseCase = getCartUseCase(
            item = cartItem,
            mealDomain = FakeCartData.mealDomain().copy(restaurantId = 10),
            snackDomain = FakeCartData.snackDomain().copy(restaurantId = 10)
        )
        val result = cartUseCase.plus(
            userId = userId,
            mealId = cartItem.mealId,
            size = size,
            type = CategoryEnum.fromString(cartItem.type)
        )

        assert(result is AddToCartStates.ErrorInCartRestaurant)
    }

    @Test
    fun plus_shouldReturnSuccess() = runTest {
        val cartItem = FakeCartData.fakeCartItemsDomainClass().copy(quantity = 5, restaurantId = 1)
        val userId = "aaaaa"
        val size = ""

        val cartUseCase = getCartUseCase(cartItem)
        val result = cartUseCase.plus(
            userId = userId,
            mealId = cartItem.mealId,
            size = size,
            type = CategoryEnum.fromString(cartItem.type)
        )

        assertThat(result).isEqualTo(AddToCartStates.Success)
    }

    @Test
    fun `plus when cart is empty should call createNewCart`() = runTest {
        val cartItem = FakeCartData.fakeCartItemsDomainClass().copy(quantity = 5)
        val userId = "aaaaa"
        val size = ""

        val cartUseCase = getCartUseCase(
            item = cartItem,
            cartItems = emptyList()
        )
        cartUseCase.plus(
            userId = userId,
            mealId = cartItem.mealId,
            size = size,
            type = CategoryEnum.fromString(cartItem.type)
        )

        coVerify(exactly = 1){ cartRepository.createNewCart(any(), any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `plus when item exists and quantity is under 99 should update quantity`() = runTest {
        val cartItem = FakeCartData.fakeCartItemsDomainClass().copy(quantity = 5, restaurantId = 1)
        val userId = "aaaaa"
        val size = ""

        val cartUseCase = getCartUseCase(item = cartItem)

        cartUseCase.plus(
            userId = userId,
            mealId = cartItem.mealId,
            size = size,
            type = CategoryEnum.fromString(cartItem.type)
        )

        coVerify(exactly = 1) {
            cartRepository.updateQuantity(any(), any(), any(), any(), any())
        }
    }

    @Test
    fun `plus when quantity is 99 should not call updateQuantity`() = runTest {
        val cartItem = FakeCartData.fakeCartItemsDomainClass().copy(quantity = 99, restaurantId = 1)
        val userId = "aaaaa"
        val size = ""

        val cartUseCase = getCartUseCase(cartItem)
        cartUseCase.plus(
            userId = userId,
            mealId = cartItem.mealId,
            size = size,
            type = CategoryEnum.fromString(cartItem.type)
        )

        coVerify(exactly = 0){
            cartRepository.updateQuantity(any(), any(), any(), any(), any())
        }
    }


    // *** ---------------------- \\***  Minus Test  ***// ---------------------- ***
    @Test
    fun `minus when item quantity is 1 should call deleteFromCart`() = runTest {
        val cartItem = FakeCartData.fakeCartItemsDomainClass().copy(quantity = 1, restaurantId = 1)
        val userId = "aaaaa"
        val size = ""

        val cartUseCase = getCartUseCase(cartItem)
        cartUseCase.minus(
            userId = userId,
            mealId = cartItem.mealId,
            size = size
        )

        coVerify(exactly = 1){
            cartRepository.deleteFromCart(any(), any(), any())
        }
    }

    @Test
    fun `minus when item quantity is greater than 1 should call updateQuantity`() = runTest {
        val cartItem = FakeCartData.fakeCartItemsDomainClass().copy(quantity = 5, restaurantId = 1)
        val userId = "aaaaa"
        val size = ""

        val cartUseCase = getCartUseCase(cartItem)
        cartUseCase.minus(
            userId = userId,
            mealId = cartItem.mealId,
            size = size
        )

        coVerify(exactly = 1){
            cartRepository.updateQuantity(any(), any(), any(), any(), any())
        }
    }


    // *** ---------------------- \\***  Helper Setup  ***// ---------------------- ***
    private fun getCartUseCase(
        item : CartItemsDomainClass,
        mealDomain : MealDomain = FakeCartData.mealDomain(),
        snackDomain : SnackDomain = FakeCartData.snackDomain(),
        cartInformation : CartClass = FakeCartData.fakeCartRestaurant(),
        cartItems : List<CartItemsDomainClass> = listOf(item),
        cartRestaurantData : Restaurants = Restaurants(id = item.restaurantId)
    ): CartUseCase {
        every { cartRepository.cartInformation } returns MutableStateFlow(cartInformation)
        every { cartRepository.cartItems } returns MutableStateFlow(cartItems)
        coEvery { cartRepository.getCartRestaurantData(item.restaurantId) } returns (cartRestaurantData)
        coEvery { cartRepository.getMealByIdFromDatabase(item.mealId) } returns (mealDomain)
        coEvery { cartRepository.getSnackByIdFromDatabase(item.mealId) } returns (snackDomain)

        return CartUseCase(cartRepository = cartRepository)
    }
}