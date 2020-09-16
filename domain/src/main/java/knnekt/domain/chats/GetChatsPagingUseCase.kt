package knnekt.domain.chats

import androidx.paging.PagingData
import androidx.paging.filter
import knnekt.domain.usecase.UseCase
import knnekt.domain.entity.Chat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetChatsPagingUseCase(
    private val chatsRepository: ChatsRepository
) : UseCase<GetChatsPagingUseCase.Param, Flow<PagingData<Chat>>>() {

    override fun execute(parameters: Param): Flow<PagingData<Chat>> {
        return chatsRepository.getChatsPagingData()
            .map { data ->
                data.filter { it.isArchived == parameters.archived }
            }
    }

    data class Param(val archived: Boolean)
}