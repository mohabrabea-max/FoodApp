package com.example.applicationhome

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.applicationhome.core.data.local.dao.UsersDao
import com.example.applicationhome.core.data.local.entity.NotificationEntity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var usersDao : UsersDao

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.data["title"] ?: remoteMessage.notification?.title ?: "إشعار جديد"
        val body = remoteMessage.data["body"] ?: remoteMessage.notification?.body ?: ""
        val timestamp = if (remoteMessage.sentTime > 0) {
            remoteMessage.sentTime
        } else {
            System.currentTimeMillis()
        }

        CoroutineScope(Dispatchers.IO).launch {
            val activeUserId = usersDao.getActiveUserNotFlow()

            usersDao.insertNotification(
                NotificationEntity(
                    userId = activeUserId?.id?: "",
                    title = title,
                    body = body,
                    timestamp = timestamp
                )
            )
        }

        // 3. إظهار الإشعار بخصائص الأيقونة واللون اللي ضبطناهم
        showNotification(title, body)
    }

    private fun showNotification(title: String, body: String) {
        val channelId = "default_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification_icon)
            .setColor(ContextCompat.getColor(this, R.color.dark_orange))
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}