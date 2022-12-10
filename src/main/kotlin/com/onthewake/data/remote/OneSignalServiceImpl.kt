package com.onthewake.data.remote

import com.onthewake.data.remote.dto.Notification
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*

@InternalAPI
class OneSignalServiceImpl(
    private val client: HttpClient,
    private val apiKey: String
): OneSignalService {

    override suspend fun sendNotification(notification: Notification): Boolean {
        return try {
            client.post {
                url(OneSignalService.NOTIFICATIONS)
                contentType(ContentType.Application.Json)
                header("Authorization", "Basic $apiKey")
                body = notification
            }
            true
        } catch(e: Exception) {
            e.printStackTrace()
            false
        }
    }
}