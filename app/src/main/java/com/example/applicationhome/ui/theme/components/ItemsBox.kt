package com.example.applicationhome.ui.theme.components

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.applicationhome.data.models.FoodItem
import com.example.applicationhome.data.models.Screens
import com.example.applicationhome.ui.theme.BackgroundForCards
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.LightBackgroundForCards
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.ui.theme.MediumBrownForTitle
import com.example.applicationhome.view.model.ItemScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ItemsBox(item: FoodItem, drawerState : DrawerState, coroutineScope : CoroutineScope, navigationController : NavHostController, viewModel: ItemScreenViewModel){
    val context = LocalContext.current
    Box(
        modifier = Modifier.
        animateContentSize().
        aspectRatio(0.8f).
        clickable{
            coroutineScope.launch{drawerState.close()}
            viewModel.selectedItem = item
            navigationController.navigate(Screens.ItemScreen.screen){popUpTo(0)}
        }.
        padding(5.dp).
        shadow(elevation = 3.dp, shape = RoundedCornerShape(10.dp))
    ){
        Column(
            modifier = Modifier.
            background(Color.LightBackgroundForCards)
        ) {
            Image(
                painter = painterResource(id = item.image),
                contentDescription = "Food Logo",
                modifier = Modifier.
                fillMaxSize().
                weight(3f),
                contentScale = ContentScale.Crop
            )
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleLarge,
                color = Color.MediumBrownForTitle,
                fontSize = 18.sp,
                modifier = Modifier.
                weight(1f).
                padding(10.dp)
            )
            Box(
                modifier = Modifier.
                fillMaxWidth().
                background(Color.BackgroundForCards),
                contentAlignment = Alignment.Center
            ){
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Information",
                        tint = Color.BrownForFont,
                        modifier = Modifier.
                        clip(CircleShape).
                        alpha(0.8f).
                        clickable {Toast.makeText(context, "Price : ${item.price}", Toast.LENGTH_SHORT).show()}
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${item.price} L.E",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.BrownForFont,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ){
            Favorite(modifier = Modifier.padding(10.dp).clip(CircleShape).size(35.dp).background(Color.LightBrownForBackground.copy(alpha = 0.8f)),id = item.id)
            Spacer(modifier = Modifier.height(33.dp))
            AddBox(item.id)
        }
    }
}