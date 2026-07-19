package com.example.applicationhome.features.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectBottomSheet(
    searchBoxTitle : String,
    stringList : List<String>,
    unShowBottomSheet : () -> Unit,
    select : (String) -> Unit,
    unselect : () -> Unit,
    filter : (String) -> Unit
){
    var searchTextField by rememberSaveable { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    ModalBottomSheet(
        onDismissRequest = {
            unShowBottomSheet()
            filter("")
        },
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
        sheetState = sheetState,
        containerColor = Color.White
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    coroutineScope {
                        detectDragGestures { change, dragAmount ->
                            change.consume() // ابلع الحركة وماتمررهاش للشيت
                        }
                    }
                }
                .padding(top = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().weight(8f).navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row(modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(), horizontalArrangement = Arrangement.Start){
                    Box(
                        modifier = Modifier.padding(5.dp).
                        size(40.dp).
                        clip(shape = RoundedCornerShape(30.dp)).
                        background(Color.White).
                        border(width = 1.dp, color = Color.Gray.copy(alpha = 0.4f), shape = RoundedCornerShape(30.dp)).
                        clickable {
                            unShowBottomSheet()
                            filter("")
                        },
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }

                TextField(
                    value = searchTextField,

                    onValueChange = {
                        newText ->
                        searchTextField = newText
                        filter(newText)
                    },

                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 15.dp)
                        .fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = searchBoxTitle,
                            color = Color.LightGray,
                            fontSize = 15.sp
                        )
                    },

                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search Icon", tint = Color.Gray)
                    },

                    trailingIcon = {
                        if (searchTextField.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    searchTextField = ""
                                    filter("")
                                }
                            ){
                                Icon(Icons.Default.Clear, contentDescription = "Clear Text", tint = Color.Gray)
                            }
                        }
                    },

                    singleLine = true,

                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),

                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    ),

                    shape = RoundedCornerShape(30.dp),

                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,

                        focusedContainerColor = Color(0xFFF3F3F3),
                        unfocusedContainerColor = Color(0xFFF3F3F3)
                    )
                )

                LazyColumn(
                    horizontalAlignment = Alignment.Start
                ){
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .clickable { unselect() }
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(Icons.Default.Remove, contentDescription = "Remove", tint = Color.Gray)

                            Spacer(modifier = Modifier.width(5.dp))

                            Text("Nothing", color = Color.Gray)
                        }

                        Divider(
                            color = Color.LightGray,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }

                    items(stringList){ item ->

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .clickable { select(item) }
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.CenterStart
                        ){
                            Text(item)
                        }

                        if(item != stringList.last()) Divider(
                            color = Color.LightGray,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}