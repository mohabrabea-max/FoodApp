package com.example.applicationhome.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.R
import com.example.applicationhome.data.models.Account
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.MediumBrownForTitle

@Composable
fun Profile(){
    val profile = listOf(
        Account("Name","Mohab Rabea",Icons.Default.Person),
        Account("Phone Number","01011223344",Icons.Default.Phone),
        Account("Country","Egypt",Icons.Default.LocationOn)
    )
        LazyColumn(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.spacedBy(16.dp)){
            item{
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ){
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(modifier = Modifier.size(150.dp).clip(CircleShape),contentAlignment = Alignment.Center){
                        Image(
                            painter = painterResource(id = R.drawable.myphoto),
                            contentDescription = "Food Logo",
                            modifier = Modifier.
                            fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "Mohab Rabea",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Divider()
                }
            }
            items(profile){item ->
                Column(modifier = Modifier.fillMaxSize()){
                    NavigationDrawerItem(
                        label = {
                            Column {
                                Text(text = item.title, fontSize = 20.sp, color = Color.BrownForFont)
                                Text(text = item.value, fontSize = 13.sp, color = Color.MediumBrownForTitle)
                            }
                        },
                        selected = false,
                        icon = {Icon(modifier = Modifier.size(30.dp), imageVector = item.icon, contentDescription = item.title, tint = Color.BrownForFont)},
                        onClick = {}
                    )
                }
                Divider()
            }
            item{
                Box(modifier = Modifier.fillMaxSize().padding(50.dp), contentAlignment = Alignment.Center){
                    IconButton(onClick = {}){
                        Icon(modifier = Modifier.size(50.dp), imageVector = Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.BrownForFont)
                    }
                }
            }
        }

//    Column(
//        modifier = Modifier.background(Color.White).fillMaxSize().padding(10.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ){
//
//
//
//
//        Spacer(modifier = Modifier.height(50.dp))
//
//        Box(modifier = Modifier.fillMaxSize()){
//            Column(
//                modifier = Modifier.fillMaxSize().align(Alignment.Center).padding(10.dp),
//                verticalArrangement = Arrangement.Top,
//                horizontalAlignment = Alignment.Start
//            ){
//                Text(text = "Email   : ", fontSize = 30.sp, color = Color.Black)
//                Text(text = "Age     : ", fontSize = 30.sp, color = Color.Black)
//                Text(text = "Country :", fontSize = 30.sp, color = Color.Black)
//            }
//        }
//    }
}