package blasty.domain.datasource.local

import blasty.domain.entity.local.Chat
import kotlinx.coroutines.flow.Flow

interface LocalChatsDataSource {

    fun getChats(limit: Int, skip: Int): Flow<List<Chat>>
    suspend fun saveChats(chats: List<Chat>)

}