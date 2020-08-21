package knnekt.shared.data.mapper

import com.connectycube.chat.model.ConnectycubeChatMessage
import knnekt.shared.data.db.MessageEntity

object RemoteMessageToEntityMapper: Mapper<ConnectycubeChatMessage, MessageEntity> {

    override fun convert(obj: ConnectycubeChatMessage): MessageEntity {
        return MessageEntity(obj.id).apply {
            id = obj.id
            body = obj.body
            dialogId = obj.dialogId
            dateSent = obj.dateSent
            senderId = obj.senderId
            recipientId = obj.recipientId
            readIds = obj.readIds
            deliveredIds = obj.deliveredIds
            attachments = obj.attachments
        }
    }

}