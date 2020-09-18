package knnekt.data.datasource.remote

import com.connectycube.chat.ConnectycubeRestChatService
import com.connectycube.chat.model.ConnectycubeChatDialog
import com.connectycube.core.request.RequestGetBuilder
import knnekt.data.datasource.remote.entity.ChatRemoteEntity
import knnekt.data.util.await

interface ChatsRemoteDataSource {

    suspend fun getChats(): List<ChatRemoteEntity>

}

class ChatsRemoteDataSourceImpl : ChatsRemoteDataSource {

    override suspend fun getChats(): List<ChatRemoteEntity> {
        val request = RequestGetBuilder()
        return ConnectycubeRestChatService.getChatDialogs(null, request)
            .await().map(this::convert).also {
                println("=======CHATS==========")
                println(it)
            }
    }


    private fun convert(chat: ConnectycubeChatDialog): ChatRemoteEntity {
        return ChatRemoteEntity(
            chat.dialogId,
            chat.lastMessage,
            chat.lastMessageDateSent,
            chat.lastMessageUserId ?: -1,
            chat.photo,
            chat.name,
            chat.unreadMessageCount,
            chat.type.code,
            chat.occupants?.toList().orEmpty(),
            chat.occupantsCount ?: 0
        )
    }

}