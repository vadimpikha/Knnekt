package knnekt.data.mapper

import knnekt.data.datasource.db.entity.AttachmentEntity
import knnekt.data.datasource.remote.entity.MessageRemoteEntity
import knnekt.domain.mapper.Mapper

object RemoteMessageAttachmentMapper : Mapper<MessageRemoteEntity, List<AttachmentEntity>> {
    override fun convert(obj: MessageRemoteEntity): List<AttachmentEntity> {
        TODO("Not yet implemented")
    }
}