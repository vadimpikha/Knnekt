package knnekt.data.mapper

import knnekt.data.datasource.db.entity.ChatEntity
import knnekt.data.datasource.remote.entity.ChatRemoteEntity
import knnekt.domain.mapper.Mapper

object RemoteChatToEntityMapper: Mapper<ChatRemoteEntity, ChatEntity> {

    override fun convert(obj: ChatRemoteEntity): ChatEntity {
        return ChatEntity(
            chatId = obj.id,
            dialogType = obj.type,
            lastMessage = obj.lastMessage,
            lastMessageUserId = obj.lastMessageUserId,
            photo = obj.photo,
            unreadMessageCount = obj.unreadCount,
            name = obj.name,
            lastMessageDate = obj.lastMessageDate,
            occupants = obj.occupants,
            occupantsCount = obj.occupantsCount,
            createdAt = obj.createdAt
        )
    }

}