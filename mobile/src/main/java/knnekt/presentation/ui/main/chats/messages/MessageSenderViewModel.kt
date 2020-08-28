package knnekt.presentation.ui.main.chats.messages

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import knnekt.presentation.lifecycle.Event
import knnekt.shared.data.entity.Chat
import knnekt.shared.domain.messages.SendMessageUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class MessageSenderViewModel(
    private val chat: Chat,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    private val _scrollNeeded = AtomicBoolean(false)

    val scrollNeeded: Boolean
        get() = _scrollNeeded.getAndSet(false)

    private val nonCancellableScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    val messageText = MutableLiveData("")

    val dataForSendExists = MediatorLiveData<Boolean>().apply {
        fun rebind() {
            value = !messageText.value.isNullOrEmpty()
        }
        addSource(messageText) {
            rebind()
        }
    }.distinctUntilChanged()


    fun send() {
        val body = messageText.value!!
        messageText.value = ""
        nonCancellableScope.launch {
            _scrollNeeded.set(true)
            sendMessageUseCase(SendMessageUseCase.Params(body, chat.id))
        }
    }
}