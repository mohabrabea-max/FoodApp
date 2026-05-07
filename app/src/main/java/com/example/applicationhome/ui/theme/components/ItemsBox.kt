package com.example.applicationhome.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.FoodItem
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.view.model.ItemScreenViewModel


@Preview
@Composable
fun ItemsBox(
    item: FoodItem,
    navigationController : NavHostController,
    viewModel: ItemScreenViewModel = viewModel(),
    size : String?,
    actions : @Composable ColumnScope.() -> Unit = {}
){

    Card(
        modifier = Modifier.padding(7.dp).shadow(elevation = 7.dp, spotColor = Color.LightGray, shape = RoundedCornerShape(30.dp)).
        background(Color.White).
        aspectRatio(0.65f).
        clickable{
            viewModel.run { selectItem(item, item.priceANDsize.keys.last()) }
            navigationController.navigate(Screens.ItemScreen.screen)
        }.
        padding(start = 20.dp, end = 15.dp, top = 15.dp, bottom = 20.dp)
    ){
        Column(modifier = Modifier.fillMaxSize().background(Color.White)){
            Box(modifier = Modifier.fillMaxWidth().weight(2f)){
                Image(
                    modifier = Modifier.fillMaxSize().padding(top = 5.dp, end = 5.dp),
                    painter = painterResource(id = item.image[0]),
                    contentDescription = null
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.SpaceBetween,
                    content = actions
                )
            }

            Column(modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.SpaceBetween){
                Text(
                    text = item.name,
                    fontSize = 14.sp,
                    color = Color.Black,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.name,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = item.priceANDsize.values.last().toString() + " E.G",
                    fontSize = 16.sp,
                    color = Color.Black,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}