package com.example.applicationhome.features.confirmorder.ui.pageone

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.designsystem.MyButton
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.VeryLightGray
import com.example.applicationhome.data.data.model.ActionsStates
import com.example.applicationhome.data.data.model.ProfileEditResult
import com.example.applicationhome.data.data.model.TextFieldClassFromConfirmOrderScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PageOneConfirmOrder(
    textFieldConfirmOrderScreenList : List<TextFieldClassFromConfirmOrderScreen>,
    isButtonClicked : Boolean,
    confirmOrderError : ProfileEditResult?,
    confirmOrderState : ActionsStates,
    bottonState: Boolean,
    location : String,
    locationImage : String,
    isSavePhoneNumberSelected : Boolean,
    bottonStateChange : () -> Unit,
    openMaps : () -> Unit,
    onBottonStateChange : () -> Unit,
    onSavePhoneNumber : () -> Unit
){
    val color = if(bottonState) Color.DarkOrange else Color.VeryLightGray
    val fontcolor = if(bottonState) Color.White else Color.LightGray


    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize(),

        containerColor = Color.White,

        bottomBar = {
            Column(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .pointerInput(Unit) { detectTapGestures { } },
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                MyButton(
                    confirmOrderState == ActionsStates.Loading,
                    color,
                    fontcolor,
                    40.dp,
                    stringResource(R.string.save_address)
                ) {
                    if(bottonState) onBottonStateChange()
                }
            }
        }
    ){
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ){
            item{Spacer(modifier = Modifier.height(130.dp))}

            item{
                Box(
                    modifier = Modifier.padding(10.dp)
                        .height(130.dp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(15.dp))
                        .border(width = 1.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(15.dp))

                ){
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = ImageRequest.Builder(LocalContext.current).
                        data(locationImage).
                        crossfade(true).
                        precision(Precision.EXACT).
                        build(),
                        contentDescription = locationImage,
                        contentScale = ContentScale.Crop
                    )
                }
            }

            item{
                Row(
                    modifier = Modifier.padding(start = 15.dp, end = 15.dp)
                        .height(70.dp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(15.dp))
                        .border(width = 1.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(15.dp))
                        .background(Color.White)
                        .clickable { openMaps() }
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Row(
                        modifier = Modifier.weight(5f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ){
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.padding(end = 7.dp)
                        )

                        Column(
                            modifier = Modifier.padding(horizontal = 7.dp).fillMaxHeight(),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.SpaceBetween
                        ){

                            Text(
                                text = "Area",
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = location,
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterEnd
                    ){
                        Text(
                            text = "Change",
                            color = Color.DarkOrange,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            item{Spacer(modifier = Modifier.height(15.dp))}

            items(textFieldConfirmOrderScreenList) { item ->
                ConfirmOrderScreenTextField(
                    item = item,
                    isButtonClicked = isButtonClicked,
                    errorOutput = confirmOrderError ?: ProfileEditResult.NetworkError,
                    isSavePhoneNumberSelected = isSavePhoneNumberSelected,
                    bottonStateChange = { bottonStateChange() },
                    onSavePhoneNumber = { onSavePhoneNumber() }
                )
            }

            item{Spacer(modifier = Modifier.height(100.dp))}
        }
    }

}