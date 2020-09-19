package knnekt.data.messages

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import knnekt.data.datasource.db.AppDatabase
import knnekt.data.datasource.db.entity.AttachmentEntity
import knnekt.data.datasource.db.entity.MessageEntity
import knnekt.data.datasource.db.entity.MessageWithAttachmentsEntity
import knnekt.data.datasource.remote.MessagesRemoteDataSource
import knnekt.data.datasource.remote.entity.MessageRemoteEntity
import knnekt.domain.entity.Message
import knnekt.domain.mapper.Mapper
import knnekt.domain.messages.MessagesRepository
import knnekt.domain.prefs.PreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessagesRepositoryImpl(
    private val db: AppDatabase,
    private val messagesRemoteDataSource: MessagesRemoteDataSource,
    private val preferencesDataSource: PreferencesDataSource,
    private val remoteToEntityMapper: Mapper<MessageRemoteEntity, MessageEntity>,
    private val entityToPresentationMapper: Mapper<MessageWithAttachmentsEntity, Message>,
    private val attachmentMapper: Mapper<MessageRemoteEntity, List<AttachmentEntity>>
) : MessagesRepository {

    companion object {
        private const val PAGE_SIZE = 20
    }

    override suspend fun refreshRecentMessages(chatId: String) {
        val dialogs = messagesRemoteDataSource.getRecentMessages(chatId, PAGE_SIZE)
        db.withTransaction {
            db.messageDao().insertAll(dialogs.map(remoteToEntityMapper::convert))
            db.attachmentDao().insertAll(dialogs.flatMap(attachmentMapper::convert))
        }
    }

    override fun getMessagesPagingData(chatId: String): Flow<PagingData<Message>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = MessageRemoteMediator(
                chatId,
                db,
                messagesRemoteDataSource,
                remoteToEntityMapper,
                attachmentMapper
            ),
            pagingSourceFactory = { db.messageWithAttachmentDao().postsByDialogId(chatId) }
        ).flow.map { data ->
            data.map(entityToPresentationMapper::convert)
        }
    }

    override suspend fun sendMessage(text: String, chatId: String) {

        val userId = requireNotNull(preferencesDataSource.currentUser?.id)

        val message = MessageEntity(
            "",
            chatId,
            text,
            emptyList(),
            emptyList(),
            System.currentTimeMillis() / 1000,
            null,
            null,
            userId,
            false
        )

        val messageDao = db.messageDao()
//        messageDao.insert(remoteToEntityMapper.convert(message))
        messagesRemoteDataSource.createMessage(chatId, text, userId)

//        val sentMessage = ConnectycubeRestChatService.createMessage(message, true).await()
//        messageDao.update(remoteToEntityMapper.convert(sentMessage))
    }


}

