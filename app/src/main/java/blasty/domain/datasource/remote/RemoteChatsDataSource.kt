package blasty.domain.datasource.remote

import blasty.domain.entity.local.Chat
import kotlinx.coroutines.flow.Flow

interface RemoteChatsDataSource {

    suspend fun getChats(limit: Int, skip: Int): List<Chat>

}