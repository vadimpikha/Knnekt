package knnekt.presentation.ui.main.chats.messages

import androidx.lifecycle.*
import androidx.paging.cachedIn
import knnekt.presentation.lifecycle.Event
import knnekt.presentation.lifecycle.asEvent
import knnekt.shared.data.connection.ChatConnectionManager
import knnekt.shared.data.entity.Chat
import knnekt.shared.domain.messages.GetMessagesPagingUseCase
import knnekt.shared.result.ifNotHandled
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

class ChatViewModel(
    private val currentChat: Chat,
    private val getMessagesPagingUseCase: GetMessagesPagingUseCase,
    private val connectionManager: ChatConnectionManager
) : ViewModel() {

    private val _messageJustReceived = AtomicBoolean(false)

    val messageJustReceived: Boolean
        get() = _messageJustReceived.getAndSet(false)

    val toast = MutableLiveData<Event<String>>()

    val messagesPagingData = getMessagesPagingUseCase(currentChat.id)
        .cachedIn(viewModelScope)

    val scrollToEvent = MutableLiveData<Event<Int>>()

    init {
        viewModelScope.launch {
            connectionManager.chatInvalidatedEvent
                .filter { it.peekContent() == currentChat.id }
                .collectLatest { event ->
                    Timber.d("Invalidation chat ${event.peekContent()}")
                    _messageJustReceived.set(true)
                    event.ifNotHandled { getMessagesPagingUseCase.invalidate(it) }
                }
        }
    }

    fun scrollDown() {
        scrollToEvent.value = Event(0)
    }

    private fun onError(t: Throwable) {
        toast.value = (t.message ?: "Error").asEvent()
    }

}