package com.example.applicationhome.features.WelcomeScreen.Ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.features.WelcomeScreen.repository.WelcomeScreenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    private val welcomeScreenRepository : WelcomeScreenRepository
): ViewModel() {
    fun updateFirstTimeToOpenApp(){
        viewModelScope.launch {
            welcomeScreenRepository.updateFirstTimeToOpenApp()
        }
    }
}