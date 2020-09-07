package knnekt.presentation.ui.main.chats.list.archived

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import knnekt.shared.domain.chats.GetChatsPagingUseCase

class ArchivedChatsViewModel(
    val getChatsPagingUseCase: GetChatsPagingUseCase
): ViewModel() {

    val archivedChatsPagingData = getChatsPagingUseCase(GetChatsPagingUseCase.Param(true))
        .cachedIn(viewModelScope)

}