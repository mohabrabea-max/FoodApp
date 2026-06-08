package com.example.applicationhome.ui.theme.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class BirthdayViewModel: ViewModel() {
    var selectedDay by mutableStateOf(1)
    var selectedMonth by mutableStateOf(1)
    var selectedYear by mutableStateOf(2000)

    fun birthday(day : Int, month : Int, year : Int){
        selectedDay = day
        selectedMonth = month
        selectedYear = year
    }
}