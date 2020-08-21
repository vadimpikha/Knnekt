package knnekt.shared.data.db

import androidx.room.Embedded
import androidx.room.Relation
import knnekt.shared.data.db.AttachmentEntity
import knnekt.shared.data.db.MessageEntity

data class MessageWithAttachmentsEntity(
    @Embedded
    val message: MessageEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "messageId"
    )
    val attachments: List<AttachmentEntity>?
) {
    init {
        message.attachments = attachments
    }

    override fun toString() =
        "messageId= ${message.messageId}, body= ${message.body}, attachments= $attachments"
}