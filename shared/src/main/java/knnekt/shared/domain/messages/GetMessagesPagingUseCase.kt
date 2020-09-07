package knnekt.shared.domain.messages

import androidx.paging.PagingData
import androidx.paging.map
import knnekt.shared.data.db.MessageEntity
import knnekt.shared.data.entity.Message
import knnekt.shared.data.mapper.Mapper
import knnekt.shared.data.messages.MessagesRepository
import knnekt.shared.domain.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMessagesPagingUseCase(
    private val messagesRepository: MessagesRepository,
    private val entityToUiMessageMapper: Mapper<MessageEntity, Message>
): UseCase<String, Flow<PagingData<Message>>>() {

    override fun execute(parameters: String): Flow<PagingData<Message>> {
        return messagesRepository.getMessagesPagingData(parameters)
            .map { data ->
                data.map { entityToUiMessageMapper.convert(it) }
            }
    }

    suspend fun invalidate(chatId: String) {
        messagesRepository.refreshChatMessages(chatId)
    }
}