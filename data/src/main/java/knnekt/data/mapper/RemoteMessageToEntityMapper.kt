package knnekt.data.mapper

import knnekt.data.datasource.db.entity.MessageEntity
import knnekt.data.datasource.remote.entity.MessageRemoteEntity
import knnekt.domain.mapper.Mapper

object RemoteMessageToEntityMapper : Mapper<MessageRemoteEntity, MessageEntity> {

    override fun convert(obj: MessageRemoteEntity): MessageEntity {
        TODO("Not yet implemented")
    }

}