package knnekt.data.datasource.remote

import com.connectycube.chat.ConnectycubeRestChatService
import com.connectycube.chat.model.ConnectycubeChatDialog
import com.connectycube.core.request.GenericQueryRule
import com.connectycube.core.request.QueryRule
import com.connectycube.core.request.RequestGetBuilder
import knnekt.data.datasource.remote.entity.ChatRemoteEntity
import knnekt.data.util.await

interface ChatsRemoteDataSource {

    suspend fun getRecentChats(limit: Int): List<ChatRemoteEntity>
    suspend fun getChats(limit: Int, skip: Int): List<ChatRemoteEntity>
    suspend fun getChatsUpdatedAfter(limit: Int, date: Long): List<ChatRemoteEntity>
    suspend fun getChatById(chatId: String): ChatRemoteEntity

}

class ChatsRemoteDataSourceImpl : ChatsRemoteDataSource {

    override suspend fun getChatById(chatId: String): ChatRemoteEntity {
        val request = RequestGetBuilder().apply {
            this.limit = limit
            eq("_id", chatId)
        }
        return ConnectycubeRestChatService.getChatDialogs(null, request)
            .await().map(this::convert).single()
    }

    override suspend fun getRecentChats(limit: Int): List<ChatRemoteEntity> {
        val request = RequestGetBuilder().apply {
            this.limit = limit
        }
        return ConnectycubeRestChatService.getChatDialogs(null, request)
            .await().map(this::convert)
    }

    override suspend fun getChats(limit: Int, skip: Int): List<ChatRemoteEntity> {
        val request = RequestGetBuilder().apply {
            this.limit = limit
            this.skip = skip
        }
        return ConnectycubeRestChatService.getChatDialogs(null, request)
            .await().map(this::convert)
    }

    override suspend fun getChatsUpdatedAfter(limit: Int, date: Long): List<ChatRemoteEntity> {
        val request = RequestGetBuilder().apply {
            this.limit = limit
            gt("last_message_date_sent", date)
        }
        return ConnectycubeRestChatService.getChatDialogs(null, request)
            .await().map(this::convert)
    }


    private fun convert(chat: ConnectycubeChatDialog): ChatRemoteEntity {
        return ChatRemoteEntity(
            chat.dialogId,
            chat.lastMessage,
            chat.lastMessageDateSent,
            chat.lastMessageUserId ?: -1,
            chat.createdAt.time,
            chat.photo,
            chat.name,
            chat.unreadMessageCount,
            chat.type.code,
            chat.occupants?.toList().orEmpty(),
            chat.occupantsCount ?: 0
        )
    }

}