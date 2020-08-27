package knnekt.presentation.ui.main.chats.messages

import androidx.lifecycle.*
import androidx.paging.cachedIn
import knnekt.presentation.lifecycle.Event
import knnekt.presentation.lifecycle.asEvent
import knnekt.shared.data.entity.Chat
import knnekt.shared.domain.chats.GetChatConnectionUseCase
import knnekt.shared.domain.messages.GetMessagesPagingUseCase

class ChatViewModel(
    private val currentChat: Chat,
    private val getMessagesPagingUseCase: GetMessagesPagingUseCase,
    private val getChatConnectionUseCase: GetChatConnectionUseCase
) : ViewModel() {

    val toast = MutableLiveData<Event<String>>()

//    private val chatConnection = getChatConnectionUseCase(currentChat)

    val messagesPagingData = getMessagesPagingUseCase(currentChat.id)
        .cachedIn(viewModelScope)


    val outgoingMessageText = MutableLiveData("")

    val outgoingMessageTextNotEmpty = MediatorLiveData<Boolean>().apply {
        fun rebind() {
            value = !outgoingMessageText.value.isNullOrEmpty()
        }
        addSource(outgoingMessageText) {
            rebind()
        }
    }.distinctUntilChanged()

    fun send() {
        val text = outgoingMessageText.value!!
        outgoingMessageText.value = ""
//        viewModelScope.launch {
//            chatConnection.sendMessage(text)
//        }
    }

    private fun onError(t: Throwable) {
        toast.value = (t.message ?: "Error").asEvent()
    }

}