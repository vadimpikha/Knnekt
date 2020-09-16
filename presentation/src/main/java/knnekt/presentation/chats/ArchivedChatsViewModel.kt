package knnekt.presentation.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import knnekt.domain.chats.ArchiveChatUseCase
import knnekt.domain.chats.GetChatsPagingUseCase
import kotlinx.coroutines.launch

class ArchivedChatsViewModel(
    getChatsPagingUseCase: GetChatsPagingUseCase,
    private val archiveChatUseCase: ArchiveChatUseCase
) : ViewModel() {

    val archivedChatsPagingData = getChatsPagingUseCase(GetChatsPagingUseCase.Param(true))
        .cachedIn(viewModelScope)

    fun unarchiveChat(id: String) {
        viewModelScope.launch {
            archiveChatUseCase(ArchiveChatUseCase.Param(id, false))
        }
    }

}