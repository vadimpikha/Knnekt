package knnekt.presentation.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

interface Ap/*pNotificationManager {

    fun showNewMessageNotification(context: Context, title: String, message: String, chatId: String)

}

class AppNotificationManagerImpl(
    private val notificationManager: NotificationManagerCompat,
    private val appResources: AppResources
) : AppNotificationManager {

    companion object {
        private const val CHANNEL_ID = "chats_channel"

        private const val CHAT_NOTIFICATION_ID = 1
    }

    init {
        createChannelIfNeeded()
    }


    override fun showNewMessageNotification(
        context: Context,
        title: String,
        message: String,
        chatId: String
    ) {

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.common_full_open_on_phone)
            .setContentTitle(title)
            .setContentText(message)
            .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()


           notificationManager.notify(CHAT_NOTIFICATION_ID, notification)
    }

    private fun createChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(CHANNEL_ID) != null) return

            val name = appResources.getString(R.string.chats_channel)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
    }


}*/