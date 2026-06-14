package com.example.applicationhome.ui.theme.components.profileAndSetting

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applicationhome.data.models.repository.ProfileData
import com.example.applicationhome.ui.theme.BrownForFont
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.MediumBrownForTitle
import com.example.applicationhome.ui.theme.VeryLightGray
import com.example.applicationhome.ui.theme.model.BirthdayViewModel
import com.example.applicationhome.ui.theme.model.DrawerViewModel
import com.example.applicationhome.ui.theme.model.ProfileViewModel
import com.example.applicationhome.ui.theme.model.UserImageViewModel
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBox(
    userImageViewModel: UserImageViewModel,
    profileViewModel : ProfileViewModel,
    birthdayViewModel: BirthdayViewModel,
    drawerViewModel: DrawerViewModel = viewModel()
){
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var sheetStateViewModel = drawerViewModel.sheetState
    var profile = profileViewModel.profile
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequesters = remember {
        profile.associate { it.id to FocusRequester() }
    }
    Column(modifier = Modifier.fillMaxSize().background(Color.VeryLightGray).padding(15.dp)){
        Spacer(modifier = Modifier.height(70.dp))
        Column(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)).background(Color.White).padding(17.dp)){
            Text(text = "Personal Information", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(15.dp))
            profile.forEach{ item ->
                val state = rememberTextFieldState()
                val focusRequester = focusRequesters[item.id]
                Column(modifier = Modifier.fillMaxSize()){
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                        Column (modifier = Modifier.weight(7f), horizontalAlignment = Alignment.Start){
                            //Spacer(modifier = Modifier.width(15.dp))
                            Text(text = item.title, fontSize = 17.sp, color = Color.BrownForFont)
                            Spacer(modifier = Modifier.height(10.dp))
                            Row {
                                Spacer(modifier = Modifier.width(3.dp))
                                if(item.title == "First Name" || item.title == "Last Name" || item.title == "Phone number"){
                                    Box(contentAlignment = Alignment.CenterStart){
                                        if (item.value == null && state.text.isEmpty()) {
                                            Text(
                                                text = item.empty,
                                                color = Color.Gray,
                                                fontSize = 14.sp
                                            )
                                        }else if(state.text.isEmpty()){
                                            Text(
                                                text = item.value.toString(),
                                                color = Color.MediumBrownForTitle,
                                                fontSize = 14.sp
                                            )
                                        }
                                        BasicTextField(
                                            state = state,
                                            modifier = Modifier.fillMaxSize().
                                            then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier).
                                            onFocusChanged { focusState ->
                                                if (focusState.isFocused) {
                                                    userImageViewModel.statetrue()
                                                }
                                            },
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType =
                                                    if(item.title == "First Name" || item.title == "Last Name")
                                                        KeyboardType.Text
                                                    else
                                                        KeyboardType.Phone, imeAction = ImeAction.Done
                                            ),
                                            onKeyboardAction = {
                                                userImageViewModel.statefalse()
                                                keyboardController?.hide()
                                                focusManager.clearFocus()
                                                profileViewModel.changeProfileData(item, state)
                                            },
                                            textStyle = TextStyle(
                                                fontSize = 14.sp,
                                                color = Color.MediumBrownForTitle
                                            )
                                        )
                                    }

                                }else if(item.title == "Email"){
                                    Text(
                                        text = item.value.toString(),
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                        if(item.icon != null){
                            IconButton(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    if(item.icon == Icons.Default.Add){
                                        drawerViewModel.stateTrue()
                                    }else{
                                        focusRequester?.requestFocus()
                                    }
                                }
                            ){
                                Icon(
                                    modifier = Modifier.size(22.dp),
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = if(item.icon == Icons.Default.Add) Color.Blue else Color.Gray
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    if(item != profile.last()) Divider(color = Color.LightGray.copy(alpha = 0.2f))
                }
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
    if(sheetStateViewModel){
        ModalBottomSheet(
            onDismissRequest = {drawerViewModel.stateFalse()},
            sheetState = sheetState
        ){
            val days = ProfileData.days
            val months = ProfileData.months
            val years = ProfileData.years
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .pointerInput(Unit) {
                        coroutineScope {
                            detectDragGestures { change, dragAmount ->
                                change.consume() // ابلع الحركة وماتمررهاش للشيت
                            }
                        }
                    }
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "اختر تاريخ ميلادك",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // صف بيجمع الـ 3 سكرولات جنب بعض
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp), // طول منطقة السكرول
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // 1. سكرول الأيام
                    DatePickerWheel(items = days, initialValue = birthdayViewModel.selectedDay) { birthdayViewModel.selectedDay = it }

                    // 2. سكرول الشهور
                    DatePickerWheel(items = months, initialValue = birthdayViewModel.selectedMonth) { birthdayViewModel.selectedMonth = it }

                    // 3. سكرول السنين
                    DatePickerWheel(items = years, initialValue = birthdayViewModel.selectedYear) { birthdayViewModel.selectedYear = it }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // زرار التأكيد
                Button(
                    onClick = {
                        drawerViewModel.stateFalse()
                        birthdayViewModel.birthday(birthdayViewModel.selectedDay, birthdayViewModel.selectedMonth, birthdayViewModel.selectedYear)
                              },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("تأكيد التاريخ")
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DatePickerWheel(
    items: List<Int>,
    initialValue: Int,
    onValueChange: (Int) -> Unit
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = items.indexOf(initialValue).coerceAtLeast(0))

    // الميزة دي بتخلي السكرول "يقفش" على الرقم وميقفش في النص بين رقمين (تأثير العجلة الدوارة)
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    // بنراقب أول عنصر باين في النص عشان نحدث القيمة فوراً والمستخدم بيعمل سكرول
    LaunchedEffect(listState.firstVisibleItemIndex) {
        if (items.isNotEmpty()) {
            onValueChange(items[listState.firstVisibleItemIndex])
        }
    }

    Box(
        modifier = Modifier
            .width(80.dp)
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        // خطوط صغننة تحدد العنصر اللي في النص (شكل جمالي)
        Divider(modifier = Modifier.fillMaxWidth().align(Alignment.Center).offset(y = (-20).dp))
        Divider(modifier = Modifier.fillMaxWidth().align(Alignment.Center).offset(y = 20.dp))

        LazyColumn(
            state = listState,
            flingBehavior = snapFlingBehavior, // تركيب ميزة القفش التلقائي
            contentPadding = PaddingValues(vertical = 60.dp), // عشان نسيب مساحة فوق وتحت يظهروا فاضيين
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items.size) { index ->
                Text(
                    text = items[index].toString(),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 8.dp),
                    // لو العنصر هو اللي واقف في النص بنخليه غامق، لو بعيد بنخليه باهت
                    color = if (listState.firstVisibleItemIndex == index)
                        Color.DarkOrange
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
        }
    }
}