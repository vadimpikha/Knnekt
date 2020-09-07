package knnekt.presentation.ui.main.chats.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import knnekt.presentation.lifecycle.Event
import knnekt.presentation.lifecycle.asEvent
import knnekt.shared.data.entity.Chat
import knnekt.shared.domain.chats.GetChatsPagingUseCase

class ChatsListViewModel(
    private val getChatsPagingUseCase: GetChatsPagingUseCase,
) : ViewModel() {

    val chatsPagingData = getChatsPagingUseCase(Unit).cachedIn(viewModelScope)
    val toastEvent = MutableLiveData<Event<String>>()

    private fun onError(t: Throwable) {
        toastEvent.value = (t.message ?: "Error occurred").asEvent()
    }

    fun archiveChat(chat: Chat) {

    }
}