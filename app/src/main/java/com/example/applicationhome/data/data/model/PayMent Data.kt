package com.example.applicationhome.data.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Payments
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.applicationhome.BuildConfig
import com.google.gson.annotations.SerializedName

enum class PaymentMethod(
    val title : String,
    val icon : ImageVector,
    val integrationId : Int
){
    CARD(
        title = "Bank card (Visa / Mastercard)",
        icon = Icons.Default.Payment,
        integrationId = BuildConfig.PAYMOB_CARD_INTEGRATION_ID.toInt()
    ),

    WALLET(
        title = "E-wallet(Vodafone Cash)",
        icon = Icons.Default.AccountBalanceWallet,
        integrationId = BuildConfig.PAYMOB_WALLET_INTEGRATION_ID.toInt()
    ),

    CASH(
        title = "Cash on delivery",
        icon = Icons.Default.Payments,
        integrationId = 0
    )

}

data class PaymobAuthRequest(
    @SerializedName("api_key")
    val apiKey: String
)

data class PaymobAuthResponse(
    @SerializedName("token")
    val token: String
)

data class PaymobOrderRequest(
    @SerializedName("auth_token")
    val authToken: String,

    @SerializedName("amount_cents")
    val amountCents: String,

    @SerializedName("currency")
    val currency: String = "EGP",

    @SerializedName("delivery_needed")
    val deliveryNeeded: String = "false",

    @SerializedName("items")
    val items: List<Any> = emptyList() // قائمة المنتجات (يمكن تركها فارغة في الربط البسيط)
)

data class PaymobOrderResponse(
    @SerializedName("id")
    val id: Long // رقم الطلب الصادر من Paymob
)

data class PaymobPaymentKeyResponse(
    @SerializedName("token")
    val token: String // هذا هو Payment Key النهائي الذي يُعرض به الـ Iframe
)

data class PaymobPaymentKeyRequest(
    @SerializedName("auth_token")
    val authToken: String,

    @SerializedName("amount_cents")
    val amountCents: String,

    @SerializedName("expiration")
    val expiration: Int = 3600,

    @SerializedName("order_id")
    val orderId: String,

    @SerializedName("billing_data")
    val billingData: PaymobBillingData,

    @SerializedName("currency")
    val currency: String = "EGP",

    @SerializedName("integration_id")
    val integrationId: Int
)

data class PaymobBillingData(
    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("phone_number")
    val phoneNumber: String,

    @SerializedName("apartment")
    val apartment: String = "5",

    @SerializedName("floor")
    val floor: String = "1",

    @SerializedName("street")
    val street: String = "1",

    @SerializedName("building")
    val building: String = "1",

    @SerializedName("shipping_method")
    val shippingMethod: String = "PKG",

    @SerializedName("postal_code")
    val postalCode: String = "12345",

    @SerializedName("city")
    val city: String = "Cairo",

    @SerializedName("country")
    val country: String = "EGY",

    @SerializedName("state")
    val state: String = "Cairo"
)

sealed interface PaymentApiState{
    data object Idle : PaymentApiState
    data object Loading : PaymentApiState
    data class Success(val paymentToken: String) : PaymentApiState
    data class Error(val message: String) : PaymentApiState
}

sealed interface PaymentState{
    data object Idle : PaymentState
    data object Loading : PaymentState
    data object Success : PaymentState
    data object Failed : PaymentState
}

data class CheckoutUiState(
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.CASH,
    val isProcessing: Boolean = false
)

//data class UserDataInCheckoutScreen(
//
//)