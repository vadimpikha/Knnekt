package knnekt.data.mapper

import knnekt.data.datasource.db.entity.AttachmentEntity
import knnekt.data.datasource.remote.entity.AttachmentRemoteEntity
import knnekt.data.datasource.remote.entity.MessageRemoteEntity
import knnekt.domain.mapper.Mapper

object RemoteMessageAttachmentMapper : Mapper<MessageRemoteEntity, List<AttachmentEntity>> {

    override fun convert(obj: MessageRemoteEntity): List<AttachmentEntity> {
        return obj.attachments.map { convert(it, obj.id) }
    }

    private fun convert(attachment: AttachmentRemoteEntity, msgId: String): AttachmentEntity {
        return AttachmentEntity(
            attachmentId = attachment.id,
            messageId = msgId,
            attachmentType = attachment.type,
            url = attachment.url
        )
    }
}