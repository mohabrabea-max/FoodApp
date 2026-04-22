package com.example.applicationhome.ui.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.applicationhome.ui.theme.BackgroundForCards
import com.example.applicationhome.ui.theme.BrownForFont

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MyTopBar(scrollBehavior: TopAppBarScrollBehavior, navController: NavController, drawerState : DrawerState){
//    val coroutineScope = rememberCoroutineScope()
//    CenterAlignedTopAppBar(modifier = Modifier.fillMaxWidth(),
//        title = {Text(text = "Home")},
//        windowInsets = WindowInsets(top = 20.dp),
//        scrollBehavior = scrollBehavior,
//        navigationIcon = {
//            IconButton(onClick = { coroutineScope.launch{drawerState.open()} }){
//                Icon(
//                    imageVector = Icons.Default.Menu,
//                    contentDescription = "MenuButton",
//                    tint = Color.BrownForFont
//                )
//            }
//        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.BackgroundForCards,scrolledContainerColor = Color.BackgroundForCards, titleContentColor = Color.BrownForFont, navigationIconContentColor = Color.BrownForFont),
//        actions = {
//            IconButton(onClick = {navController.navigate(Screens.HomeScreen.screen){popUpTo(0)}}){
//                Icon(imageVector = Icons.Default.Home, contentDescription = "Menu", tint = Color.BrownForFont)
//            }
//        }
//    )
//}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(scrollBehavior: TopAppBarScrollBehavior, onMenuClick: () -> Unit, onFirstClick: () -> Unit, firstIcon : ImageVector, onSecondeClick: () -> Unit, secondeIcon : ImageVector){
    val maxHeight = 100.dp
    val density = LocalDensity.current
    val currentHeight by remember {
        derivedStateOf {
            (maxHeight + with(density) { scrollBehavior.state.heightOffset.toDp() })
                .coerceAtLeast(0.dp)
        }
    }
    Surface(
        modifier = Modifier.
        fillMaxWidth().
        height(currentHeight).
        statusBarsPadding(),
        color = Color.BackgroundForCards
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically, // العناصر كلها هتتوسطن أوتوماتيكياً!
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = null, tint = Color.BrownForFont)
            }

            // العنوان (دلوقتي هو في النص بالملي)
            Text(
                text = "Home",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.BrownForFont
            )
            Row{
                IconButton(onClick = onFirstClick) {
                    Icon(firstIcon, contentDescription = null, tint = Color.BrownForFont)
                }
                IconButton(onClick = onSecondeClick) {
                    Icon(secondeIcon, contentDescription = null, tint = Color.BrownForFont)
                }
            }
        }
    }
}