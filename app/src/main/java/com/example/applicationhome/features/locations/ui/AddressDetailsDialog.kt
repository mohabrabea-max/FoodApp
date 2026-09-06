package com.example.applicationhome.features.locations.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.components.bars.MyTopBar
import com.example.applicationhome.core.ui.components.designsystem.MyButton
import com.example.applicationhome.core.ui.components.designsystem.TopBarButtons
import com.example.applicationhome.core.ui.theme.DarkOrange
import com.example.applicationhome.data.data.model.ProfileEditResult
import com.example.applicationhome.data.data.model.TextFieldClassFromConfirmOrderScreen
import com.example.applicationhome.data.local.entity.AddressesEntity
import com.example.applicationhome.features.confirmorder.ui.pageone.ConfirmOrderScreenTextField

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddressDetailsDialog(
    title : String,
    address : AddressesEntity,
    textFieldConfirmOrderScreenList : List<TextFieldClassFromConfirmOrderScreen>,
    buttonTitle : String,
    onEditeAddress : ((AddressesEntity) -> Unit)? = null,
    dismissButton : () -> Unit
){
    Dialog(
        onDismissRequest = {  },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ){
        Card(
            modifier = Modifier
                .fillMaxSize(0.9f)
                .shadow(elevation = 10.dp, spotColor = Color.LightGray.copy(0.5f), shape = RoundedCornerShape(25.dp)),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ){
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),

                containerColor = MaterialTheme.colorScheme.surface,

                topBar = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        MyTopBar(
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth().height(70.dp),
                            title = title,
                            titleColor = MaterialTheme.colorScheme.onSurface,
                            startaction = {
                                TopBarButtons(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = stringResource(R.string.close),
                                            tint = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    },
                                    onClick = { dismissButton() },
                                    elevation = 0.dp,
                                    border = 1.dp
                                )
                            }
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.onTertiary)
                    }
                },

                bottomBar = {
                    if(onEditeAddress != null) Column(
                        modifier = Modifier
                            .padding(bottom = 15.dp)
                            .pointerInput(Unit) { detectTapGestures { } },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        MyButton(
                            loading = false,
                            backgroundcolor = Color.DarkOrange,
                            fontcolor = Color.White,
                            horizontalPadding = 40.dp,
                            title = buttonTitle
                        ){
                            onEditeAddress(address)
                        }
                    }
                }
            ){
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 15.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ){
                    item{Spacer(modifier = Modifier.height(90.dp))}

                    item{
                        Row(
                            modifier = Modifier.padding(start = 15.dp, end = 15.dp)
                                .height(80.dp)
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(15.dp))
                                .border(width = 1.dp, color = Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(15.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(15.dp),

                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ){
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(end = 7.dp)
                            )

                            Column(
                                modifier = Modifier.padding(horizontal = 7.dp).fillMaxHeight(),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.SpaceBetween
                            ){

                                Text(
                                    text = stringResource(R.string.area),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = address.locationName,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    item{Spacer(modifier = Modifier.height(15.dp))}

                    items(textFieldConfirmOrderScreenList) { item ->
                        if(item.textField.text.isNotEmpty()) ConfirmOrderScreenTextField(
                            isEditEnabled = false,
                            item = item,
                            isLastTextField = false,
                            isButtonClicked = false,
                            errorOutput = ProfileEditResult.Success,
                            isSavePhoneNumberSelected = false,
                            isSaveAddressSelected = false,
                            bottonStateChange = {  },
                            onSaveAddressRadioButton = {  }
                        )
                    }

                    item{Spacer(modifier = Modifier.height(50.dp))}
                }
            }
        }
    }
}