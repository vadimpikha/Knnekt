package knnekt.data.chats

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import knnekt.data.datasource.db.AppDatabase
import knnekt.data.datasource.db.entity.ChatEntity
import knnekt.data.datasource.db.entity.ChatWithPrefsEntity
import knnekt.data.datasource.remote.ChatsRemoteDataSource
import knnekt.data.datasource.remote.entity.ChatRemoteEntity
import knnekt.domain.mapper.Mapper
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class ChatRemoteMediator(
    private val db: AppDatabase,
    private val chatsRemoteDataSource: ChatsRemoteDataSource,
    private val remoteToEntityMapper: Mapper<ChatRemoteEntity, ChatEntity>
) : RemoteMediator<Int, ChatWithPrefsEntity>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ChatWithPrefsEntity>
    ): MediatorResult {

        return try {

            val limit = state.config.pageSize

            val chats = when (loadType) {
                LoadType.REFRESH -> {
                    val date = getLastMessageDateClosestToCurrentPosition(state)

                    if (date == null) {
                        chatsRemoteDataSource.getRecentChats(state.config.initialLoadSize)
                    } else {
                        chatsRemoteDataSource.getChatsUpdatedAfter(limit, date)
                    }
                }
                LoadType.PREPEND -> {
                    val date = getLastMessageDateOfFirstItem(state)
                        ?: throw InvalidObjectException("Result is empty")

                    chatsRemoteDataSource.getChatsUpdatedAfter(limit, date)
                }
                LoadType.APPEND -> {
                    val date = getLastMessageDateOfLastItem(state)
                        ?: throw InvalidObjectException("Result is empty")

                    chatsRemoteDataSource.getChatsUpdatedBefore(limit, date)
                }
            }.map(remoteToEntityMapper::convert)

            db.chatDao().insertAll(chats)

            MediatorResult.Success(endOfPaginationReached = chats.isEmpty())
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

    private fun getLastMessageDateOfLastItem(state: PagingState<Int, ChatWithPrefsEntity>): Long? {
        return state.lastItemOrNull()?.chat?.let { chat ->
            if (chat.lastMessageDate != 0L)
                chat.lastMessageDate
            else
                chat.createdAt
        }
    }

    private fun getLastMessageDateOfFirstItem(state: PagingState<Int, ChatWithPrefsEntity>): Long? {
        return state.firstItemOrNull()?.chat?.let { chat ->
            if (chat.lastMessageDate != 0L)
                chat.lastMessageDate
            else
                chat.createdAt
        }
    }

    private fun getLastMessageDateClosestToCurrentPosition(state: PagingState<Int, ChatWithPrefsEntity>): Long? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.chat?.let { chat ->
                if (chat.lastMessageDate != 0L)
                    chat.lastMessageDate
                else
                    chat.createdAt
            }
        }
    }

}