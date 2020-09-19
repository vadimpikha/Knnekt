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
            message.id,
            df.format(Date(message.dateSent * 1000)),
            message.body,
            message.readIds,
            message.deliveredIds,
            message.viewsCount,
            message.recipientId,
            message.senderId,
            message.markable,
            false,
            attachments?.map(::convert).orEmpty(),
            message.senderId != currentUserId
        )
    }


    private fun convert(attachment: AttachmentEntity): Attachment {
        return Attachment(
            attachment.attachmentId,
            attachment.url,
            attachment.attachmentType
        )
    }
}