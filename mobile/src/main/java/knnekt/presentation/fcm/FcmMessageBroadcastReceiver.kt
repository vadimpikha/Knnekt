package knnekt.presentation.fcm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class FcmMessageBroadcastReceiver : BroadcastReceiver() {

    lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val message = intent.getStringExtra("message") ?: return
        val from = intent.getStringExtra("from") ?: return

    }


    fun showNotification(title: String, message: String) {


    }


    fun createChannelIfNeeded() {

    }
}