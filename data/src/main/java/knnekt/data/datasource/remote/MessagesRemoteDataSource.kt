package knnekt.data.datasource.remote

import knnekt.data.datasource.remote.entity.MessageRemoteEntity

interface MessagesRemoteDataSource {

    suspend fun createMessage(text: String, userId: Int)
    suspend fun getRecentMessages(chatId: String, pageSize: Int): List<MessageRemoteEntity>
    suspend fun getMessagesAfter(chatId: String, limit: Int, date: Long): List<MessageRemoteEntity>
    suspend fun getMessagesBefore(chatId: String, limit: Int, date: Long): List<MessageRemoteEntity>

}

class MessagesRemoteDataSourceImpl: MessagesRemoteDataSource {

    override suspend fun createMessage(text: String, userId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getRecentMessages(
        chatId: String,
        pageSize: Int
    ): List<MessageRemoteEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getMessagesAfter(
        chatId: String,
        limit: Int,
        date: Long
    ): List<MessageRemoteEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getMessagesBefore(
        chatId: String,
        limit: Int,
        date: Long
    ): List<MessageRemoteEntity> {
        TODO("Not yet implemented")
    }

}