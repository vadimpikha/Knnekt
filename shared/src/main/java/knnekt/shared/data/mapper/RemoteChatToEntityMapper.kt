package knnekt.shared.data.mapper

import com.connectycube.chat.model.ConnectycubeChatDialog
import knnekt.shared.data.db.ChatEntity

object RemoteChatToEntityMapper : Mapper<ConnectycubeChatDialog, ChatEntity> {

    override fun convert(obj: ConnectycubeChatDialog): ChatEntity {
        return ChatEntity(obj.dialogId, obj.type.code).apply {
            dialogId = obj.dialogId
            lastMessage = obj.lastMessage
            lastMessageDateSent = obj.lastMessageDateSent
            lastMessageUserId = obj.lastMessageUserId
            photo = obj.photo
            userId = obj.userId
            unreadMessageCount = obj.unreadMessageCount ?: 0
            name = obj.name
            setOccupantsIds(obj.occupants)
            pinnedMessagesIds = obj.pinnedMessagesIds
            type = obj.type
            adminsIds = obj.adminsIds
            customData = obj.customData
            description = obj.description
            occupantsCount = obj.occupantsCount
            createdAt = obj.createdAt
            updatedAt = obj.updatedAt
        }
    }

}