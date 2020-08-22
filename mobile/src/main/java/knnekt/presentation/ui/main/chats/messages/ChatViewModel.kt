package knnekt.presentation.ui.main.chats.messages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.connectycube.chat.model.ConnectycubeChatDialog
import knnekt.shared.data.entity.Chat
import knnekt.shared.data.entity.Message
import knnekt.presentation.lifecycle.Event
import knnekt.presentation.lifecycle.asEvent
import knnekt.shared.domain.chats.GetChatConnectionUseCase
import knnekt.shared.domain.messages.GetMessagesPagingUseCase
import kotlinx.coroutines.launch

class ChatViewModel(
    private val currentChat: Chat,
    private val getMessagesPagingUseCase: GetMessagesPagingUseCase,
    private val getChatConnectionUseCase: GetChatConnectionUseCase
) : ViewModel() {

    private val chatConnection = getChatConnectionUseCase(currentChat)

    val messagesPagingData = getMessagesPagingUseCase(currentChat.id).cachedIn(viewModelScope)

    val outgoingMessageText = MutableLiveData("")
    val toast = MutableLiveData<Event<String>>()

    fun send() {
        val text = outgoingMessageText.value!!
        viewModelScope.launch {
            chatConnection.sendMessage(text)
        }
    }

    private fun onError(t: Throwable) {
        toast.value = (t.message ?: "Error").asEvent()
    }

}