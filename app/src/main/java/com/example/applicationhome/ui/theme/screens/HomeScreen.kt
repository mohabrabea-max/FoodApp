package com.example.applicationhome.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.FoodDataSource
import com.example.applicationhome.data.models.VarietiesMenu
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.ui.theme.components.CategoriesBox
import com.example.applicationhome.ui.theme.components.ItemsBox
import com.example.applicationhome.view.model.AddBoxViewModel
import com.example.applicationhome.view.model.ItemScreenViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun HomeScreen(drawerState : DrawerState, coroutineScope : CoroutineScope, navigationController : NavHostController, cartNumber : AddBoxViewModel = viewModel(), viewModel: ItemScreenViewModel){
    val context = LocalContext.current
    val menu = FoodDataSource.allMenu()
    val categories = VarietiesMenu.categoriesList()
    var cart2 = cartNumber.totalCart.value ?: 0
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.LightBrownForBackground
    ){
        LazyVerticalGrid (
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            item(span = { GridItemSpan(2) }){ LazyRow(
                modifier = Modifier.height(150.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                items(categories) { category -> CategoriesBox(category) } }
            }

            items(menu){ item -> ItemsBox(item, drawerState, coroutineScope, navigationController, viewModel) }
        }
        Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.BottomCenter){
            if(cart2 > 0){
                Box(
                    modifier = Modifier.size(150.dp).
                    aspectRatio(3f).
                    shadow(elevation = 3.dp, shape = RoundedCornerShape(30.dp)).
                    background(Color.Blue).
                    clickable{
                        cartNumber.addToCart()
                        Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show()
                    }.
                    padding(5.dp),
                    contentAlignment = Alignment.Center
                ){
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = Color.White, modifier = Modifier.weight(1f))
                        VerticalDivider(modifier = Modifier.align(Alignment.CenterVertically))
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center){
                            Text(text = cart2.toString(), color = Color.White)
                        }

                    }
                }
            }else{
                Box(
                    modifier = Modifier.size(150.dp).
                    aspectRatio(3f).
                    shadow(elevation = 3.dp, shape = RoundedCornerShape(30.dp)).
                    background(Color.LightGray).
                    clickable{}.
                    padding(5.dp),
                    contentAlignment = Alignment.Center
                ){
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = Color.BrownForFont, modifier = Modifier.weight(1f))
                        VerticalDivider(color = Color.BrownForFont, modifier = Modifier.align(Alignment.CenterVertically))
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center){
                            Text(text = "Cart", color = Color.BrownForFont)
                        }
                    }
                }
            }
        }
    }
}