package knnekt.shared.data.messages

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.connectycube.chat.model.ConnectycubeChatMessage
import knnekt.shared.data.chats.ChatMessagesRemoteSource
import knnekt.shared.data.db.*
import knnekt.shared.data.mapper.Mapper
import timber.log.Timber
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class MessageRemoteMediator(
    private val chatId: String,
    private val db: AppDatabase,
    private val remoteSource: ChatMessagesRemoteSource,
    private val remoteToEntityMapper: Mapper<ConnectycubeChatMessage, MessageEntity>,
    private val attachmentMapper: Mapper<ConnectycubeChatMessage, List<AttachmentEntity>>
) : RemoteMediator<Int, MessageWithAttachmentsEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageWithAttachmentsEntity>
    ): MediatorResult {

        try {

            val limit = state.config.pageSize

            Timber.d("load with $loadType")

            val dialogs = when (loadType) {
                LoadType.REFRESH -> {
                    val date = getSendDateClosestToCurrentPosition(state)

                    if (date == null) {
                        Timber.d("Get top")
                        remoteSource.getTop(chatId, limit).also {
                            Timber.d("Loaded: ${it.joinToString("; ") { it.body }}")
                        }
                    } else {
                        Timber.d("Get top after $date")
                        remoteSource.getTopAfter(chatId, limit, date).also {
                            Timber.d("Loaded: ${it.map { it.body }.joinToString("; ")}")
                        }
                    }
                }
                LoadType.PREPEND -> {
                    val date = getSendDateForFirstItem(state)
                        ?: throw InvalidObjectException("Result is empty")

                    Timber.d("Get top after $date")
                    remoteSource.getTopAfter(chatId, limit, date).also {
                        Timber.d("Loaded: ${it.map { it.body }.joinToString("; ")}")
                    }
                }
                LoadType.APPEND -> {
                    val date = getSendDateForLastItem(state)
                        ?: throw InvalidObjectException("Result is empty")

                    Timber.d("Get top before $date")
                    remoteSource.getTopBefore(chatId, limit, date).also {
                        Timber.d("Loaded: ${it.map { it.body }.joinToString("; ")}")
                    }
                }
            }

            val chats = dialogs.map(remoteToEntityMapper::convert)

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.attachmentDao().nukeTable()
                    db.messageDao().nukeTable()
                }

                db.messageDao().insertAll(chats)
                db.attachmentDao().insertAll(chats.flatMap(attachmentMapper::convert))
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