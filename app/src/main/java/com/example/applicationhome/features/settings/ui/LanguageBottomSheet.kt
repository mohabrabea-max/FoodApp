package com.example.applicationhome.features.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.designsystem.TopBarButtons
import com.example.applicationhome.core.ui.theme.DeepMatteBlack
import com.example.applicationhome.data.data.model.AppLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageBottomSheet(
    currentLanguageCode : String,
    onLanguageSelected : (AppLanguage) -> Unit,
    onDismissRequest : () -> Unit
){
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .background(
                        color = Color(0xFFE0E0E0),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                TopBarButtons(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.close),
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    onClick = { onDismissRequest() },
                    elevation = 0.dp,
                    border = 1.dp
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = stringResource(R.string.language),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(13.dp))

            AppLanguage.entries.forEach { language ->
                val isSelected = currentLanguageCode == language.code

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            onLanguageSelected(language)
                            onDismissRequest()
                        }
                        .padding(vertical = 10.dp),

                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .height(20.dp)
                            .width(7.dp)
                            .clip(shape = RoundedCornerShape(50))
                            .background(Color.DeepMatteBlack.copy(alpha = if(isSelected) 1f else 0f))
                    )

                    Text(
                        text = language.titleRes,
                        fontSize = 18.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 15.dp)
                    )
                }
            }
        }
    }
}