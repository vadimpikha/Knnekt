package knnekt.domain.messages

import androidx.paging.PagingData
import knnekt.domain.entity.Message
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {

    fun getMessagesPagingData(chatId: String): Flow<PagingData<Message>>
    suspend fun sendMessage(text: String, chatId: String)
    suspend fun refreshRecentMessages(chatId: String)

}