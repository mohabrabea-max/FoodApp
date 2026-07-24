package com.example.applicationhome.features.search.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.core.ui.components.designsystem.TopBarButtons
import com.example.applicationhome.core.ui.theme.DeepMatteBlack
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

//@Preview(showBackground = true)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreenTopBar(
    totalInCart : Int,
    searchText : TextFieldValue,
    backClick : () -> Unit,
    cartClick : () -> Unit,
    onQueryChange : (TextFieldValue) -> Unit,
    clickSearch : (String) -> Unit,
    unClickSearch : () -> Unit,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?
){
    var localSearchString by remember(searchText) { mutableStateOf(searchText) }

    LaunchedEffect(localSearchString){
        if(localSearchString.text.isEmpty()){
            onQueryChange(TextFieldValue(""))
        }

        delay(250.milliseconds)

        onQueryChange(localSearchString)
    }

    Column(
        modifier = Modifier.background(Color.White)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(65.dp)
                .background(Color.White)
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            TopBarButtons(
                {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.DeepMatteBlack
                    )
                },
                { backClick() },
                0.dp
            )

            BasicTextField(
                value = localSearchString,

                onValueChange = { localSearchString = it },

                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .weight(1f)
                    .height(40.dp)
                    .onFocusChanged { focusState ->
                        if(focusState.isFocused){
                            unClickSearch()
                        }
                    },

                singleLine = true,

                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black,
                    platformStyle = PlatformTextStyle(includeFontPadding = false) // حماية إضافية للخط
                ),

                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),

                keyboardActions = KeyboardActions(
                    onSearch = {
                        clickSearch(localSearchString.text)
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                ),

                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFF5F5F5), CircleShape)
                            .border(1.dp, Color.LightGray.copy(alpha = 0.6f), CircleShape)
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (localSearchString.text.isEmpty()) {
                                Text(
                                    text = "Search for food or restaurant",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                            innerTextField()
                        }

                        if (localSearchString.text.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    localSearchString = TextFieldValue("")
                                    onQueryChange(TextFieldValue(""))
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear Text",
                                    tint = Color.DeepMatteBlack,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            )

            TopBarButtons(
                {
                    BadgedBox(
                        badge = {
                            if(totalInCart > 0){
                                Badge(
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                ){
                                    if(totalInCart < 10){
                                        Text(text = "$totalInCart")
                                    }else{ Text(text = "+9", fontSize = 8.sp) }
                                }
                            }
                        }
                    ){
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                { cartClick() },
                0.dp
            )
        }
        Divider(color = Color.LightGray)
    }
}