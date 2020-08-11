package knnekt.data.mapper

import com.connectycube.chat.model.ConnectycubeChatDialog
import knnekt.domain.entity.Chat

object RemoteChatMapper : Mapper<ConnectycubeChatDialog, Chat> {

    override fun convert(obj: ConnectycubeChatDialog): Chat {
       return Chat(
           id = obj.dialogId,
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