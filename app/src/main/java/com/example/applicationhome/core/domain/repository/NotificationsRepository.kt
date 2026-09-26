package com.example.applicationhome.core.domain.repository

import com.example.applicationhome.core.domain.model.NotificationDomainClass
import kotlinx.coroutines.flow.Flow

interface NotificationsRepository {
    fun getNotifications(userId : String): Flow<List<NotificationDomainClass>>
    suspend fun deleteNotification(id : Int)
}