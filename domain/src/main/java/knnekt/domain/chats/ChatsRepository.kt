package knnekt.domain.chats

import androidx.paging.PagingData
import knnekt.domain.entity.Chat
import kotlinx.coroutines.flow.Flow

interface ChatsRepository {

    fun getChatsPagingData(): Flow<PagingData<Chat>>
    suspend fun updateChat(chatId: String)
    suspend fun archiveChat(chatId: String, archive: Boolean)

}