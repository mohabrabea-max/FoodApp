package com.example.applicationhome.features.Notifications.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.bars.MyTopBar
import com.example.applicationhome.core.ui.components.screens.EmptyScreen
import com.example.applicationhome.core.ui.model.NotificationUIClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notifications(
    notifications : List<NotificationUIClass>,
    onDelete : (Int) -> Unit,
    popBack : () -> Unit
){
    Scaffold(
        topBar = {
            MyTopBar(
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                title = stringResource(R.string.notifications),
                titleColor = MaterialTheme.colorScheme.onSurface,
                startaction = {
                    IconButton(
                        onClick = { popBack() },
                        modifier = Modifier
                            .size(50.dp)
                            .padding(5.dp)
                            .clip(CircleShape)
                    ){
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ){ paddingValues ->
        if(notifications.isEmpty()){
            EmptyScreen(
                title = stringResource(R.string.no_previous_notifications),
                image = painterResource(R.drawable.emptyscreenicon),
                spacerheight = 80.dp
            )
        }else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                items(notifications) { notification ->
                    NotificationCard(notification = notification){ id ->
                        onDelete(id)
                    }
                }
            }
        }
    }
}