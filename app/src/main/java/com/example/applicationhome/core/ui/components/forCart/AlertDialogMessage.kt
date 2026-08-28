package com.example.applicationhome.core.ui.components.forCart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.core.ui.theme.VeryLightGray

@Composable
fun AlertDialogMessage(
    title : String,
    content : String,
    confirmButtonText : String,
    confirmButton : () -> Unit,
    dismissButtonText : String? = null,
    dismissButton : (() -> Unit)? = null,
){
    Dialog(
        onDismissRequest = { if(dismissButton != null) dismissButton() else confirmButton() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false // عشان يفرش في الجناب
        )
    ){
        Card(
            modifier = Modifier.
            fillMaxWidth().
            shadow(elevation = 10.dp, spotColor = Color.VeryLightGray.copy(0.5f), shape = RoundedCornerShape(25.dp)).
            padding(16.dp),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ){
            Column(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 25.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(text = content)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    if(dismissButtonText != null && dismissButton != null){
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            onClick = { dismissButton() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = CircleShape,
                            border = BorderStroke(width = 0.5.dp, color = Color.LightGray)
                        ){
                            Text(text = dismissButtonText, color = Color.Black)
                        }

                        Spacer(modifier = Modifier.width(10.dp))
                    }

                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        onClick = { confirmButton() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkOrange),
                        shape = CircleShape,
                        border = BorderStroke(width = 0.5.dp, color = Color.LightGray)
                    ){
                        Text(text = confirmButtonText, color = Color.White)
                    }
                }
            }
        }
    }
}