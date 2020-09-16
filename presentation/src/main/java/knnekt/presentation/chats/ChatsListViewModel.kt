package knnekt.presentation.chats

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import knnekt.domain.chats.ArchiveChatUseCase
import knnekt.domain.chats.GetChatsPagingUseCase
import knnekt.presentation.util.Event
import kotlinx.coroutines.launch

class ChatsListViewModel(
    private val getChatsPagingUseCase: GetChatsPagingUseCase,
    private val archiveChatUseCase: ArchiveChatUseCase
) : ViewModel() {

    val chatsPagingData = getChatsPagingUseCase(GetChatsPagingUseCase.Param(false))
        .cachedIn(viewModelScope)

    val toastEvent = MutableLiveData<Event<String>>()

    private fun onError(t: Throwable) {
        toastEvent.value = Event(t.message ?: "Error occurred")
    }

    fun archiveChat(chatId: String) {
        viewModelScope.launch {
            archiveChatUseCase(ArchiveChatUseCase.Param(chatId, true))
        }
    }
}