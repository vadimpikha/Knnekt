package knnekt.data.mapper

import knnekt.data.datasource.db.entity.AttachmentEntity
import knnekt.data.datasource.db.entity.MessageWithAttachmentsEntity
import knnekt.domain.entity.Attachment
import knnekt.domain.entity.Message
import knnekt.domain.mapper.Mapper
import java.text.SimpleDateFormat
import java.util.*

class MessageWithAttachmentToPresentationMapper(
    private val currentUserId: Int
) : Mapper<MessageWithAttachmentsEntity, Message> {

    private val df = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun convert(obj: MessageWithAttachmentsEntity): Message {
        val message = obj.message
        val attachments = obj.attachments
        return Message(
            id = message.id,
            dateSent = df.format(Date(message.dateSent * 1000)),
            body = message.body,
            readIds = message.readIds,
            deliveredIds = message.deliveredIds,
            viewsCount = message.viewsCount,
            recipientId = message.recipientId,
            senderId = message.senderId,
            markable = message.markable,
            delayed = false,
            attachments = attachments?.map(::convert).orEmpty(),
            isIncoming = message.senderId != currentUserId,
            isTemp = message.isTemp
        )
    }


    private fun convert(attachment: AttachmentEntity): Attachment {
        return Attachment(
            id = attachment.attachmentId,
            url = attachment.url,
            type = attachment.attachmentType
        )
    }
}