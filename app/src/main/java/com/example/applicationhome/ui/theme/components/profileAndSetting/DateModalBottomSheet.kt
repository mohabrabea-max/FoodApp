package com.example.applicationhome.ui.theme.components.profileAndSetting

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationhome.data.data.repository.ProfileData
import com.example.applicationhome.ui.theme.DarkOrange
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateModalBottomSheet(
    selectedDay : Int,
    selectedMonth : Int,
    selectedYear : Int,
    stateFalse : () -> Unit,
    setDay : (Int) -> Unit,
    setMonth : (Int) -> Unit,
    setYear : (Int) -> Unit,
    birthday : () -> Unit
){
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    ModalBottomSheet(
        onDismissRequest = {stateFalse()},
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
                DatePickerWheel(items = days, initialValue = selectedDay) { setDay(it) }

                // 2. سكرول الشهور
                DatePickerWheel(items = months, initialValue = selectedMonth) { setMonth(it) }

                // 3. سكرول السنين
                DatePickerWheel(items = years, initialValue = selectedYear) { setYear(it) }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // زرار التأكيد
            Button(
                onClick = {
                    stateFalse()
                    birthday()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("تأكيد التاريخ")
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