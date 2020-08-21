package knnekt.shared.data.connection

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.connectycube.auth.session.ConnectycubeSettings
import com.connectycube.chat.ConnectycubeChatService
import com.connectycube.chat.exception.ChatException
import com.connectycube.chat.listeners.ChatDialogMessageListener
import com.connectycube.chat.model.ConnectycubeChatMessage
import com.connectycube.core.EntityCallback
import com.connectycube.core.exception.ResponseException
import com.connectycube.users.model.ConnectycubeUser
import knnekt.shared.data.entity.User
import knnekt.shared.data.lifecycle.ChatAppLifecycleObserver
import knnekt.shared.domain.repository.LocalPreferencesRepository
import knnekt.shared.result.Event
import org.jivesoftware.smack.AbstractConnectionListener
import org.jivesoftware.smack.XMPPConnection
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

class ChatConnectionManager(
    private val localPreferencesRepository: LocalPreferencesRepository
) {

    private val isPending = AtomicBoolean(false)
    private val isInitialized = AtomicBoolean(false)
    private val chatAppObserver = ChatAppLifecycleObserver()

    val chatInvalidatedEvent = MutableLiveData<Event<String>>()

    private val messageListener = object : ChatDialogMessageListener {
        override fun processMessage(
            dialogId: String,
            chatMessage: ConnectycubeChatMessage,
            senderId: Int?
        ) {
            chatInvalidatedEvent.value = Event(dialogId)
        }

        override fun processError(
            dialogId: String,
            ex: ChatException?,
            chatMessage: ConnectycubeChatMessage,
            senderId: Int?
        ) {
            Timber.d(ex)
        }

    }

    fun initialize() {
        Timber.d("initWith, isPending ${isPending.get()}")
        if (isPending.get() || isInitialized.get()) return

        isPending.set(true)
        Timber.d("Start chat login")
        initConnectionListener()
        ConnectycubeChatService.getInstance().login(
            convertUser(localPreferencesRepository.user),
            object : EntityCallback<Void> {
                override fun onSuccess(void: Void?, bundle: Bundle?) {
                    isPending.set(false)
                    isInitialized.set(true)
                    registerAppLifeCycleObserver()
                    registerMessageListener()
                }

                override fun onError(ex: ResponseException) {
                    isPending.set(false)
                    Timber.d("Error while login to chat, error = ${ex.message}")
                }
            })
    }

    private fun registerMessageListener() {
        ConnectycubeChatService.getInstance().incomingMessagesManager?.addDialogMessageListener(
            messageListener
        )
    }

    private fun convertUser(user: User?): ConnectycubeUser? {
        if (user == null) return null

        return ConnectycubeUser().apply {
            id = user.id
            login = user.login
            password = user.password
            fullName = user.fullName
            avatar = user.avatar
        }
    }

    private fun initConnectionListener() {
        ConnectycubeChatService.getInstance().addConnectionListener(object :
            AbstractConnectionListener() {
            override fun authenticated(connection: XMPPConnection?, resumed: Boolean) {
                Timber.d("authenticated")
                chatAppObserver.onLoggedIn()
            }

            override fun connectionClosedOnError(e: Exception) {
                Timber.d("connectionClosedOnError e= $e")
            }
        })
    }

    private fun registerAppLifeCycleObserver() {
        chatAppObserver.registeredObserver()
    }

    private fun unregisterAppLifeCycleObserver() {
        chatAppObserver.unregisteredObserver()
    }

    fun terminate() {
        ConnectycubeChatService.getInstance().destroy()
        unregisterAppLifeCycleObserver()
        isPending.set(false)
        isInitialized.set(false)
    }
}