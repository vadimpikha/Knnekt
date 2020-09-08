package knnekt.shared.data.mapper

import com.connectycube.chat.model.ConnectycubeChatMessage
import knnekt.shared.data.db.AttachmentEntity

object AttachmentMapper : Mapper<ConnectycubeChatMessage, List<AttachmentEntity>> {

    override fun convert(obj: ConnectycubeChatMessage): List<AttachmentEntity> {
        return obj.attachments?.map {
            AttachmentEntity(it.id, obj.id, it.type).apply {
                name = it.name
                contentType = it.contentType
                type = it.type
                url = it.url
                id = it.id
                data = it.data
                size = it.size
                height = it.height
                width = it.width
                duration = it.duration
            }
        } ?: emptyList()
    }

}