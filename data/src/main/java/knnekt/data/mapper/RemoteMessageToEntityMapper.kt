package knnekt.data.mapper

import knnekt.data.datasource.db.entity.MessageEntity
import knnekt.data.datasource.remote.entity.MessageRemoteEntity
import knnekt.domain.mapper.Mapper

object RemoteMessageToEntityMapper : Mapper<MessageRemoteEntity, MessageEntity> {

    override fun convert(obj: MessageRemoteEntity): MessageEntity {
        return MessageEntity(
            id = obj.id,
            chatId = obj.chatId,
            body = obj.body,
            readIds = obj.readIds,
            deliveredIds = obj.deliveredIds,
            dateSent = obj.dateSend,
            viewsCount = obj.viewsCount,
            recipientId = obj.recipientId,
            senderId = obj.senderId,
            markable = obj.markable
        )
    }

}