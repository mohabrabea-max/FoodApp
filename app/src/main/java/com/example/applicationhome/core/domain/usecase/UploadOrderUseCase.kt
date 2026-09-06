package com.example.applicationhome.core.domain.usecase

import com.example.applicationhome.core.domain.repository.AddressesRepository
import com.example.applicationhome.core.domain.repository.CartRepository
import com.example.applicationhome.core.domain.repository.OrderRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.data.data.model.OrderItemsClass
import com.example.applicationhome.data.data.model.OrderStatesEnum
import com.example.applicationhome.data.data.model.OrdersClass
import com.example.applicationhome.data.data.model.UserInformationInOrderClass
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UploadOrderUseCase @Inject constructor(
    private val userRepository : UserRepository,
    private val cartRepository : CartRepository,
    private val orderRepository: OrderRepository,
    private val addressesRepository: AddressesRepository,
    private val addAddressUseCase : AddAddressUseCase,
    private val cartUseCase : CartUseCase
){
    operator suspend fun invoke(
        addressId : Long? = null,
        title : String,
        house : String,
        street : String,
        phoneNumber : String,
        additionalDirectionsState : String,
        addressLabelState : String,
        latLocation : String,
        lngLocation : String,
        locationName : String,
        locationFullName : String,
        isSavePhoneNumberSelected : Boolean,
        isSaveAddressSelected : Boolean
    ): Result<Unit>{
        val currentUser = userRepository.userData.value
        val userId = currentUser.id

        val orderInformation = cartRepository.getCartData(userId).first()

        if(orderInformation == null){ return Result.failure(Exception()) }


        val firstname = currentUser.firstname
        val lastname = currentUser.lastname

        val currentCartItems = cartRepository.getCartItems(userId).first()

        val subtotal = currentCartItems.sumOf { it?.totalPrice ?: 0.0 }
        val delivery = 55.0
        val service = 8.0
        val totalPrice = subtotal + delivery + service

        val orderItems = currentCartItems.mapNotNull { item ->
            item?.let {
                OrderItemsClass(
                    mealId = it.mealId,
                    mealName = it.name,
                    size = it.size,
                    price = it.priceOfOne,
                    quantity = it.quantity,
                    image = it.image,
                    type = it.type
                )
            }

        }

        val order = OrdersClass(
            date = "",
            state = OrderStatesEnum.PREPARING.rawValue,
            subtotal = subtotal,
            delivery = delivery,
            service = service,
            totalPrice = totalPrice,
            userInformation = UserInformationInOrderClass(
                name = "$firstname $lastname",
                phonenumber = phoneNumber,
                additionalDirectionsState = additionalDirectionsState,
                addressLabelState = addressLabelState,
                latLocation = latLocation,
                lngLocation = lngLocation,
                locationAddress = locationFullName
            ),
            orderItems = orderItems,
            orderHistory = emptyList(),
            restaurantName = orderInformation.restaurantName,
            restaurantImage = orderInformation.restaurantImage,
            restaurantId = orderInformation.restaurantId,
        )

        if(isSavePhoneNumberSelected){
            userRepository.updatePhoneNumber(userId, phoneNumber)
        }
        if(isSaveAddressSelected){
            addAddressUseCase(
                userId = userId,
                addressId = addressId?: 0L,
                title = title,
                house = house,
                street = street,
                phoneNumber = phoneNumber,
                additionalDirectionsState = additionalDirectionsState,
                addressLabelState = addressLabelState,
                latLocation = latLocation,
                lngLocation = lngLocation,
                locationName = locationName,
                locationFullName = locationFullName
            )
        }

        val result = orderRepository.uploadOrderRequest(order, userId)

        return if(result.isSuccess){
            cartUseCase.clearAllCart(userId)

            if(addressId != null){
                addressesRepository.updateAddressesLastUse(
                    userId = userId,
                    addressId = addressId
                )
            }

            Result.success(Unit)
        }else{
            Result.failure(result.exceptionOrNull() ?: Exception("Failed to upload order"))
        }
    }
}