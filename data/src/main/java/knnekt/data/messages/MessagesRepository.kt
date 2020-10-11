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
import knnekt.data.util.processScope
import knnekt.domain.entity.Message
import knnekt.domain.mapper.Mapper
import knnekt.domain.messages.MessagesRepository
import knnekt.domain.prefs.PreferencesDataSource
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        val createdMessage = messagesRemoteDataSource.createMessage(chatId, text, userId)
//        db.messageDao().insert(remoteToEntityMapper.convert(createdMessage))
        val sentMessage = messagesRemoteDataSource.sendMessage(createdMessage)
        db.messageDao().insert(remoteToEntityMapper.convert(sentMessage))
    }


}

