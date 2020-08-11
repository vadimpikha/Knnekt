package knnekt.data.repository

import knnekt.domain.datasource.local.LocalChatsDataSource
import knnekt.domain.datasource.remote.RemoteChatsDataSource
import knnekt.domain.entity.Chat
import knnekt.domain.entity.internal.Resource
import knnekt.domain.repository.ChatsRepository
import knnekt.domain.util.networkBoundResource
import kotlinx.coroutines.flow.Flow

class ChatsRepositoryImpl(
    private val localChatsDataSource: LocalChatsDataSource,
    private val remoteChatsDataSource: RemoteChatsDataSource
): ChatsRepository {

    override fun getChats(limit: Int, skip: Int): Flow<Resource<List<Chat>>> {
        return networkBoundResource(
            query = { localChatsDataSource.getChats(limit, skip) },
            fetch = { remoteChatsDataSource.getChats(limit, skip) },
            saveFetchResult = { chats -> localChatsDataSource.saveChats(chats) }
        )
    }

}