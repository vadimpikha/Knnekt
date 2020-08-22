package knnekt.presentation.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectycube.chat.ConnectycubeChatService
import com.connectycube.chat.exception.ChatException
import com.connectycube.chat.listeners.ChatDialogMessageListener
import com.connectycube.chat.listeners.MessageDeleteListener
import com.connectycube.chat.listeners.MessageStatusListener
import com.connectycube.chat.listeners.MessageUpdateListener
import com.connectycube.chat.model.ConnectycubeChatMessage
import knnekt.shared.data.connection.ChatConnectionManager
import knnekt.shared.data.entity.Chat
import knnekt.shared.domain.chats.InvalidateChatUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    private val connectionManager: ChatConnectionManager,
    invalidateChatUseCase: InvalidateChatUseCase
) : ViewModel() {


    val currentChat = MutableLiveData<Chat>(null)


    fun setCurrentChat(chat: Chat?) {
        currentChat.value = chat
    }

    init {
        connectionManager.chatInvalidatedEvent.observeForever {
            it.getContentIfNotHandled()?.let { chatId ->
                viewModelScope.launch {
                    invalidateChatUseCase(chatId)
                }
            }
        }
    }

    fun ping() {

    }

}