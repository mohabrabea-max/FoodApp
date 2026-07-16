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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.data.data.repository.ProfileData
import com.example.applicationhome.ui.theme.DarkOrange
import com.example.applicationhome.ui.theme.model.ProfileViewModel
import com.example.applicationhome.ui.theme.model.UserImageViewModel
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBox(      // TODO: Under maintenance - This feature is currently in progress
    userImageViewModel: UserImageViewModel,
    profileViewModel : ProfileViewModel
){
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val sheetStateViewModel = profileViewModel.sheetState
    val profile = profileViewModel.profile
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequesters = remember {
        profile.associate { it.id to FocusRequester() }
    }

    Column(modifier = Modifier.padding(15.dp).fillMaxSize().clip(RoundedCornerShape(10.dp)).background(Color.White).padding(17.dp)){
        Text(text = "Personal Information", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(15.dp))
        profile.forEach{ item ->
        }
    }
    if(sheetStateViewModel){
        ModalBottomSheet(
            onDismissRequest = {profileViewModel.stateFalse()},
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
                    DatePickerWheel(items = days, initialValue = profileViewModel.selectedDay) { profileViewModel.selectedDay = it }

                    // 2. سكرول الشهور
                    DatePickerWheel(items = months, initialValue = profileViewModel.selectedMonth) { profileViewModel.selectedMonth = it }

                    // 3. سكرول السنين
                    DatePickerWheel(items = years, initialValue = profileViewModel.selectedYear) { profileViewModel.selectedYear = it }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // زرار التأكيد
                Button(
                    onClick = {
                        profileViewModel.stateFalse()
                        profileViewModel.birthday(profileViewModel.selectedDay, profileViewModel.selectedMonth, profileViewModel.selectedYear)
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