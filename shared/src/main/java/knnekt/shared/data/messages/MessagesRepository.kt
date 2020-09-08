package knnekt.shared.data.messages

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import com.connectycube.chat.ConnectycubeRestChatService
import com.connectycube.chat.model.ConnectycubeChatMessage
import knnekt.shared.data.chats.ChatMessagesRemoteSource
import knnekt.shared.data.db.*
import knnekt.shared.data.mapper.Mapper
import knnekt.shared.data.util.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface MessagesRepository {

    fun getMessagesPagingData(chatId: String): Flow<PagingData<MessageEntity>>
    suspend fun sendMessage(text: String, chatId: String)
    suspend fun refreshTopMessages(chatId: String)

}

class MessagesRepositoryImpl(
    private val db: AppDatabase,
    private val remoteSource: ChatMessagesRemoteSource,
    private val remoteToEntityMapper: Mapper<ConnectycubeChatMessage, MessageEntity>,
    private val attachmentMapper: Mapper<ConnectycubeChatMessage, List<AttachmentEntity>>,
    private val currentUserId: Int
) : MessagesRepository {

    companion object {
        private const val PAGE_SIZE = 20
    }

    override suspend fun refreshTopMessages(chatId: String) {
        val dialogs = remoteSource.getTop(chatId, PAGE_SIZE)
        db.withTransaction {
            db.messageDao().insertAll(dialogs.map(remoteToEntityMapper::convert))
            db.attachmentDao().insertAll(dialogs.flatMap(attachmentMapper::convert))
        }
    }

    override fun getMessagesPagingData(chatId: String): Flow<PagingData<MessageEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = MessageRemoteMediator(
                chatId,
                db,
                remoteSource,
                remoteToEntityMapper,
                attachmentMapper
            ),
            pagingSourceFactory = { db.messageWithAttachmentDao().postsByDialogId(chatId) }
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
        val messageDao = db.messageDao()
        messageDao.insert(remoteToEntityMapper.convert(message))
        val sentMessage = ConnectycubeRestChatService.createMessage(message, true).await()
        messageDao.update(remoteToEntityMapper.convert(sentMessage))
    }


}

