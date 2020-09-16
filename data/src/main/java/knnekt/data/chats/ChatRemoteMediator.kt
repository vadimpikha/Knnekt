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
            val allDataSize = state.pages.sumBy { it.data.size }

            val chats = chatsRemoteDataSource.getChats().map(remoteToEntityMapper::convert)

            db.chatDao().insertAll(chats)

            MediatorResult.Success(endOfPaginationReached = chats.isEmpty())
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

}