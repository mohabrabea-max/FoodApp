package com.example.applicationhome.features.confirmorder.ui.pageone

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.applicationhome.R
import com.example.applicationhome.data.data.model.CheckoutFormState
import com.example.applicationhome.data.data.model.ConfirmOrderScreenTextFieldEnum
import com.example.applicationhome.data.data.model.TextFieldClassFromConfirmOrderScreen

@Composable
fun rememberConfirmOrderAddressFields(formState: CheckoutFormState): List<TextFieldClassFromConfirmOrderScreen> {
    return remember(formState){
        listOf(
            TextFieldClassFromConfirmOrderScreen(
                formState.houseState,
                R.string.house,
                ConfirmOrderScreenTextFieldEnum.HOUSE
            ),
            TextFieldClassFromConfirmOrderScreen(
                formState.streetState,
                R.string.street,
                ConfirmOrderScreenTextFieldEnum.STREET
            ),
            TextFieldClassFromConfirmOrderScreen(
                formState.phoneNumberState,
                R.string.phone_number,
                ConfirmOrderScreenTextFieldEnum.PHONE
            ),
            TextFieldClassFromConfirmOrderScreen(
                formState.additionalDirectionsState,
                R.string.additional_directions_optional,
                ConfirmOrderScreenTextFieldEnum.ADDITIONAL
            ),
            TextFieldClassFromConfirmOrderScreen(
                formState.addressLabelState,
                R.string.address_label_optional,
                ConfirmOrderScreenTextFieldEnum.ADDRESS
            )
        )
    }
}

@Composable
fun rememberTitleTextField(formState: CheckoutFormState): TextFieldClassFromConfirmOrderScreen{
    return remember(formState){
        TextFieldClassFromConfirmOrderScreen(
            formState.addressTitle,
            R.string.title,
            ConfirmOrderScreenTextFieldEnum.TITLE
        )
    }
}