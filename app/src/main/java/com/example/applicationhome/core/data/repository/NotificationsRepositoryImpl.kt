package com.example.applicationhome.core.data.repository

import com.example.applicationhome.core.data.local.dao.UsersDao
import com.example.applicationhome.core.data.mapper.notificationEntityToNotificationDomainClass
import com.example.applicationhome.core.domain.model.NotificationDomainClass
import com.example.applicationhome.core.domain.repository.NotificationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationsRepositoryImpl @Inject constructor(
    private val usersDao: UsersDao
): NotificationsRepository {
    override fun getNotifications(userId: String): Flow<List<NotificationDomainClass>> =
        usersDao.getNotificationsForUser(userId).map { item ->
            item.map {
                it.notificationEntityToNotificationDomainClass()
            }
        }

    override suspend fun deleteNotification(id: Int) {
        usersDao.deleteNotification(id)
    }
}