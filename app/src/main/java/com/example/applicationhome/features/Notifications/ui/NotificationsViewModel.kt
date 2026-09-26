package com.example.applicationhome.features.Notifications.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applicationhome.core.domain.repository.NotificationsRepository
import com.example.applicationhome.core.domain.repository.UserRepository
import com.example.applicationhome.core.ui.mapper.notificationDomainClassToNotificationUIClass
import com.example.applicationhome.core.ui.model.NotificationUIClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class NotificationsViewModel @Inject constructor(
    userRepository: UserRepository,
    private val notificationsRepository: NotificationsRepository
): ViewModel(){
    val userData = userRepository.userData

    val notifications : StateFlow<List<NotificationUIClass>> =
        userData.flatMapLatest { user ->
            notificationsRepository.getNotifications(user.id)
                .map { item ->
                    item.map {
                        it.notificationDomainClassToNotificationUIClass()
                    }
                }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteNotification(id : Int){
        viewModelScope.launch {
            notificationsRepository.deleteNotification(id)
        }
    }
}