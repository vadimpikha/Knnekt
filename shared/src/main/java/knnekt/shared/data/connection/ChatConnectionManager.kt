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
import knnekt.shared.result.handled
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    val chatInvalidatedEvent = MutableStateFlow(Event("").handled())

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

    private fun initialize() {
        Timber.d("initWith, isPending ${isPending.get()}")
        if (isPending.get() || isInitialized.get()) return

        isPending.set(true)
        initConnectionListener()
        val user = convertUser(localPreferencesRepository.user)

        Timber.d("Start chat login")

        ConnectycubeChatService.getInstance().login(user,
            object : EntityCallback<Void> {
                override fun onSuccess(void: Void?, bundle: Bundle?) {
                    Timber.d("Logged in")
                    isPending.set(false)
                    isInitialized.set(true)
                    registerMessageListener()
                    chatAppObserver.onLoggedIn()
                    chatAppObserver.registeredObserver()
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