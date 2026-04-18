package com.example.applicationhome.ui.theme.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.ui.theme.BackgroundForCards
import com.example.applicationhome.ui.theme.BrownForFont
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(scrollBehavior: TopAppBarScrollBehavior, navController: NavController, drawerState : DrawerState){
    val coroutineScope = rememberCoroutineScope()
    TopAppBar(modifier = Modifier.fillMaxWidth(),
        title = {Text(text = "Home")},
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = { coroutineScope.launch{drawerState.open()} }){
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "MenuButton",
                    tint = Color.BrownForFont
                )
            }
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.BackgroundForCards,scrolledContainerColor = Color.BackgroundForCards, titleContentColor = Color.BrownForFont, navigationIconContentColor = Color.BrownForFont),
        actions = {
            IconButton(onClick = {navController.navigate(Screens.HomeScreen.screen){popUpTo(0)}}){
                Icon(imageVector = Icons.Default.Home, contentDescription = "Menu", tint = Color.BrownForFont)
            }
        }
    )
}
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MyCustomTopBar(scrollBehavior: TopAppBarScrollBehavior, onMenuClick: () -> Unit){
//    val heightOffsetLimit = scrollBehavior.state.heightOffset
//    Surface(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(90.dp) // الارتفاع اللي أنت عايزه
//            .offset { IntOffset(x = 0, y = heightOffsetLimit.toInt()) }, // ده اللي بيخليه يختفي
//        color = Color.BackgroundForCards,
//        tonalElevation = 4.dp,
//        shadowElevation = 4.dp
//    ) {
//        // 2. رص العناصر جوه الـ 90dp
//        Row(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 16.dp),
//            verticalAlignment = Alignment.CenterVertically, // العناصر كلها هتتوسطن أوتوماتيكياً!
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            // أيقونة المنيو
//            IconButton(onClick = onMenuClick) {
//                Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White)
//            }
//
//            // العنوان (دلوقتي هو في النص بالملي)
//            Text(
//                text = "Home",
//                style = MaterialTheme.typography.headlineMedium,
//                color = Color.White
//            )
//
//            // أيقونة إضافية عشان التوازن (أو سيبها فاضية)
//            IconButton(onClick = { /* Search */ }) {
//                Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
//            }
//        }
//    }
//}