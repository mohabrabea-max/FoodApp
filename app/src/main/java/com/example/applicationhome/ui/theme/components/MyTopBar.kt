package com.example.applicationhome.ui.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.applicationhome.ui.theme.DarkOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    title : String,
    onMenuClick: () -> Unit,
    startIcon : @Composable () -> Unit = {},
    actions : @Composable RowScope.() -> Unit = {}
){
    Surface(
        modifier = Modifier.
        fillMaxWidth().
        height(100.dp).
        shadow(elevation = 5.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onMenuClick) {
                startIcon()
            }

            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.DarkOrange
            )

            Row(verticalAlignment = Alignment.CenterVertically, content = actions)
        }
    }
}