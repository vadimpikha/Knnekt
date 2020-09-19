package knnekt.data.messages

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import knnekt.data.datasource.db.AppDatabase
import knnekt.data.datasource.db.entity.AttachmentEntity
import knnekt.data.datasource.db.entity.MessageEntity
import knnekt.data.datasource.db.entity.MessageWithAttachmentsEntity
import knnekt.data.datasource.remote.MessagesRemoteDataSource
import knnekt.data.datasource.remote.entity.MessageRemoteEntity
import knnekt.domain.mapper.Mapper
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class MessageRemoteMediator(
    private val chatId: String,
    private val db: AppDatabase,
    private val remoteSource: MessagesRemoteDataSource,
    private val remoteToEntityMapper: Mapper<MessageRemoteEntity, MessageEntity>,
    private val attachmentMapper: Mapper<MessageRemoteEntity, List<AttachmentEntity>>
) : RemoteMediator<Int, MessageWithAttachmentsEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageWithAttachmentsEntity>
    ): MediatorResult {

        try {

            val limit = state.config.pageSize

            val dialogs = when (loadType) {
                LoadType.REFRESH -> {
                    val date = getSendDateClosestToCurrentPosition(state)

                    if (date == null) {
                        remoteSource.getRecentMessages(chatId, state.config.initialLoadSize)
                    } else {
                        remoteSource.getMessagesAfter(chatId, limit, date)
                    }
                }
                LoadType.PREPEND -> {
                    val date = getSendDateForFirstItem(state)
                        ?: throw InvalidObjectException("Result is empty")

                    remoteSource.getMessagesAfter(chatId, limit, date)
                }
                LoadType.APPEND -> {
                    val date = getSendDateForLastItem(state)
                        ?: throw InvalidObjectException("Result is empty")

                    remoteSource.getMessagesBefore(chatId, limit, date)
                }
            }

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    val messagesIds = db.messageDao().messagesIdsByDialogId(chatId)
                    db.attachmentDao().deleteByMessageId(*messagesIds)
                    db.messageDao().deleteByDialogId(chatId)
                }

                db.messageDao().insertAll(dialogs.map(remoteToEntityMapper::convert))
                db.attachmentDao().insertAll(dialogs.flatMap(attachmentMapper::convert))
            }

            return MediatorResult.Success(endOfPaginationReached = dialogs.isEmpty())
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }

    }

    private fun getSendDateForLastItem(state: PagingState<Int, MessageWithAttachmentsEntity>): Long? {
        return state.lastItemOrNull()?.message?.dateSent
    }

    private fun getSendDateForFirstItem(state: PagingState<Int, MessageWithAttachmentsEntity>): Long? {
        return state.firstItemOrNull()?.message?.dateSent
    }


    private fun getSendDateClosestToCurrentPosition(state: PagingState<Int, MessageWithAttachmentsEntity>): Long? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.message?.dateSent
        }
    }


}