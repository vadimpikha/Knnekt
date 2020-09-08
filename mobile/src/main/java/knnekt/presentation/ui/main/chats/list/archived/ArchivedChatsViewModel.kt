package knnekt.presentation.ui.main.chats.list.archived

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import knnekt.shared.domain.chats.ArchiveChatUseCase
import knnekt.shared.domain.chats.GetChatsPagingUseCase
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