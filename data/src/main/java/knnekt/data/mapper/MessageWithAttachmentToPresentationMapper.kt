package knnekt.data.mapper

import knnekt.data.datasource.db.entity.MessageWithAttachmentsEntity
import knnekt.domain.entity.Message
import knnekt.domain.mapper.Mapper

object MessageWithAttachmentToPresentationMapper : Mapper<MessageWithAttachmentsEntity, Message> {
    override fun convert(obj: MessageWithAttachmentsEntity): Message {
        TODO("Not yet implemented")
    }
}