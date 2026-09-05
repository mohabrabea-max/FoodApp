package com.example.applicationhome.features.locations.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.designsystem.MyButton
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.data.local.entity.AddressesEntity

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SelectAddress(
    addresses: List<AddressesEntity>,
    paddingValues: PaddingValues,
    onNewAddressClickable : () -> Unit,
    onAddressClickable : (AddressesEntity) -> Unit,
    onDeleteAddress : ((userId: String, addressId: Long) -> Unit)? = null
){
    Scaffold(
        modifier = Modifier
            .padding(paddingValues)
            .navigationBarsPadding()
            .fillMaxSize(),

        bottomBar = {
            Column(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .pointerInput(Unit) { detectTapGestures { } },
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                MyButton(
                    loading = false,
                    backgroundcolor = Color.DarkOrange,
                    fontcolor = Color.White,
                    horizontalPadding = 40.dp,
                    title = stringResource(R.string.new_address)
                ){
                    onNewAddressClickable()
                }
            }
        }
    ){
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            item{ Spacer(modifier = Modifier.height(15.dp)) }

            items(addresses) { item ->
                AddressBox(
                    address = item,
                    onOpenAddressBox = { onAddressClickable(item) },
                    onDeleteAddress = onDeleteAddress?.let { callback ->
                        { callback(item.userId, item.addressId) }
                    }
                )
            }

            item{Spacer(modifier = Modifier.height(100.dp))}
        }
    }
}