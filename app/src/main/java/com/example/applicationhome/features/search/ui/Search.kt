package com.example.applicationhome.features.search.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NorthWest
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.applicationhome.core.ui.components.designsystem.TopBarButtons
import com.example.applicationhome.core.ui.theme.DeepMatteBlack
import com.example.applicationhome.core.ui.theme.formatSingleWordInSearch
import com.example.applicationhome.data.data.model.Categories
import com.example.applicationhome.data.data.model.Screens
import com.example.applicationhome.features.homescreen.ui.CategoriesBar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Search(
    navigationController : NavHostController,
    searchViewModel : SearchViewModel
){
    val interactionSource = remember { MutableInteractionSource() }

    val lastSearchList = listOf("Bazzoka", "fjjfhttty", "tutuytu", "fjjfhttty", "tutghhhhjuytu", "fjjfhttty", "tutuytu", "fjjfhttty", "tutuytu", "fjjfhttty", "tutuytu", "fjjfhttty", "tutuytu")
    val searchList = listOf(
    "Burger",
    "dsgsfdg",
    "ghfjg",
    "dsgsfhhdg",
    "ghfhgjg",
    "dsgsfvcdg",
    "ghhgjfjjg",
    "dsdfhgsfdg",
    "ghfuijjg",
    "dsvhjcbgsfdg",
    "grhfjjg",
    "gfvbgsfdg",
    "ghfjwrjg",
    "ghjjghjg"
    )
    var search by remember { mutableStateOf("") }


    Scaffold(
        modifier = Modifier.navigationBarsPadding().
        fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            SearchScreenTopBar(
                totalInCart = 5,
                searchText = search,
                backClick = {
                    if (navigationController.previousBackStackEntry != null) {
                    navigationController.popBackStack()
                    }
                },
                cartClick = { navigationController.navigate(Screens.Cart.screen) },
                onSearchTriggered = { search = it },
                clearText = { search = "" }
            )
        }
    ){ paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.Start
        ){
            if(search.isNotEmpty()){
                //       --------------------------\\ Last Search //--------------------------
                if(lastSearchList.isNotEmpty()) items(lastSearchList){ item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {  }
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "Search Icon",
                                tint = Color.DarkGray,
                                modifier = Modifier.size(25.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = item.formatSingleWordInSearch(search),
                                color = Color.DarkGray,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.NorthWest,
                            contentDescription = "Search Icon",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(17.dp)
                        )
                    }

                    Divider(
                        color = Color.LightGray,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    )
                }

                //       --------------------------\\ New Search //--------------------------
                items(searchList){ item ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {  }
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon",
                                tint = Color.Black,
                                modifier = Modifier.size(25.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = item.formatSingleWordInSearch(search),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.NorthWest,
                            contentDescription = "Search Icon",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(17.dp)
                        )
                    }

                    if(item != searchList.last()) Divider(
                        color = Color.LightGray,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    )
                }

            }else{
                //       --------------------------\\ Categories Bar //--------------------------
                item{
                    CategoriesBar(
                        listOf(
                            Categories(0, "Pizza", "Pizza"),
                            Categories(0, "Pizza", "Pizza"),
                            Categories(0, "Pizza", "Pizza"),
                            Categories(0, "Pizza", "Pizza"),
                            Categories(0, "Pizza", "Pizza"),
                            Categories(0, "Pizza", "Pizza")
                        ),
                        false,
                        0,
                        {},
                        {}
                    )
                }

                //       --------------------------\\ Search History //--------------------------
                if(lastSearchList.isNotEmpty()){
                    item{
                        Text(
                            text = "Search History",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
                        )
                    }
                    item{
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ){
                            lastSearchList.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .height(37.dp)
                                        .background(Color.White)
                                        .border(1.dp, Color.LightGray, CircleShape)
                                        .clip(CircleShape)
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) {  }
                                        .padding(horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ){
                                    Icon(
                                        imageVector = Icons.Default.History,
                                        contentDescription = "Search Icon",
                                        tint = Color.DarkGray,
                                        modifier = Modifier.size(20.dp)
                                    )

                                    Spacer(modifier = Modifier.width(3.dp))

                                    Text(
                                        text = item,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}





//@Preview(showBackground = true)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreenTopBar(
    totalInCart : Int = 5,
    searchText : String,
    backClick : () -> Unit = {},
    cartClick : () -> Unit = {},
    onSearchTriggered : (String) -> Unit = {},
    clearText :  () -> Unit = {},
){
    Surface(
        modifier = Modifier.shadow(
            elevation =
                if(searchText.isNotEmpty())5.dp
                else 0.dp
        ),
        color = Color.White
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(65.dp)
                .background(Color.White)
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            TopBarButtons(
                {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.DeepMatteBlack
                    )
                },
                { backClick() }
            )

            BasicTextField(
                value = searchText,
                onValueChange = onSearchTriggered,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .weight(1f)
                    .height(40.dp),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black,
                    platformStyle = PlatformTextStyle(includeFontPadding = false) // حماية إضافية للخط
                ),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFF5F5F5), CircleShape)
                            .border(1.dp, Color.LightGray, CircleShape)
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (searchText.isEmpty()) {
                                Text(
                                    text = "Search for food or restaurant",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                            innerTextField()
                        }

                        if (searchText.isNotEmpty()) {
                            IconButton(
                                onClick = { onSearchTriggered("") },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear Text",
                                    tint = Color.DeepMatteBlack,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            )

            TopBarButtons(
                {
                    BadgedBox(
                        badge = {
                            if(totalInCart > 0){
                                Badge(
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                ){
                                    if(totalInCart < 10){
                                        Text(text = "$totalInCart")
                                    }else{ Text(text = "+9") }
                                }
                            }
                        }
                    ){
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                { cartClick() }
            )
        }
    }
}