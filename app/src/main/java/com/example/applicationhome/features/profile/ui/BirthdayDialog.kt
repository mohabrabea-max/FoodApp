package com.example.applicationhome.features.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.applicationhome.R
import com.example.applicationhome.core.ui.theme.BrownForFont
import com.example.applicationhome.core.ui.theme.DarkOrange
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayDialog(
    selectedDate : String,
    selectDate : (String) -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }

    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ){ showDatePicker = true }
    ) {
        OutlinedTextField(
            value = selectedDate,

            onValueChange = {  },

            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurface,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                errorBorderColor = Color.Red,
                disabledBorderColor = MaterialTheme.colorScheme.onSurface
            ),

            label = {
                Text(
                    stringResource(R.string.birthday),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },

            placeholder = {
                Text(
                    stringResource(R.string.birthday),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },

            readOnly = true,

            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(R.string.select_date),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),

            enabled = false,

            shape = RoundedCornerShape(20.dp),
        )
    }


    if(showDatePicker){
        Dialog(
            onDismissRequest = { showDatePicker = false },
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ){
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(shape = RoundedCornerShape(28.dp))
                    .wrapContentSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(bottom = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(
                        dividerColor = MaterialTheme.colorScheme.onSecondary,

                        // 1. الخلفية العامة والعناوين الرئيسية فوق
                        containerColor = MaterialTheme.colorScheme.surface,                  // خلفية صندوق النتيجة بالكامل
                        titleContentColor = Color.DarkGray,            // لون كلمة "Select date" الصغيرة فوق
                        headlineContentColor = Color.BrownForFont,     // لون التاريخ الكبير المكتوب (مثلاً: Sat, Jul 18)

                        subheadContentColor = Color.DarkGray,              // لون اسم الشهر والسنة (مثلاً: July 2026)
                        navigationContentColor = Color.DarkGray,           // لون أسهم التقليب بين الشهور (> و <)
                        weekdayContentColor = MaterialTheme.colorScheme.onSurfaceVariant,              // لون حروف أيام الأسبوع (S, M, T, W...)

                        // 3. أرقام الأيام (Days)
                        dayContentColor = MaterialTheme.colorScheme.onSurface,                    // لون أرقام الأيام العادية في الشهر
                        disabledDayContentColor = MaterialTheme.colorScheme.onSecondary,        // لون الأيام المقفولة (لو عامل حدود للتاريخ)

                        // 4. اليوم اللي المستخدم ضغط عليه واختاره (Selected Day)
                        selectedDayContainerColor = Color.DarkOrange,     // لون الدائرة اللي بتظهر ورا اليوم المختار
                        selectedDayContentColor = MaterialTheme.colorScheme.surface,            // لون رقم اليوم نفسه جوه الدائرة المختارة

                        // 5. اليوم الحالي (النهاردة - Today)
                        todayContentColor = Color.DarkOrange,           // لون رقم تاريخ النهاردة لو مش متبت عليه
                        todayDateBorderColor = Color.DarkOrange,        // لون الدائرة الخفيفة المحاوطة تاريخ النهاردة

                        // 6. لو المستخدم فتح لستة السنين (Year Picker)
                        yearContentColor = MaterialTheme.colorScheme.onSurface,                    // لون السنين العادية في اللستة
                        selectedYearContainerColor = Color.DarkOrange,   // خلفية السنة المحددة
                        selectedYearContentColor = MaterialTheme.colorScheme.surface,            // لون نص السنة المحددة
                        currentYearContentColor = Color.BrownForFont,       // لون السنة الحالية في منظومة الوقت

                        //7. الجزء الخاص بضغط القلم والكتابة اليدوية
                        dateTextFieldColors = TextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,               // لون الكلام اللي المستخدم بيكتبه وهو واقف جوه الخانة
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,             // لون الكلام لو خرج بره الخانة

                            focusedContainerColor = Color.Transparent,    // خلفية خانة الكتابة (شفافة أو أبيض حسب ذوقك)
                            unfocusedContainerColor = Color.Transparent,

                            cursorColor = Color.DarkOrange,             // لون مؤشر الكتابة الوميض (Cursor)

                            // الخط اللي بيبقى تحت خانة الإدخال (Indicator)
                            focusedIndicatorColor = Color.DarkOrange,      // لون الخط لما المستخدم يضغط ويبدأ يكتب
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSecondary,     // لون الخط في الحالة العادية
                            errorIndicatorColor = Color.Red,               // لون الخط لو كتب تاريخ غلط (الفايربيز بيطلع إيرور تلقائي لو التاريخ مش منطقي)

                            // العنوان الصغير اللي بيبقى مكتوب جوه الخانة (مثال: Date)
                            focusedLabelColor = MaterialTheme.colorScheme.onSurface,            // لونه لما يطير فوق
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface           // لونه وهو جوه الخانة
                        )
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp, top = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(stringResource(R.string.cancel), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = {
                        val dateInMillis = datePickerState.selectedDateMillis
                        if (dateInMillis != null) {
                            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                            selectDate(formatter.format(Date(dateInMillis)))
                        }
                        showDatePicker = false
                    }) {
                        Text(
                            stringResource(R.string.select),
                            color = Color.DarkOrange,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}