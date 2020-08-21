package knnekt.shared.domain.chats

import androidx.paging.PagingData
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
) : UseCase<Unit, Flow<PagingData<Chat>>>() {

    override fun execute(parameters: Unit): Flow<PagingData<Chat>> {
        return chatsRepository.getChatsPagingData()
            .map { data ->
                data.map { entityToUiChatMapper.convert(it) }
            }
    }
}