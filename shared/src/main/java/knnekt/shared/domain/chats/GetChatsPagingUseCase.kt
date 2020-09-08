package knnekt.shared.domain.chats

import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.insertSeparators
import androidx.paging.map
import knnekt.shared.data.chats.ChatsRepository
import knnekt.shared.data.db.ChatEntity
import knnekt.shared.data.entity.Chat
import knnekt.shared.data.mapper.Mapper
import knnekt.shared.domain.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetChatsPagingUseCase(
    private val chatsRepository: ChatsRepository,
    private val entityToUiChatMapper: Mapper<ChatEntity, Chat>
) : UseCase<GetChatsPagingUseCase.Param, Flow<PagingData<Chat>>>() {

    override fun execute(parameters: Param): Flow<PagingData<Chat>> {
        return chatsRepository.getChatsPagingData()
            .map { data ->
                data.filter { (it.prefs?.isArchived ?: false) == parameters.archived }
                    .map { it.chat }
                    .map { entityToUiChatMapper.convert(it) }
            }
    }

    data class Param(val archived: Boolean)
}