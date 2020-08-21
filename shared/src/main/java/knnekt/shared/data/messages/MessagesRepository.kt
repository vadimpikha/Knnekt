package knnekt.shared.data.messages

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.connectycube.chat.model.ConnectycubeChatMessage
import knnekt.shared.data.db.*
import knnekt.shared.data.mapper.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface MessagesRepository {

    fun getMessagesPagingData(chatId: String): Flow<PagingData<MessageEntity>>

}

class MessagesRepositoryImpl(
    private val messageDao: MessageDao,
    private val attachmentDao: AttachmentDao,
    private val messageWithAttachmentsDao: MessageWithAttachmentsDao,
    private val remoteToEntityMapper: Mapper<ConnectycubeChatMessage, MessageEntity>
) : MessagesRepository {

    override fun getMessagesPagingData(chatId: String): Flow<PagingData<MessageEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                enablePlaceholders = false
            ),
            remoteMediator = MessageRemoteMediator(
                chatId,
                messageDao,
                attachmentDao,
                remoteToEntityMapper
            ),
            pagingSourceFactory = { messageWithAttachmentsDao.postsByDialogId(chatId) }
        ).flow.map { data ->
            data.map { it.message }
        }
    }
}

