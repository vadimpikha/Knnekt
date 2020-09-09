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

    val appNotificationManager: AppNotificationManager by instance()

//    data: {user_id=1731269, badge=62, message=You have 62 new messages, dialog_id=5f270a01ca8bf42eedfb723e}

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