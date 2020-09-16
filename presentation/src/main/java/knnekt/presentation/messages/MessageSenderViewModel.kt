package knnekt.presentation.messages

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import knnekt.data.util.processScope
import knnekt.domain.messages.SendMessageUseCase
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class MessageSenderViewModel(
    private val chatId: String,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    private val _messageJustSent = AtomicBoolean(false)

    val messageJustSent: Boolean
        get() = _messageJustSent.getAndSet(false)

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
        processScope.launch {
            _messageJustSent.set(true)
            sendMessageUseCase(SendMessageUseCase.Params(body, chatId))
        }
    }
}