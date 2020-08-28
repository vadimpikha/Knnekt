package knnekt.shared.data.messages

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.connectycube.chat.ConnectycubeRestChatService
import com.connectycube.chat.model.ConnectycubeChatMessage
import knnekt.shared.data.db.*
import knnekt.shared.data.mapper.Mapper
import knnekt.shared.data.util.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

interface MessagesRepository {

    fun getMessagesPagingData(chatId: String): Flow<PagingData<MessageEntity>>
    suspend fun sendMessage(text: String, chatId: String)

}

class MessagesRepositoryImpl(
    private val messageDao: MessageDao,
    private val attachmentDao: AttachmentDao,
    private val messageWithAttachmentsDao: MessageWithAttachmentsDao,
    private val remoteToEntityMapper: Mapper<ConnectycubeChatMessage, MessageEntity>,
    private val currentUserId: Int
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

    override suspend fun sendMessage(text: String, chatId: String) {
        val message = ConnectycubeChatMessage().apply {
            body = text
            dialogId = chatId
            dateSent = System.currentTimeMillis() / 1000
            senderId = currentUserId
        }
        messageDao.insert(remoteToEntityMapper.convert(message))
        val (sentMessage, _)
                = ConnectycubeRestChatService.createMessage(message, true).await()
        messageDao.update(remoteToEntityMapper.convert(sentMessage))
    }


}

