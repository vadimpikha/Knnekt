package knnekt.presentation.chats.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import knnekt.domain.chats.GetChatByIdUseCase
import knnekt.domain.messages.GetMessagesPagingUseCase

class ChatDetailsViewModel(
    private val chatId: String,
    private val getChatByIdUseCase: GetChatByIdUseCase
): ViewModel() {

    val chat = getChatByIdUseCase(chatId).asLiveData()

}