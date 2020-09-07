package knnekt.shared.data.messages

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.connectycube.chat.ConnectycubeRestChatService
import com.connectycube.chat.model.ConnectycubeAttachment
import com.connectycube.chat.model.ConnectycubeChatDialog
import com.connectycube.chat.model.ConnectycubeChatMessage
import com.connectycube.chat.request.MessageGetBuilder
import com.connectycube.core.request.RequestGetBuilder
import knnekt.shared.data.db.*
import knnekt.shared.data.mapper.Mapper
import knnekt.shared.data.util.await

@OptIn(ExperimentalPagingApi::class)
class MessageRemoteMediator(
    private val chatId: String,
    private val db: AppDatabase,
    private val remoteToEntityMapper: Mapper<ConnectycubeChatMessage, MessageEntity>
) : RemoteMediator<Int, MessageWithAttachmentsEntity>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageWithAttachmentsEntity>
    ): MediatorResult {

        try {
            val allDataSize = state.pages.sumBy { it.data.size }

            val request = MessageGetBuilder().apply {
                limit = state.config.pageSize
                skip = allDataSize
                markAsRead(false)
            }
            val dialogs = ConnectycubeRestChatService.getDialogMessages(
                ConnectycubeChatDialog(chatId),
                request
            ).await()

            val chats = dialogs.map(remoteToEntityMapper::convert)

            db.withTransaction {
//                if (loadType == LoadType.REFRESH) {
//                    db.attachmentDao().nukeTable()
//                    db.messageDao().nukeTable()
//                }

                db.messageDao().insertAll(chats)
                db.attachmentDao().insertAll(chats.flatMap { convert(it) })
            }

            return MediatorResult.Success(endOfPaginationReached = dialogs.isEmpty())
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }

    }

    fun convert(message: ConnectycubeChatMessage): List<AttachmentEntity> {
        return message.attachments?.map {
            AttachmentEntity(it.id, message.id, it.type).apply {
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