package knnekt.domain.datasource.local

import knnekt.domain.entity.Chat
import kotlinx.coroutines.flow.Flow

interface LocalChatsDataSource {

    fun getChats(limit: Int, skip: Int): Flow<List<Chat>>
    suspend fun saveChats(chats: List<Chat>)
    fun getChatById(id: String): Flow<Chat>
    suspend fun updateChat(chat: Chat)

}