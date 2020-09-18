package knnekt.data.mapper

import knnekt.data.datasource.db.entity.ChatEntity
import knnekt.data.datasource.remote.entity.ChatRemoteEntity
import knnekt.domain.mapper.Mapper

object RemoteChatToEntityMapper: Mapper<ChatRemoteEntity, ChatEntity> {

    override fun convert(obj: ChatRemoteEntity): ChatEntity {
        return ChatEntity(
            obj.id,
            obj.type,
            obj.lastMessage,
            obj.lastMessageUserId,
            obj.photo,
            obj.unreadCount,
            obj.name,
            obj.lastMessageDate,
            obj.occupants,
            obj.occupantsCount
        )
    }

}