package knnekt.presentation.mapper

import com.connectycube.chat.model.ConnectycubeDialogType
import knnekt.data.mapper.Mapper
import knnekt.domain.entity.Chat
import knnekt.presentation.entity.ChatItem
import knnekt.presentation.util.getPrettyDate

object ChatItemMapper : Mapper<Chat, ChatItem> {

    override fun convert(obj: Chat): ChatItem {
        return ChatItem(
            id = obj.id,
            lastMessage = obj.lastMessage.orEmpty(),
            lastMessageUser = "",
            photo = obj.photo,
            unreadMessageCount = createUnreadMsgsString(obj),
            name = obj.name,
            type = obj.type,
            updatedAt = getUpdateTime(obj),
            isPrivate = obj.type == ConnectycubeDialogType.PRIVATE.code
        )
    }

    private fun getUpdateTime(obj: Chat): String {
        var lastMessageDateSent: Long = obj.lastMessageDateSent * 1000
        if (lastMessageDateSent == 0L) lastMessageDateSent = obj.createdAt.time
        return getPrettyDate(lastMessageDateSent)
    }

    private fun createUnreadMsgsString(obj: Chat): String {
        return when (val count = obj.unreadMessageCount) {
            null, 0 -> ""
            in 1..100 -> count.toString()
            else -> "100+"
        }
    }

}