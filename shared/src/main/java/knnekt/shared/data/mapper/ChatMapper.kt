package knnekt.shared.data.mapper

import com.connectycube.chat.model.ConnectycubeDialogType
import knnekt.shared.data.db.ChatEntity
import knnekt.shared.data.entity.Chat
import knnekt.shared.utils.getPrettyDate

object ChatMapper : Mapper<ChatEntity, Chat> {

    override fun convert(obj: ChatEntity): Chat {
        return Chat(
            id = obj.chatId,
            lastMessage = obj.lastMessage.orEmpty(),
            lastMessageUser = "",
            photo = obj.photo,
            unreadMessageCount = createUnreadMsgsString(obj),
            name = obj.name,
            type = obj.type.code,
            updatedAt = getUpdateTime(obj),
            isPrivate = obj.type == ConnectycubeDialogType.PRIVATE,
            occupants = obj.occupants,
            occupantsCount = obj.occupantsCount
        )
    }

    private fun getUpdateTime(obj: ChatEntity): String {
        var lastMessageDateSent: Long = obj.lastMessageDateSent * 1000
        if (lastMessageDateSent == 0L) lastMessageDateSent = obj.createdAt?.time ?: return ""
        return getPrettyDate(lastMessageDateSent)
    }

    private fun createUnreadMsgsString(obj: ChatEntity): String {
        return when (val count = obj.unreadMessageCount) {
            null, 0 -> ""
            in 1..99 -> count.toString()
            else -> "99+"
        }
    }

}