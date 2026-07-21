package com.example.applicationhome.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.net.ntp.NTPUDPClient
import java.net.InetAddress
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkTimeProvider @Inject constructor(){
    suspend fun getNtpNetworkTime(): Long = withContext(Dispatchers.IO) {
        try {
            val timeClient = NTPUDPClient().apply { defaultTimeout = 3000 }

            val inetAddress = InetAddress.getByName("time.google.com")

            val timeInfo = timeClient.getTime(inetAddress)
            timeInfo.message.transmitTimeStamp.time
        }catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
}