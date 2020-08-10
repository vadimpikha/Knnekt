package blasty.data.datasource.local

import blasty.domain.datasource.local.LocalChatsDataSource
import blasty.domain.entity.local.Chat
import kotlinx.coroutines.flow.Flow

class LocalChatsDataSourceImpl(private val chatDao: ChatDao): LocalChatsDataSource {

    override fun getChats(limit: Int, skip: Int): Flow<List<Chat>> {
       return chatDao.getChatsByPage(limit, skip)
    }

    override suspend fun saveChats(chats: List<Chat>) {
       chatDao.insertAll(chats)
    }
}