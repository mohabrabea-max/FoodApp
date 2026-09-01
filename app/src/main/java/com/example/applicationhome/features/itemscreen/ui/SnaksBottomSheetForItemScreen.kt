package com.example.applicationhome.features.itemscreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.designsystem.TopBarButtons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnaksBottomSheetForItemScreen(
    title : String,
    description : String? = null,
    image : String,
    onDismissRequest : () -> Unit
){
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle =  null
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .padding(bottom = 15.dp)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ){
                Box(modifier = Modifier.fillMaxWidth().height(220.dp)){
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(image)
                            .crossfade(true)
                            .precision(Precision.EXACT)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                if(description != null){
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = description,
                        fontSize = 15.sp,
                        lineHeight = 25.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .background(
                        color = Color(0xFFE0E0E0),
                        shape = RoundedCornerShape(2.dp)
                    )
                    .align(Alignment.TopCenter)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                TopBarButtons(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.close),
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    onClick = { onDismissRequest() },
                    elevation = 0.dp,
                    border = 1.dp
                )
            }
        }
    }
}