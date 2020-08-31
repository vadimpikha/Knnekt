package knnekt.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import knnekt.shared.data.connection.ChatConnectionManager
import knnekt.shared.domain.chats.InvalidateChatUseCase
import knnekt.shared.result.ifNotHandled
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val connectionManager: ChatConnectionManager,
    invalidateChatUseCase: InvalidateChatUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            connectionManager.chatInvalidatedEvent.collectLatest { event ->
                event.ifNotHandled { chatId ->
                    invalidateChatUseCase(chatId)
                }
            }
        }
    }

    fun enterActiveState() {
        connectionManager.enterActiveState()
    }

}