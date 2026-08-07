package com.example.applicationhome.features.confirmorder.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.data.data.model.ProfileEditResult
import com.example.applicationhome.data.data.model.TextFieldClassFromConfirmOrderScreen

@Composable
fun PageOneConfirmOrder(
    textFieldConfirmOrderScreenList : List<TextFieldClassFromConfirmOrderScreen>,
    isButtonClicked : Boolean,
    confirmOrderError : ProfileEditResult?,
    location : String,
    bottonStateChange : () -> Unit,
    openMaps : () -> Unit
){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ){
        item{Spacer(modifier = Modifier.height(100.dp))}

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
                        tint = Color.Gray,
                        modifier = Modifier.padding(end = 7.dp)
                    )

                    Column(
                        modifier = Modifier.padding(horizontal = 7.dp).fillMaxHeight(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween
                    ){

                        Text(
                            text = "Area",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
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
                item,
                isButtonClicked,
                confirmOrderError ?: ProfileEditResult.NetworkError,
            ){ bottonStateChange() }
        }

        item{Spacer(modifier = Modifier.height(100.dp))}
    }
}