package blasty.domain.converters

import blasty.domain.entity.local.Chat
import com.connectycube.chat.model.ConnectycubeChatDialog

fun convertToChat(dialog: ConnectycubeChatDialog): Chat {
    return Chat(dialog.dialogId, dialog.type.code).apply {
        dialogId = dialog.dialogId
        lastMessage = dialog.lastMessage
        lastMessageDateSent = dialog.lastMessageDateSent
        lastMessageUserId = dialog.lastMessageUserId
        photo = dialog.photo
        userId = dialog.userId
        unreadMessageCount = dialog.unreadMessageCount ?: 0
        name = dialog.name
        setOccupantsIds(dialog.occupants)
        pinnedMessagesIds = dialog.pinnedMessagesIds
        type = dialog.type
        adminsIds = dialog.adminsIds
        customData = dialog.customData
        description = dialog.description
        occupantsCount = dialog.occupantsCount
        createdAt = dialog.createdAt
        updatedAt = dialog.updatedAt
    }
}