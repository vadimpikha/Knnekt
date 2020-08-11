package knnekt.data.mapper

import com.connectycube.chat.model.ConnectycubeDialogType
import knnekt.data.entity.local.Chat

object DomainDataChatsMapper : DualMapper<Chat, knnekt.domain.entity.Chat> {

    override fun convert2(obj: knnekt.domain.entity.Chat): Chat {
        return Chat(obj.id, obj.type).apply {
            dialogId = obj.id
            lastMessage = obj.lastMessage
            lastMessageDateSent = obj.lastMessageDateSent
            lastMessageUserId = obj.lastMessageUserId
            photo = obj.photo
            userId = obj.userId
            unreadMessageCount = obj.unreadMessageCount ?: 0
            name = obj.name
            setOccupantsIds(obj.occupants)
            pinnedMessagesIds = obj.pinnedMessagesIds
            type = ConnectycubeDialogType.parseByCode(obj.type)
            adminsIds = obj.adminsIds
            description = obj.description
            occupantsCount = obj.occupantsCount
            createdAt = obj.createdAt
            updatedAt = obj.updatedAt
        }
    }

    override fun convert(obj: Chat): knnekt.domain.entity.Chat {
        return knnekt.domain.entity.Chat(
            id = obj.chatId,
            lastMessage = obj.lastMessage,
            lastMessageDateSent = obj.lastMessageDateSent,
            lastMessageUserId = obj.lastMessageUserId,
            photo = obj.photo,
            userId = obj.userId,
            roomJid = obj.roomJid,
            unreadMessageCount = obj.unreadMessageCount,
            name = obj.name,
            type = obj.type.code,
            description = obj.description,
            occupantsCount = obj.occupantsCount,
            occupants = obj.occupants,
            pinnedMessagesIds = obj.pinnedMessagesIds,
            adminsIds = obj.adminsIds,
            createdAt = obj.createdAt,
            updatedAt = obj.updatedAt
        )
    }

}