package knnekt.presentation.mapper

import com.connectycube.chat.model.ConnectycubeChatMessage
import knnekt.data.mapper.Mapper
import knnekt.presentation.entity.MessageItem

object MessageItemMapper : Mapper<ConnectycubeChatMessage, MessageItem> {


    override fun convert(obj: ConnectycubeChatMessage): MessageItem {
        return MessageItem(
            id = obj.id,
            dateSent = obj.dateSent,
            body = obj.body,
            readIds = obj.readIds,
            deliveredIds = obj.deliveredIds,
            viewsCount = obj.viewsCount,
            recipientId = obj.recipientId,
            senderId = obj.senderId,
            markable = obj.isMarkable,
            delayed = obj.isDelayed
        )
    }

}