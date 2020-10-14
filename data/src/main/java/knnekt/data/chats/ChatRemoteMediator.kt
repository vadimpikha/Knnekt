package knnekt.data.chats

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import knnekt.data.datasource.db.AppDatabase
import knnekt.data.datasource.db.entity.ChatEntity
import knnekt.data.datasource.db.entity.ChatWithPrefsEntity
import knnekt.data.datasource.remote.ChatsRemoteDataSource
import knnekt.data.datasource.remote.entity.ChatRemoteEntity
import knnekt.domain.mapper.Mapper
import timber.log.Timber

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

            val skip = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.APPEND -> db.chatDao().size()
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            }

            Timber.d("Load chats. Skip $skip")
            val chats = chatsRemoteDataSource.getChats(limit,  skip)
                .map(remoteToEntityMapper::convert)

            Timber.d("Loaded chats: ${chats.map { it.lastMessageDate }}")

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.chatDao().nukeTable()
                }
                db.chatDao().insertAll(chats)
            }

            MediatorResult.Success(endOfPaginationReached = chats.isEmpty())
        } catch (exception: Exception) {
            exception.printStackTrace()
            MediatorResult.Error(exception)
        }
    }

}