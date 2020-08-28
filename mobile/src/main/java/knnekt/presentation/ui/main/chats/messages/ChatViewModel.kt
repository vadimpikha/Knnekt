package knnekt.presentation.ui.main.chats.messages

import androidx.lifecycle.*
import androidx.paging.cachedIn
import knnekt.presentation.lifecycle.Event
import knnekt.presentation.lifecycle.asEvent
import knnekt.shared.data.entity.Chat
import knnekt.shared.domain.messages.GetMessagesPagingUseCase

class ChatViewModel(
    private val currentChat: Chat,
    private val getMessagesPagingUseCase: GetMessagesPagingUseCase
) : ViewModel() {

    val toast = MutableLiveData<Event<String>>()

    val messagesPagingData = getMessagesPagingUseCase(currentChat.id)
        .cachedIn(viewModelScope)

    val scrollToEvent = MutableLiveData<Event<Int>>()

    private fun onError(t: Throwable) {
        toast.value = (t.message ?: "Error").asEvent()
    }

}