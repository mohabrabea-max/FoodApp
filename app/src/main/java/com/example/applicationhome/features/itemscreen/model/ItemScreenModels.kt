package com.example.applicationhome.features.itemscreen.model

import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.example.applicationhome.R


@Keep
sealed interface StartBottomSheets {
    data object None : StartBottomSheets

    data class LoginBottomSheet(
        @StringRes val title : Int = R.string.hey_there,
        @StringRes val message : Int = R.string.log_in_or_sign_up_for_a_more_personalized_ordering_experience
    ) : StartBottomSheets

    data class OrdersBottomSheet(
        @StringRes val title : Int = R.string.continue_your_order,
        @StringRes val message : Int = R.string.you_still_have_items_in_your_cart_would_you_like_to_review_them_and_finish_placing_your_order
    ) : StartBottomSheets
}