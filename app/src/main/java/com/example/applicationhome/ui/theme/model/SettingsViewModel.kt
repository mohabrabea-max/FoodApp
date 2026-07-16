package com.example.applicationhome.ui.theme.model

import androidx.lifecycle.ViewModel
import com.example.applicationhome.data.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    userRepository: UserRepository
): ViewModel() {
    val userData = userRepository.userData
}