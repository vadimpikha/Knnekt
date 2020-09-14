package knnekt.presentation.fcm

import android.annotation.SuppressLint
import com.connectycube.pushnotifications.services.fcm.FcmPushListenerService
import knnekt.presentation.notifications.AppNotificationManager
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FcmService : FcmPushListenerService(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private val appNotificationManager: AppNotificationManager by instance()

    override fun sendPushMessage(
        data: MutableMap<Any?, Any?>,
        message: String,
        messageId: String?,
        dialogId: String?,
        userId: String?
    ) {
        super.sendPushMessage(data, message, messageId, dialogId, userId)
        appNotificationManager.showNewMessageNotification(
            this,
            "Knnekt",
            message,
            dialogId ?: ""
        )
    }

}