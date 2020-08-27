package knnekt.shared.data.mapper

import com.connectycube.chat.model.ConnectycubeAttachment
import knnekt.shared.data.db.MessageEntity
import knnekt.shared.data.entity.Attachment
import knnekt.shared.data.entity.Message
import java.text.SimpleDateFormat
import java.util.*

class MessageMapper(private val currentUserId: Int) : Mapper<MessageEntity, Message> {

    private val df = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun convert(obj: MessageEntity): Message {
        return Message(
            id = obj.id,
            dateSent = df.format(Date(obj.dateSent * 1000L)),
            body = obj.body,
            readIds = obj.readIds,
            deliveredIds = obj.deliveredIds,
            viewsCount = obj.viewsCount,
            recipientId = obj.recipientId,
            senderId = obj.senderId,
            markable = obj.isMarkable,
            delayed = obj.isDelayed,
            attachments = mapAttachments(obj.attachments),
            isIncoming = obj.senderId != currentUserId
        )
    }

    private fun mapAttachments(attachments: Collection<ConnectycubeAttachment>?): Collection<Attachment>? {
        return attachments?.map {
            Attachment(
                name = it.name,
                contentType = it.contentType,
                type = it.type,
                url = it.url,
                id = it.id,
                data = it.data,
                size = it.size,
                height = it.height,
                width = it.width,
                duration = it.duration
            )
        }
    }

}