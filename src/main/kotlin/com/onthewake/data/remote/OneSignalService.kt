package com.onthewake.data.remote

import com.onthewake.data.remote.dto.Notification


interface OneSignalService {

    suspend fun sendNotification(notification: Notification): Boolean

    companion object {
        const val ONESIGNAL_APP_ID = "8890bfbd-a1d0-426a-98ae-88aa5b954b68"
        const val NOTIFICATIONS = "https://onesignal.com/api/v1/notifications"
    }
}