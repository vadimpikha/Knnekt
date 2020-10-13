package knnekt.presentation.messages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import knnekt.domain.chats.GetChatByIdUseCase
import knnekt.domain.messages.GetMessagesPagingUseCase
import knnekt.presentation.util.Event
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class ChatMessagesViewModel(
    private val chatId: String,
    private val getMessagesPagingUseCase: GetMessagesPagingUseCase,
    private val getChatByIdUseCase: GetChatByIdUseCase
) : ViewModel() {

    val chat = getChatByIdUseCase(chatId).asLiveData()

    private val _messageJustReceived = AtomicBoolean(false)

    val messageJustReceived: Boolean
        get() = _messageJustReceived.getAndSet(false)

    val toast = MutableLiveData<Event<String>>()

    val messagesPagingData = getMessagesPagingUseCase(chatId)
        .cachedIn(viewModelScope)

    val scrollToEvent = MutableLiveData<Event<Int>>()

    fun scrollDown() {
        scrollToEvent.value = Event(0)
    }

    private fun onError(t: Throwable) {
        toast.value = Event(t.message ?: "Error")
    }

}