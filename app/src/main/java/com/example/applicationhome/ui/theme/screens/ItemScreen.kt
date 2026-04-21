package com.example.applicationhome.ui.theme.screens

import android.R.attr.name
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.ui.theme.BackgroundForCards
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.LightBrownForBackground
import com.example.applicationhome.ui.theme.MediumBrownForTitle
import com.example.applicationhome.ui.theme.components.AddBox
import com.example.applicationhome.ui.theme.components.CartButton
import com.example.applicationhome.view.model.ItemScreenViewModel

@Composable
fun ItemScreen(viewModel: ItemScreenViewModel){
    val item = viewModel.selectedItem
    var size = 3
    if(item != null){
        Box(
            modifier = Modifier.fillMaxSize().
            background(Color.LightBrownForBackground)
        ){
            LazyColumn(modifier = Modifier.fillMaxSize()){
                item {
                    Column(modifier = Modifier.fillMaxSize().padding(10.dp)){
                        Box(modifier = Modifier.fillMaxWidth().height(300.dp)){
                            Image(
                                painter = painterResource(id = item.image),
                                contentDescription = "$name Logo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.
                                fillMaxSize()
                            )
                        }
                        Divider()
                        Spacer(modifier = Modifier.height(20.dp))
                        Column{
                            Text(
                                text = item.name,
                                fontSize = 20.sp,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.BrownForFont,
                                modifier = Modifier.padding(10.dp)
                            )
                            Text(
                                text = item.description,
                                color = Color.MediumBrownForTitle,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "${item.price} L.E",
                            fontSize = 30.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.BrownForFont,
                            modifier = Modifier.padding(10.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(20.dp))
                        Box(
                            modifier = Modifier.
                            animateContentSize().
                            padding(10.dp).
                            height(50.dp).
                            width(180.dp).
                            clip(CircleShape).
                            background(Color.BackgroundForCards.copy(alpha = 0.8f)),
                            contentAlignment = Alignment.Center
                        ){
                            if(size == 3){
                                Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                                    Box(modifier = Modifier.weight(1f).fillMaxHeight().clickable {}, contentAlignment = Alignment.Center){
                                        Text(text = "Beg", color = Color.BrownForFont)
                                    }
                                    VerticalDivider(color = Color.MediumBrownForTitle, modifier = Modifier.padding(5.dp))
                                    Box(modifier = Modifier.weight(1f).fillMaxHeight().clickable {}, contentAlignment = Alignment.Center){
                                        Text(text = "Mediam", color = Color.BrownForFont)
                                    }
                                    VerticalDivider(color = Color.MediumBrownForTitle, modifier = Modifier.padding(5.dp))
                                    Box(modifier = Modifier.weight(1f).fillMaxHeight().clickable {}, contentAlignment = Alignment.Center){
                                        Text(text = "Small", color = Color.BrownForFont)
                                    }
                                }
                            }else if(size == 2){
                                Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                                    Box(modifier = Modifier.weight(1f).fillMaxHeight().clickable {}, contentAlignment = Alignment.Center){
                                        Text(text = "Beg", color = Color.BrownForFont)
                                    }
                                    VerticalDivider(color = Color.MediumBrownForTitle, modifier = Modifier.padding(5.dp))
                                    Box(modifier = Modifier.weight(1f).fillMaxHeight().clickable {}, contentAlignment = Alignment.Center){
                                        Text(text = "Mediam", color = Color.BrownForFont)
                                    }
                                }
                            }else{
                                Box(modifier = Modifier.fillMaxSize().clickable {}, contentAlignment = Alignment.Center){
                                    Text(text = item.size, color = Color.BrownForFont)
                                }
                            }

                        }


                    }
                }

            }
            Row(modifier = Modifier.fillMaxWidth().height(60.dp).align(Alignment.BottomCenter).background(Color.BrownForFont.copy(alpha = 0.1f)), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                AddBox(modifier = Modifier.padding(5.dp).height(48.dp).clip(CircleShape).shadow(elevation = 3.dp, shape = RoundedCornerShape(30.dp)).background(Color.LightGray.copy(alpha = 1f)), color = Color.LightGray, id = item.id)
                //AddBox2(modifier = Modifier.padding(5.dp).height(48.dp).clip(CircleShape), id = item.id)
                CartButton(modifier = Modifier.padding(5.dp))
            }
        }
    }
}