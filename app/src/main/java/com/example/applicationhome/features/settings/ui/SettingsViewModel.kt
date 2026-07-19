package com.example.applicationhome.features.settings.ui

import androidx.lifecycle.ViewModel
import com.example.applicationhome.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    userRepository: UserRepository
): ViewModel() {
    val userData = userRepository.userData
}