package knnekt.data

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.connectycube.chat.ConnectycubeChatService
import com.connectycube.core.EntityCallback
import com.connectycube.core.exception.ResponseException
import com.connectycube.users.model.ConnectycubeUser
import knnekt.domain.entity.User
import knnekt.domain.prefs.PreferencesDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import org.jivesoftware.smack.AbstractConnectionListener
import org.jivesoftware.smack.XMPPConnection
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

class ChatConnectionManager(
    private val localPreferencesRepository: PreferencesDataSource
) {

    private val isPending = AtomicBoolean(false)
    private val isInitialized = AtomicBoolean(false)
//    private val chatAppObserver = ChatAppLifecycleObserver()

//    val chatInvalidatedEvent = MutableLiveData<>
//
//    private val messageListener = object : ChatDialogMessageListener {
//        override fun processMessage(
//            dialogId: String,
//            chatMessage: ConnectycubeChatMessage,
//            senderId: Int?
//        ) {
//            chatInvalidatedEvent.value = Event(dialogId)
//        }
//
//        override fun processError(
//            dialogId: String,
//            ex: ChatException?,
//            chatMessage: ConnectycubeChatMessage,
//            senderId: Int?
//        ) {
//            Timber.d(ex)
//        }
//
//    }

    private fun initialize() {
        Timber.d("initWith, isPending ${isPending.get()}")
        if (isPending.get() || isInitialized.get()) return

        isPending.set(true)
        initConnectionListener()
        val user = convertUser(localPreferencesRepository.currentUser)

        Timber.d("Start chat login")

        ConnectycubeChatService.getInstance().login(user,
            object : EntityCallback<Void> {
                override fun onSuccess(void: Void?, bundle: Bundle?) {
                    Timber.d("Logged in")
                    isPending.set(false)
                    isInitialized.set(true)
                    registerMessageListener()
//                    chatAppObserver.onLoggedIn()
//                    chatAppObserver.registeredObserver()
                }

                override fun onError(ex: ResponseException) {
                    isPending.set(false)
                    Timber.d("Error while login to chat, error = ${ex.message}")
                }
            })
    }

    private fun registerMessageListener() {
//        ConnectycubeChatService.getInstance().incomingMessagesManager?.addDialogMessageListener(
//            messageListener
//        )
    }

    private fun convertUser(user: User?): ConnectycubeUser? {
        if (user == null) return null

        return ConnectycubeUser().apply {
            id = user.id
            login = user.login
            password = user.password
        }
    }

    private fun initConnectionListener() {
        ConnectycubeChatService.getInstance().addConnectionListener(object :
            AbstractConnectionListener() {
            override fun authenticated(connection: XMPPConnection?, resumed: Boolean) {
                Timber.d("authenticated")
            }

            override fun connectionClosedOnError(e: Exception) {
                Timber.d("connectionClosedOnError e= $e")
            }
        })
    }

    fun enterActiveState() {
        initialize()
    }
}