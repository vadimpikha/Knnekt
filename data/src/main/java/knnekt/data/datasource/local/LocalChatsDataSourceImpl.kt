package knnekt.data.datasource.local

import knnekt.data.entity.local.Chat
import knnekt.data.mapper.DualMapper
import knnekt.domain.datasource.local.LocalChatsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalChatsDataSourceImpl(
    private val chatDao: ChatDao,
    private val mapper: DualMapper<Chat, knnekt.domain.entity.Chat>
) : LocalChatsDataSource {

    override fun getChats(limit: Int, skip: Int): Flow<List<knnekt.domain.entity.Chat>> {
        return chatDao.getChatsByPage(limit, skip).map { list -> list.map(mapper::convert) }
    }

    override suspend fun saveChats(chats: List<knnekt.domain.entity.Chat>) {
        chatDao.insertAll(chats.map (mapper::convert2))
    }
}