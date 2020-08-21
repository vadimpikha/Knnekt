package knnekt.presentation.ui.main.chats.messages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import knnekt.shared.data.entity.Chat
import knnekt.shared.data.entity.Message
import knnekt.presentation.lifecycle.Event
import knnekt.presentation.lifecycle.asEvent
import knnekt.shared.domain.messages.GetMessagesPagingUseCase

class ChatViewModel(
    private val getMessagesPagingUseCase: GetMessagesPagingUseCase
) : ViewModel() {

    val outgoingMessageText = MutableLiveData("")
    val toast = MutableLiveData<Event<String>>()


    fun getMessagesPagingData(chatId: String) =
        getMessagesPagingUseCase(chatId).cachedIn(viewModelScope)


    private fun onError(t: Throwable) {
        toast.value = (t.message ?: "Error").asEvent()
    }

}